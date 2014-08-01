package stream.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

/**
 * An OperatorDefinition defines an Operator.
 * 
 * An OperatorDefinition consists of (1) a String type name (e.g., "SelectionOperator") and (2) an array of arguments
 * where each argument is either a String object or an array of String objects.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class OperatorDefinition implements java.io.Serializable {

	/**
	 * A StringTokenizer tokenizes a given string for an OperatorDefinition.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 * 
	 */
	public static class StringTokenizer extends java.util.StringTokenizer {

		/**
		 * Constructs StringTokenizer for parsing the specified string.
		 * 
		 * @param s
		 *            a string.
		 */
		public StringTokenizer(String s) {
			super(s, "@()[],", true);
		}

		@Override
		public String nextToken() {
			String token = super.nextToken().trim();
			while (token.equals(""))
				token = super.nextToken().trim();
			return token;
		}

	}

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -6835888131357438854L;

	/**
	 * The type of the Operator.
	 */
	protected String type;

	/**
	 * The arguments needed to construct the Operator.
	 */
	protected Object[] arguments;

	/**
	 * Constructs an OperatorDefinition.
	 * 
	 * @param type
	 *            the type of Operator.
	 * @param arguments
	 *            the arguments needed to construct the Operator.
	 */
	public OperatorDefinition(String type, Object[] arguments) {
		this.type = type;
		this.arguments = arguments;
	}

	/**
	 * Constructs an OperatorDefinition using the specified tokenizer.
	 * 
	 * @param tokenizer
	 *            the tokenizer for parsing the definition of an operator.
	 * @throws ParsingException
	 *             if a parsing error occurs.
	 */
	public OperatorDefinition(StringTokenizer tokenizer) throws ParsingException {
		try {
			this.type = tokenizer.nextToken();
			tokenizer.nextToken(); // (
			Vector<Object> arguments = new Vector<Object>();
			String token;
			while (!(token = tokenizer.nextToken()).equals(")")) {
				if (token.equals(","))
					token = tokenizer.nextToken();
				if (token.equals("[")) {
					arguments.add(getCollection(tokenizer).toArray(new String[1]));
				} else
					arguments.add(token);
			}
			this.arguments = arguments.toArray();
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	/**
	 * Construct an OperatorDefinition according to the specified String representation of an Operator.
	 * 
	 * @param string
	 *            a String representation of an Operator.
	 * @throws ParsingException
	 *             if a parsing error occurs.
	 */
	public OperatorDefinition(String string) throws ParsingException {
		this(new StringTokenizer(string));
	}

	@Override
	public String toString() {
		String s = "";
		for (Object argument : arguments) {
			if (s.length() > 0)
				s += ", ";
			if (argument.getClass().isArray())
				s += Arrays.toString((String[]) argument);
			else
				s += argument;
		}
		return type + "(" + s + ")";
	}

	/**
	 * Returns the type of the Operator.
	 * 
	 * @return the type of the Operator.
	 */
	public String type() {
		return type;
	}

	/**
	 * Returns the arguments needed to construct the Operator.
	 * 
	 * @return the arguments needed to construct the Operator.
	 */
	public Object[] arguments() {
		return arguments;
	}

	/**
	 * Returns a collection of Strings obtained from the specified String iterator.
	 * 
	 * @param tokenizer
	 *            the tokenizer for parsing arguments.
	 * @return a collection of Strings obtained from the specified String iterator.
	 */
	protected Collection<String> getCollection(StringTokenizer tokenizer) {
		Vector<String> collection = new Vector<String>();
		String token;
		String argument = "";
		while (!(token = tokenizer.nextToken()).equals("]")) {
			if (token.equals(",")) {
				if (argument.length() > 0)
					collection.add(argument);
				argument = "";
			} else
				argument += token;
		}
		if (argument.length() > 0)
			collection.add(argument);
		return collection;
	}

	/**
	 * A ParsingException represents a parsing error.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 */
	public class ParsingException extends Exception {

		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID = -7739391895010729791L;

		/**
		 * Constructs a ParsingException.
		 * 
		 * @param t
		 *            the cause of this ParsingException.
		 */
		public ParsingException(Throwable t) {
			super(t);
		}

	}

}
