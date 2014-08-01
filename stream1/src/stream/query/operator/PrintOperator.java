package stream.query.operator;

import stream.data.Tuple;

/**
 * A PrintOperator displays the received Tuples using the standard output stream.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class PrintOperator extends Operator {

	/**
	 * Constructs a PrintOperator.
	 */
	public PrintOperator() {
	}

	@Override
	public void process(int port, Tuple t) {
		System.out.println(t);
	}
}
