package stream.query.operator.aggregate;

/**
 * A Minimum maintains a minimum.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Minimum<V> implements AggregateFunction<V, V> {

	/**
	 * The current minimum value.
	 */
	protected V minimum = null;

	/**
	 * Constructs a Minimum.
	 */
	public Minimum() {
	}

	/**
	 * Constructs a Minimum based on the specified value.
	 * 
	 * @param v
	 *            the initial value.
	 */
	public Minimum(V v) {
		this.minimum = v;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + minimum + ")";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update(V v) {
		if (v == null || ((Comparable<V>) minimum).compareTo(v) > 0) {
			minimum = v;
			return true;
		} else
			return false;
	}

	@Override
	public V aggregateValue() {
		return minimum;
	}

}
