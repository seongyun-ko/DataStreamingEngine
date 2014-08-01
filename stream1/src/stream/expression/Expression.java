package stream.expression;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * An ArithmeticExpression represents an arithmetic expression.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Expression {

	/**
	 * The plus symbol.
	 */
	public static String plus = "+";

	/**
	 * The minus symbol.
	 */
	public static String minus = "-";

	/**
	 * The times symbol.
	 */
	public static String times = "*";

	/**
	 * The slash symbol.
	 */
	public static String slash = "/";

	/**
	 * The left parenthesis.
	 */
	public static String lparen = "(";

	/**
	 * The right parenthesis.
	 */
	public static String rparen = ")";

	/**
	 * The equality symbol.
	 */
	public static String eq = "=";

	/**
	 * The "less than" symbol.
	 */
	public static String lt = "<";

	/**
	 * The "greater than" symbol.
	 */
	public static String gt = ">";
	
	/**
	 * The "greater than or equality" symbol.
	 */
	public static String gteq = ">=";
	public static String lteq = "<=";

	/**
	 * The variables contained in this ArithmeticExpression.
	 */
	protected HashMap<String, Variable> variables = new HashMap<String, Variable>();

	/**
	 * The root note of the parse tree.
	 */
	protected Node root; 

	/**
	 * Sets the value of the specified variable.
	 * 
	 * @param name
	 *            the name of the variable.
	 * @param val
	 *            the value.
	 * @throws UnregisteredVariableException
	 *             if an unregistered variable is referenced.
	 */
	public void setVariable(String name, Object val) throws UnregisteredVariableException {
		Variable v = variables.get(name);
		if (v == null)
			throw new UnregisteredVariableException();
		v.set(val);
	}

	/**
	 * Evaluates this ArithmeticExpression.
	 * 
	 * @return the result of evaluating this ArithmeticExpression.
	 * @throws UnboundVariableException
	 *             if this ArithmeticExpression contains a variable whose value is not set.
	 */
	public Object evaluate() throws UnboundVariableException {
		return root.evaluate();
	}

	/**
	 * Returns the variables contained in this ArithmeticExpression.
	 * 
	 * @return the variables contained in this ArithmeticExpression.
	 */
	public Iterable<Variable> variables() {
		return variables.values();
	}

	/**
	 * Prints this ArithmeticExpression.
	 * 
	 * @param out
	 *            a PrintStream.
	 */
	public void print(PrintStream out) {
		root.print(out, 0);
	}

	/**
	 * Parses the condition from the specified tokenizer.
	 * 
	 * @param tokenizer
	 *            a tokenizer.
	 * @return a parse tree representing an expression.
	 * @throws ParsingException
	 *             if an error occurs while parsing an expression.
	 */
	protected Node condition(StringTokenizer tokenizer) throws ParsingException {
		Node node = expression(tokenizer);
		if (tokenizer.isCurrentToken(eq)) {
			tokenizer.next();
			return new EqualTo(node, expression(tokenizer));
		} else if (tokenizer.isCurrentToken(lt)) {
			tokenizer.next();
			if(tokenizer.isCurrentToken(eq)) {
				tokenizer.next();
				return new LessThanOrEqualTo(node, expression(tokenizer));
			}	
			else
				return new LessThan(node, expression(tokenizer));
		} else if (tokenizer.isCurrentToken(gt)) {
			tokenizer.next();
			if(tokenizer.isCurrentToken(eq)) {
				tokenizer.next();
				return new GreaterThanOrEqualTo(node, expression(tokenizer));
			}
			else
				return new GreaterThan(node, expression(tokenizer));
		} else
			throw new ParsingException();
	}

	/**
	 * Parses the expression from the specified tokenizer.
	 * 
	 * @param tokenizer
	 *            a tokenizer.
	 * @return a parse tree representing an expression.
	 * @throws ParsingException
	 *             if an error occurs while parsing an expression.
	 */
	protected Node expression(StringTokenizer tokenizer) throws ParsingException {
		boolean negation = false;
		if (tokenizer.isCurrentToken(plus) || tokenizer.isCurrentToken(minus)) {
			if (tokenizer.isCurrentToken(minus))
				negation = true;
			tokenizer.next();
		}
		Node node = negation ? new Negation(term(tokenizer)) : term(tokenizer);
		while (tokenizer.isCurrentToken(plus) || tokenizer.isCurrentToken(minus)) {
			String operation = tokenizer.currentToken();
			tokenizer.next();
			node = operation.equals(plus) ? new Addition(node, term(tokenizer))
					: new Subtraction(node, term(tokenizer));
		}
		return node;
	}

	/**
	 * Parses the term from the specified tokenizer.
	 * 
	 * @param tokenizer
	 *            a tokenizer.
	 * @return a parse tree representing a term.
	 * @throws ParsingException
	 *             if an error occurs while parsing a term.
	 */
	protected Node term(StringTokenizer tokenizer) throws ParsingException {
		Node node = factor(tokenizer);
		while (tokenizer.isCurrentToken(times) || tokenizer.isCurrentToken(slash)) {
			String operation = tokenizer.currentToken();
			tokenizer.next();
			node = operation.equals(times) ? new Multiplication(node, factor(tokenizer)) : new Division(node,
					factor(tokenizer));
		}
		return node;
	}

	/**
	 * Parses the factor from the specified tokenizer.
	 * 
	 * @param tokenizer
	 *            a tokenizer.
	 * @return a parse tree representing a factor.
	 * @throws ParsingException
	 *             if an error occurs while parsing a factor.
	 */
	protected Node factor(StringTokenizer tokenizer) throws ParsingException {
		if (tokenizer.isCurrentToken(lparen)) {
			tokenizer.next();
			Node node = expression(tokenizer);
			if (!tokenizer.isCurrentToken(rparen))
				throw new ParsingException();
			tokenizer.next();
			return node;
		} else if (tokenizer.isCurrentTokenNumeric()) {
			LeafNode c = new LeafNode(StringTokenizer.str2Number(tokenizer.currentToken()));
			tokenizer.next();
			return c;
		} else {
			String name = tokenizer.currentToken(); // Attribute 이름 저장
			Variable v = variables.get(name); // Variable 선언하고 이름도 설정
			if (v == null) {
				
				v = new Variable(name);
				variables.put(v.name(), v);
			}
			tokenizer.next();
			return v;
		}
	}

}
