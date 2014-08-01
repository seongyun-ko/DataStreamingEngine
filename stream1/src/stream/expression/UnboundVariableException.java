package stream.expression;

/**
 * An UnboundVariableException is thrown if an expression being evaluated has a variable whose value is not set.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class UnboundVariableException extends Exception {

	public UnboundVariableException(String name) {
		super(name);
	}

	/**
	 * Automatically generated serial version UID.
	 */
	private static final long serialVersionUID = 2293708101502204882L;

}
