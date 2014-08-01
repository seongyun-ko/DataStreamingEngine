package stream;

/**
 * MasterInterface is the interface for the Master class.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public interface MasterInterface extends stream.util.system.MasterInterface {

	/**
	 * Constructs an Operator according to the specified OperatorDefinition.
	 * 
	 * @param operatorID
	 *            the ID of the Operator to create.
	 * @param definition
	 *            the OperatorDefinition.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void createOperator(String operatorID, String definition) throws Exception;

}
