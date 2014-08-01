package stream.query.operator.aggregate;

/**
 * A Count maintains a count.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Count implements AggregateFunction<Object, Integer> {

	/**
	 * The count managed by this Count.
	 */
	protected int count = 0;

	/**
	 * Constructs a Count.
	 */
	public Count() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + count + ")";
	}

	@Override
	public boolean update(Object v) {
		count++;
		return true;
	}

	@Override
	public Integer aggregateValue() {
		return count;
	}

}