package stream.test;

import stream.data.Tuple;
import stream.expression.ParsingException;
import stream.query.operator.JoinOperator;
import stream.query.operator.PrintOperator;

public class JoinOperatorTest {

	public static void main(String[] args) throws ParsingException {
		JoinOperator o = new JoinOperator(new String[] { "Plate" }, new String[] { "Plate" }, 1000.0);
		o.addConsumer(new PrintOperator(), 0);
		o.process(0, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Pohang", "A" }, 0.0));
		o.process(0, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Pohang", "B" }, 0.0));
		o.process(0, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Pohang", "C" }, 0.0));
		o.process(0, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Pohang", "C" }, 1000.0));
		o.process(1, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Seoul", "A" }, 500.0));
		o.process(1, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Seoul", "B" }, 1000.0));
		o.process(1, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Seoul", "C" }, 1500.0));
		o.process(3, new Tuple(new String[] { "Location", "Plate" }, new Object[] { "Seoul", "C" }, 1500.0));
		System.out.println(o.getClass());
	//	o.display();
	}

}
