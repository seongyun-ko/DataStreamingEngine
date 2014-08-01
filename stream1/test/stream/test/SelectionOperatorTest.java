package stream.test;

import stream.data.Tuple;
import stream.expression.ParsingException;
import stream.query.operator.PrintOperator;
import stream.query.operator.SelectionOperator;

public class SelectionOperatorTest {

	public static void main(String[] args) throws ParsingException {
		//SelectionOperator o = new SelectionOperator("Celsius > 0");
		SelectionOperator o = new SelectionOperator("Celsius <= 0");
		o.addConsumer(new PrintOperator(), 0);
		o.process(0, new Tuple(new String[] { "Celsius" }, new Object[] { 0.0 }, 0.0));
		o.process(0, new Tuple(new String[] { "Celsius" }, new Object[] { 10.0 }, 1000.0));
		o.process(0, new Tuple(new String[] { "Celsius" }, new Object[] { 20.0 }, 2000.0));
	
	}

}
