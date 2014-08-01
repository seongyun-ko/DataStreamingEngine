package stream.query.operator;

import stream.data.Tuple;

/**
 * A UnionOperator merges multiple input streams into an output stream.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class UnionOperator extends Operator {

	/**
	 * Constructs a UnionOperator.
	 */
	public UnionOperator() {
	}

	@Override
	public void process(int port, Tuple t) {
		output(t);
	}

}
