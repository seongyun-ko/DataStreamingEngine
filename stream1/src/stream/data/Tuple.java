package stream.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import stream.expression.Expression;
import stream.expression.UnboundVariableException;
import stream.expression.Variable;

/**
 * A Tuple represents an object that can contain an arbitrary number of attributes.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Tuple implements java.io.Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 113272530279206273L;

	/**
	 * The attributes.
	 */
	protected LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>(0);

	/**
	 * Constructs a Tuple.
	 * 
	 * @param timestamp
	 *            the timestamp.
	 */
	public Tuple(double timestamp) {
		attributes.put("timestamp", timestamp);
	}

	/**
	 * Constructs a Tuple as a clone of the specified Tuple.
	 * 
	 * @param other
	 *            another Tuple.
	 */
	public Tuple(Tuple other) {
		attributes.put("timestamp", other.timestamp());
		for (Map.Entry<String, Object> e : other.attributes.entrySet()) {
			update(e.getKey(), e.getValue());
		}
	}

	/**
	 * Constructs a Tuple containing the specified attributes and values.
	 * 
	 * @param attributes
	 *            the attributes.
	 * @param values
	 *            the values.
	 * @param timestamp
	 *            the timestamp.
	 */
	public Tuple(String[] attributes, Object[] values, double timestamp) {
		this.attributes.put("timestamp", timestamp);
		for (int i = 0; i < attributes.length; i++) {
			update(attributes[i], values[i]);
		}
	}

	/**
	 * Constructs a Tuple by concatenating two specified Tuples.
	 * 
	 * @param t1
	 *            a Tuple.
	 * @param t2
	 *            a Tuple.
	 */
	public Tuple(Tuple t1, Tuple t2) {
		attributes.put("timestamp", Math.max(t1.timestamp(), t2.timestamp()));
		for (Map.Entry<String, Object> e : t1.attributes.entrySet()) {
			update("left." + e.getKey(), e.getValue());
		}
		for (Map.Entry<String, Object> e : t2.attributes.entrySet()) {
			update("right." + e.getKey(), e.getValue());
		}
	}

	/**
	 * Updates this Tuple based on the specified attribute.
	 * 
	 * @param attributeName
	 *            the name of the attribute.
	 * @param attributeValue
	 *            the new value of the attribute; if null the specified attribute is removed from this Tuple.
	 * @return the previous value of the specified attribute; null if none.
	 */
	public Object update(String attributeName, Object attributeValue) {
		if (attributeValue == null)
			return attributes.remove(attributeName);
		else
			return attributes.put(attributeName, attributeValue);
	}

	/**
	 * Returns the value of the specified attribute.
	 * 
	 * @param attribute
	 *            the name of the attribute.
	 * @return the value of the specified attribute.
	 */
	public Object value(String attribute) {
		return attributes.get(attribute);
	}

	/**
	 * Removes the specified attribute from this Tuple.
	 * 
	 * @param attribute
	 *            the name of the attribute to remove.
	 * @return the old value of the removed attribute.
	 */
	public Object remove(String attribute) {
		return attributes.remove(attribute);
	}

	/**
	 * Returns the attribute names.
	 * 
	 * @return the attribute names.
	 */
	public Collection<? extends String> attributeNames() {
		return attributes.keySet();
	}

	/**
	 * Returns the attribute values.
	 * 
	 * @return the attribute values.
	 */
	public Collection<Object> values() {
		return attributes.values();
	}

	/**
	 * Returns the timestamp of this Tuple.
	 * 
	 * @return the timestamp of this Tuple.
	 */
	public double timestamp() {
		return (Double) attributes.get("timestamp");
	}

	@Override
	public String toString() {
		return attributes.toString();
	}

	/**
	 * Evaluates the specified ArithmeticExpression using the attribute values of this Tuple.
	 * 
	 * @param expression
	 *            an ArithmeticExpression.
	 * @return the result of evaluating the specified ArithmeticExpression.
	 * @throws UnboundVariableException
	 *             if the ArithmeticExpression contains a variable whose value is not set.
	 */
	public Object evaluate(Expression expression) throws UnboundVariableException {
		for (Variable v : expression.variables()) {
			v.set(value(v.name()));
		}
		return expression.evaluate();
	}

	/**
	 * Returns a collection of values of the specified attributes.
	 * 
	 * @param attributes
	 *            the attributes whose values will be retrieved.
	 * @return a collection of values of the specified attributes.
	 */
	public ArrayList<Object> values(String[] attributes) {
		ArrayList<Object> values = new ArrayList<Object>();
		for (String attribute : attributes)
			values.add(value(attribute));
		return values;
	}

}
