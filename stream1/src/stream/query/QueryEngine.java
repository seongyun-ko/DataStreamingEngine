package stream.query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.TreeMap;

import stream.data.Tuple;
import stream.query.operator.Operator;
import stream.query.operator.ProjectionOperator;


/**
 * A QueryEngine executes Operators for processing data.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class QueryEngine {

	/**
	 * The Operators that this QueryEngine manages.
	 */
	TreeMap<String, Operator> operators = new TreeMap<String, Operator>();

	/**
	 * Connects the output of a specified Operator to the input port of another specified Operator.
	 * 
	 * @param producerName
	 *            the name of the Operator whose output will be connected.
	 * @param consumerName
	 *            the name of the Operator whose input port will be connected.
	 * @throws NoOperatorException
	 *             if this QueryEngine does not have one of the specified Operators.
	 */
	public void connect(String producerName, String consumerName, int port) throws NoOperatorException {
		// Problem 1
		Operator producer_operator = operator(producerName);
		Operator consumer_operator = operator(consumerName);
		if( producer_operator == null || consumer_operator == null) throw new NoOperatorException();
		else operator(producerName).addConsumer(operator(consumerName), port);
	}

	/**
	 * Constructs an Operator according to the specified OperatorDefinition.
	 * 
	 * @param definition
	 *            the definition of the Operator to create.
	 * @param name
	 *            the name of the Operator to create.
	 * @throws InstantiationException
	 *             if cannot construct an Operator.
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void createOperator(String name, OperatorDefinition definition) throws InstantiationException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Problem 2.
		Class<?> c = null;
		try {
			c = Class.forName("stream.query.operator." + definition.type());
		} catch(ClassNotFoundException e ) {
			throw new InstantiationException("Cannot construct a(n) " + definition);
		}

		Constructor<?>[] constructors = c.getDeclaredConstructors();
		Operator obj = null;
		
		System.out.println("constructors: " + Arrays.toString(constructors));

		for (int i = 0; i < constructors.length; i++) {
			Constructor<?> constructor = constructors[i];

			try {
				obj = (Operator) constructor.newInstance(definition.arguments());
				if(obj != null) break;
			} catch (InstantiationException ex) {
				 System.out.println("Cannot instantiate the operator" + constructor.toString());
			}
		}

		if(obj != null) operators.put(name, obj);

	}

	/**
	 * Shuts down this QueryEngine.
	 */
	public void shutdown() {
	}

	/**
	 * Returns the specified Operator.
	 * 
	 * @param name
	 *            the name of the Operator.
	 * @return the specified Operator.
	 * @throws NoOperatorException
	 *             if this QueryEngine does not have the specified Operator.
	 */
	public Operator operator(String name) throws NoOperatorException {
		Operator o = operators.get(name);
		if (o == null)
			throw new NoOperatorException();
		return o;
	}

	/**
	 * A NoOperatorException is thrown if an Operator cannot be found.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 */
	public static class NoOperatorException extends Exception {

		/**
		 * Automatically generated serial version UID.
		 */
		private static final long serialVersionUID = -5178832249986108645L;

	}

	/**
	 * Processes the specified Tuples using the specified Operator.
	 * 
	 * @param name
	 *            the name of the Operator.
	 * @param port
	 *            the input port of the Operator.
	 * @param tuples
	 *            the tuples to process.
	 * @throws NoOperatorException
	 *             if this QueryEngine does not have the specified Operator.
	 */
	public void process(String name, int port, Iterable<Tuple> tuples) throws NoOperatorException {
		Operator o = operator(name);
		for (Tuple t : tuples) {
			o.process(port, t);
		}
	}

}
