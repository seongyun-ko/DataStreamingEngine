package stream.query.operator;

import java.util.Vector;

import stream.WorkerInterface;
import stream.data.Tuple;

/**
 * A RelayOperator sends received Tuples to a remote Operator.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class RelayOperator extends Operator {

	/**
	 * A remote Worker.
	 */
	protected WorkerInterface worker;

	/**
	 * The name of an Operator.
	 */
	protected String operatorName;

	/**
	 * A port of an Operator.
	 */
	protected int operatorPort;

	/**
	 * A collection of Tuples.
	 */
	protected Vector<Tuple> tuples = new Vector<Tuple>();

	/**
	 * Constructs a RelayOperator.
	 * 
	 * @param worker
	 *            the worker.
	 * @param operatorName
	 *            the name of the Operator.
	 * @param operatorPort
	 *            the port of the Operator.
	 */
	public RelayOperator(WorkerInterface worker, String operatorName, int operatorPort) {
		this.worker = worker; // proxy of the remote worker.
		this.operatorName = operatorName;
		this.operatorPort = operatorPort;
	}

	@Override
	public void process(int port, Tuple t) {
		tuples.add(t);
		try {
			worker.process(operatorName, operatorPort, tuples);
			tuples.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
