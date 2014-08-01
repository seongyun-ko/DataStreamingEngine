package stream.query.operator;

import java.util.Vector;

import stream.data.Tuple;
import stream.util.Pair;

/**
 * An Operator processes Tuples and produces Tuples.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public abstract class Operator {

	/**
	 * The Operators that consume data from this Operator.
	 */
	Vector<Pair<Operator, Integer>> consumers = new Vector<Pair<Operator, Integer>>();

	/**
	 * Processes the specified Tuple
	 * 
	 * @param port
	 *            the input port.
	 * @param t
	 *            the Tuple to process.
	 */
	public abstract void process(int port, Tuple t);

	/**
	 * Sends the specified Tuple to the Operators connected to the output of this Operator.
	 * 
	 * @param t
	 *            the Tuple to send.
	 */
	public void output(Tuple t) {
		for (Pair<Operator, Integer> p : consumers) {
			p.first().process(p.second(), t);
		}
	}

	/**
	 * Connects the specified port of the specified Operator to the output of this Operator.
	 * 
	 * @param o
	 *            an Operator.
	 * @param port
	 *            an input port.
	 */
	public void addConsumer(Operator o, int port) {
		consumers.add(new Pair<Operator, Integer>(o, port));
	}

}

