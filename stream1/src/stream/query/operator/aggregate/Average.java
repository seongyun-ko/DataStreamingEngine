package stream.query.operator.aggregate;

/**
 * An Average obtains the average of given numbers.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Average extends Sum {

	/**
	 * The count.
	 */
	int count = 0;

	/**
	 * Constructs an Average.
	 */
	public Average() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(sum: " + super.sum + ", count: " + count + ")";
	}

	@Override
	public boolean update(Number v) {
		count++;
		return super.update(v);
	}

	@Override
	public Number aggregateValue() {
		if (sum instanceof Integer)
			return ((Integer) sum) / count;
		else if (sum instanceof Double)
			return ((Double) sum) / count;
		else
			throw new UnsupportedOperationException();
	}

}