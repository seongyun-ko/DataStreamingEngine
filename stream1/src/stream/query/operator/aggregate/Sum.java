package stream.query.operator.aggregate;

/**
 * A Sum maintains a sum.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Sum implements AggregateFunction<Number, Number> {

	/**
	 * The sum maintained by this Sum.
	 */
	Number sum = 0;

	/**
	 * Constructs a Sum.
	 */
	public Sum() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + sum + ")";
	}

	@Override
	public boolean update(Number v) {
		if (v instanceof Integer)
			sum = ((Integer) v).intValue() + sum.intValue();
		else if (v instanceof Double)
			sum = ((Double) v).doubleValue() + sum.doubleValue();
		else
			throw new UnsupportedOperationException();
		return true;
	}

	@Override
	public Number aggregateValue() {
		return sum;
	}

}