package stream.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A MultiValuedHashMap associates each key with a set of values.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * @param <K>
 *            the type of keys.
 * @param <V>
 *            the type of values.
 */
public class MultiValuedHashMap<K, V> extends HashMap<K, Set<V>> {

	/**
	 * An automatically generated serial version ID.
	 */
	private static final long serialVersionUID = 8198293194500909080L;

	/**
	 * Adds the specified key and value.
	 * 
	 * @param k
	 *            the key to add.
	 * @param v
	 *            the value to add.
	 */
	public void add(K k, V v) {
		getSet(k).add(v);
	}

	/**
	 * Adds the specified key and value.
	 * 
	 * @param k
	 *            the key to add.
	 * @param v
	 *            the values to add.
	 */
	public void add(K k, Set<V> v) {
		getSet(k).addAll(v);
	}

	/**
	 * Removes the specified key and value.
	 * 
	 * @param k
	 *            the key to remove.
	 * @param v
	 *            the value to remove.
	 */
	public void remove(K k, V v) {
		Set<V> s = get(k);
		if (s != null) {
			s.remove(v);
			if (s.isEmpty())
				remove(k);
		}
	}

	/**
	 * Returns the set of values for the specified key.
	 * 
	 * @param k
	 *            a key.
	 * @return the set of values for the specified key.
	 */
	protected Set<V> getSet(K k) {
		Set<V> s = get(k);
		if (s == null) {
			s = new HashSet<V>();
			put(k, s);
		} // 해당 key에 해당하는 set이 없을 경우 null 을 return하는 대신에 empty set을 return
		return s;
	}

}
