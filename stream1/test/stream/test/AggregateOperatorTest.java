package stream.test;

import stream.data.Tuple;
import stream.expression.ParsingException;
import stream.query.operator.AggregateOperator;
import stream.query.operator.PrintOperator;

public class AggregateOperatorTest {

	public static void main(String[] args) throws ParsingException, ClassNotFoundException {

		AggregateOperator o = new AggregateOperator(new String[] { "Average_Celsius:Average:Celsius" },
				new String[] { "Location" }, "2000", "1000");
		o.addConsumer(new PrintOperator(), 0);
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 0.0 }, 0.0));
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "B", 10.0 }, 0.0));
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 10.0 }, 1000.0));
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "B", 20.0 }, 1000.0));
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 20.0 }, 2000.0));
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "B", 30.0 }, 2000.0));
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 30.0 }, 3000.0));
		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "B", 40.0 }, 3000.0));
	
//		
//		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 10.0 }, 0.0));
//		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 20.0 }, 500.0));
//		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 30.0 }, 1000.0));
//		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 40.0 }, 1500.0));
//		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 50.0 }, 2000.0));
//		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 60.0 }, 2500.0));
//		o.process(0, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 70.0 }, 3000.0));

	}

}
