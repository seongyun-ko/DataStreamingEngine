package stream.test;

import stream.data.Tuple;
import stream.expression.ParsingException;
import stream.query.operator.PrintOperator;
import stream.query.operator.ProjectionOperator;

public class ProjectionOperatorTest {

	public static void main(String[] args) throws ParsingException {
		ProjectionOperator o = new ProjectionOperator(new String[] { "Fahrenheit:Celsius * 9 / 5 + 32" });
//		ProjectionOperator o = new ProjectionOperator(new String[] { "Celsius * 9 / 5 + 32" });
		o.addConsumer(new PrintOperator(), 0);
		o.process(0, new Tuple(new String[] { "Celsius" }, new Object[] { 0.0 }, 0.0));
		o.process(0, new Tuple(new String[] { "Celsius" }, new Object[] { 10.0 }, 1000.0));
		o.process(0, new Tuple(new String[] { "Celsius" }, new Object[] { 20.0 }, 2000.0));
	//	o.print();
	}

}
