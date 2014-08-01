package stream;

import java.util.Vector;

import stream.data.Tuple;
import stream.query.OperatorDefinition;
import stream.query.OperatorID;
import stream.query.QueryEngine.NoOperatorException;
import stream.util.net.CommunicationException;
import stream.util.net.LookupException;

/**
 * WorkerInterface is the interface for Workers.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public interface WorkerInterface extends stream.util.system.WorkerInterface {

	/**
	 * Constructs an Operator according to the specified OperatorDefinition.
	 * 
	 * @param name
	 *            the name of the Operator.
	 * @param definition
	 *            the definition of the Operator to create.
	 * @param inputOperators
	 *            the IDs of the input Operators.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void createOperator(String name, OperatorDefinition definition, Vector<OperatorID> inputOperators)
			throws Exception;

	/**
	 * Adds a Relay that sends data from the specified Operator to the specified consumer Operator.
	 * 
	 * @param operatorName
	 *            the name of the Operator to provide data to the Relay.
	 * @param consumerOperator
	 *            the ID of the consumer Operator.
	 * @param port
	 *            the port of the consumer Operator.
	 * @throws LookupException
	 *             if the specified Worker cannot be found.
	 * @throws CommunicationException
	 *             if there is a communication exception.
	 * @throws NoOperatorException
	 *             if the specified Operator cannot be found.
	 */
	public void addRelay(String operatorName, OperatorID consumerOperator, int port) throws LookupException,
			CommunicationException, NoOperatorException;

	/**
	 * Processes the specified Tuples using the specified Operator.
	 * 
	 * @param name
	 *            the name of the Operator.
	 * @param port
	 *            the input port of the Operator.
	 * @param tuples
	 *            the tuples to process.
	 * @throws Exception
	 *             if an error occurs.
	 */
	void process(String name, int port, Vector<Tuple> tuples) throws Exception;

}
