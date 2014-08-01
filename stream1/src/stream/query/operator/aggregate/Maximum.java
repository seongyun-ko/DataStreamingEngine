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
			return true; // Generic�̹Ƿ� �񱳿� ���� ����� �޼��带 ����� ��ƴ�.
						// ���� Comparable�� Casting�� �Ŀ� compareTo method�� ���
		}
		return false;
	}

	@Override
	public V aggregateValue() {
		return maximum;
	}

}