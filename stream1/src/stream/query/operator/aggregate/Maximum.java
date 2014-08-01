package stream.query.operator.aggregate;

/**
 * A Maximum maintains a maximum.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Maximum<V> implements AggregateFunction<V, V> {

	/**
	 * The current maximum value.
	 */
	protected V maximum = null;

	/**
	 * Constructs a Maximum.
	 */
	public Maximum() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + maximum + ")";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update(V v) {
		if (maximum == null || ((Comparable<V>) maximum).compareTo(v) < 0) {
			maximum = v;
			return true; // Generic이므로 비교에 대한 공통된 메서드를 만들기 어렵다.
						// 따라서 Comparable로 Casting한 후에 compareTo method를 사용
		}
		return false;
	}

	@Override
	public V aggregateValue() {
		return maximum;
	}

}