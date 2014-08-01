package stream.query.operator.aggregate;

/**
 * An AggregateFunction computes an aggregate value from values.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 * @param <V>
 *            The type of the input values.
 * @param <A>
 *            The type of the aggregate value.
 */
public interface AggregateFunction<V, A> {

	/**
	 * Updates this AggregateFunction's state based on the specified value.
	 * 
	 * @param v
	 *            the value for updating this AggregateFunction's state.
	 */
	public boolean update(V v);

	/**
	 * The aggregate value obtained from this AggregateFunction's state.
	 * 
	 * @return the aggregate value.
	 */
	public A aggregateValue();

}
