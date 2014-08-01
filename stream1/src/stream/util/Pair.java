package stream.util;

/**
 * A Pair contains two values.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * @param <A>
 *            the type of the first value.
 * @param <B>
 *            the type of the second value.
 */
//public class Pair<A extends Comparable<A>, B extends Comparable<B>> implements java.io.Serializable, Comparable<Pair<A,B>> {
// 함수의 return 값이 여러개의 경우, 예를 들어 벡터화 해서 return 할 수있다. pair로

public class Pair<A, B> implements java.io.Serializable {

	/**
	 * An automatically generated serial version ID.
	 */
	private static final long serialVersionUID = -1943375464508742009L;

	/**
	 * The first value.
	 */
	protected A first;

	/**
	 * The second value.
	 */
	protected B second;

	/**
	 * Constructs a Pair.
	 * 
	 * @param first
	 *            the first value.
	 * @param second
	 *            the second value.
	 */
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Returns the first value.
	 * 
	 * @return the first value.
	 */
	public A first() {
		return first;
	}

	/**
	 * Returns the second value.
	 * 
	 * @return the second value.
	 */
	public B second() {
		return second;
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}
	
/*	public static void main(String[] args) {
		Pair<Integer, String> p1 = new Pair<Integer, String>(1,"2");
		Pair<Integer, String> p2 = new Pair<Integer, String>(1,"3");
		System.out.println(p1.compareTo(p2));
	}

	@Override
	public int compareTo(Pair<A, B> o) {
		// TODO Auto-generated method stub
		int c = first.compareTo(o.first);
		if (c != 0)
			return c;
		return second.compareTo(o.second);
	}*/

}
