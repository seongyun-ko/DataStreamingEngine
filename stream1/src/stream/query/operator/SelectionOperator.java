package stream.query.operator;

import stream.data.Tuple;
import stream.expression.LogicalExpression;
import stream.expression.ParsingException;
import stream.expression.UnboundVariableException;

/**
 * A SelectionOperator outputs the Tuples that satisfy a specified predicate.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class SelectionOperator extends Operator {

	/**
	 * The predicate of this SelectionOperator.
	 */
	protected LogicalExpression predicate;

	/**
	 * Constructs a SelectionOperator.
	 * 
	 * @param predicate
	 *            the predicate for the SelectionOperator.
	 * @throws ParsingException
	 *             if an error occurs while parsing the expressions.
	 */
	public SelectionOperator(String predicate) throws ParsingException {
		this.predicate = new LogicalExpression(predicate);
	}

	@Override
	public void process(int port, Tuple t) {
		try {
			//output(t);
			Object result = t.evaluate(predicate);
		//	System.out.println(result);
			if (result.equals(Boolean.TRUE))
				output(t);
		} catch (UnboundVariableException e) {
			e.printStackTrace();
		}
	}
}
