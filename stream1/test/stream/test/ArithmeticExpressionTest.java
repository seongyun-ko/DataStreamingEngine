package stream.test;

import stream.data.Tuple;
import stream.expression.ArithmeticExpression;
import stream.expression.ParsingException;
import stream.expression.UnboundVariableException;

public class ArithmeticExpressionTest {

	public static void main(String[] args) throws ParsingException, UnboundVariableException {
		String s = "Celsius * 9 / 5 + 32";
		ArithmeticExpression expression = new ArithmeticExpression(s);
		System.out.println("arithmetic expression: " + s);
		Tuple tuple = new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 0.0 }, 20.0);
		System.out.println("tuple: " + tuple);
		System.out.println("evaluation result: " + tuple.evaluate(expression));
		
		String[] test = new String[] { "Fahrenheit:Celsius * 9 / 5 + 32" };
		System.out.println(test[0]);
		
		//System.out.println(tuple(3));
	}

}
