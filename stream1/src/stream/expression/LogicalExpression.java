package stream.expression;


/**
 * An ArithmeticExpression represents an arithmetic expression.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class LogicalExpression extends Expression {

	/**
	 * Constructs an ArithmeticExpression.
	 * 
	 * @param expression
	 *            a string representing an algebraic expression.
	 * @throws ParsingException
	 *             if a parsing error occurs.
	 */
	public LogicalExpression(String expression) throws ParsingException {
		StringTokenizer tokenizer = new StringTokenizer(expression,
				plus + minus + times + slash + lparen + rparen + eq, '\"', '#');
//		while(tokenizer.hasNext())
//			System.out.println(tokenizer.next());
		tokenizer.next(); // next�� �ѹ� ���ִ� ������, �� ������ ���� �Ŀ� \ �� ����ġ�� ���ؼ�.
		// Delimiters�� Token�� ���ԵǾ� ����
		root = condition(tokenizer);
	}

}
