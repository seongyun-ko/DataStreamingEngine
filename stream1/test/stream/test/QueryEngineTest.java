package stream.test;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import stream.data.Tuple;
import stream.query.OperatorDefinition;
import stream.query.OperatorDefinition.ParsingException;
import stream.query.QueryEngine;
import stream.query.QueryEngine.NoOperatorException;

public class QueryEngineTest {

	public static void main(String[] args) throws InstantiationException, ParsingException, NoOperatorException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		QueryEngine engine = new QueryEngine();

		// ask the engine to create a ProjectionOperator and label it as "projection"
		engine.createOperator("projection", new OperatorDefinition("ProjectionOperator([Fahrenheit:Celsius * 9 / 5 + 32])"));
		// ask the engine to create a PrintOperator and label it as "print"
		engine.createOperator("print", new OperatorDefinition("PrintOperator()"));
		// ask the engine to connect the output of "projection" to the input port 0 of "print".
		engine.connect("projection", "print", 0);

		Vector<Tuple> tuples = new Vector<Tuple>();
		tuples.add(new Tuple(new String[] { "Celsius" }, new Object[] { 0.0 }, 0.0));
		tuples.add(new Tuple(new String[] { "Celsius" }, new Object[] { 10.0 }, 1000.0));
		tuples.add(new Tuple(new String[] { "Celsius" }, new Object[] { 20.0 }, 2000.0));
	
		// ask the engine to pass the above three tuples to "projection" via input port 0
		engine.process("projection", 0, tuples);

		try { // operator creation failure due to lack of operator implementation
			engine.createOperator("strange", new OperatorDefinition("StrangeOperator()"));
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}
}
