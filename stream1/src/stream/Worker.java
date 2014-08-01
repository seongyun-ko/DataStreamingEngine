package stream;

import java.util.Vector;

import stream.data.Tuple;
import stream.query.OperatorDefinition;
import stream.query.OperatorID;
import stream.query.QueryEngine;
import stream.query.QueryEngine.NoOperatorException;
import stream.query.operator.RelayOperator;
import stream.util.net.CommunicationException;
import stream.util.net.LookupException;

/**
 * A Worker manages stream data.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Worker extends stream.util.system.Worker implements WorkerInterface {

	/**
	 * The QueryEngine for this Worker.
	 */
	protected QueryEngine queryEngine = new QueryEngine();

	/**
	 * Constructs a Worker
	 * 
	 * @param workerID
	 *            the ID of the Worker.
	 * @param port
	 *            the port number of the Worker.
	 * @param masterAddress
	 *            the address of the Master.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public Worker(int workerID, int port, String masterAddress)
			throws Exception {
		super(workerID, port, masterAddress, MasterInterface.class);
	}

	/**
	 * Returns the Master.
	 * 
	 * @return the Master.
	 */
	public MasterInterface master() {
		return (MasterInterface) master;
	}

	/**
	 * Returns the QueryEngine for this Worker.
	 * 
	 * @return the QueryEngine for this Worker.
	 */
	public QueryEngine queryEngine() {
		return queryEngine;
	}

	/**
	 * Returns the specified Worker.
	 * 
	 * @param workerID
	 *            the ID of the Worker.
	 * @return the specified Worker.
	 * @throws LookupException
	 *             if the Worker cannot be looked up.
	 * @throws CommunicationException
	 *             if the communication fails.
	 */
	public WorkerInterface worker(int workerID) throws LookupException, CommunicationException {
		return (WorkerInterface) super.worker(workerID);
	}

	@Override
	public void createOperator(String name, OperatorDefinition description, Vector<OperatorID> inputs) throws Exception {
		queryEngine.createOperator(name, description);
		int i = 0;
		for (OperatorID input : inputs) {
			connect(input, name, i++);
		}
	}

	protected void connect(OperatorID input, String name, int port) throws NoOperatorException, LookupException,
			CommunicationException {
		if (input.workerID() == workerID) // the produce operator is on this Worker.
			queryEngine.connect(input.operatorName(), name, port);
		else
			worker(input.workerID()).addRelay(input.operatorName(), new OperatorID(name, workerID), port);
	}

	@Override
	public void shutdown() {
		queryEngine.shutdown();
		super.shutdown();
	}

	@Override
	public void addRelay(String operatorName, OperatorID consumerOperator, int port) throws LookupException,
			CommunicationException, NoOperatorException {
		RelayOperator o = new RelayOperator(worker(consumerOperator.workerID()), consumerOperator.operatorName(), port);
		queryEngine.operator(operatorName).addConsumer(o, 0);
	}

	@Override
	public void process(String name, int port, Vector<Tuple> tuples) throws Exception {
		queryEngine.process(name, port, tuples);
	}
}