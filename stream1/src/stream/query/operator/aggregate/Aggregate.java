package stream.query.operator.aggregate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;

/**
 * An Aggregate maintains a collection of AggregateFunctions for each group.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 * @param <G>
 *            The type of the grouping values.
 * @param <I>
 *            the type of the input values.
 */
public class Aggregate<G, I> {

	/**
	 * A map between groups and summaries.
	 */
	Map<G, ArrayList<AggregateFunction<I, ?>>> group2aggregateFunctions = new HashMap<G, ArrayList<AggregateFunction<I, ?>>>();

	/**
	 * The initial summary.
	 */
	ArrayList<Class<?>> summaries;

	/**
	 * Constructs an Aggregate.
	 * 
	 * @param aggregateFunctions
	 *            the AggregateFunctions to maintain.
	 */
	public Aggregate(ArrayList<Class<?>> aggregateFunctions) {
		this.summaries = aggregateFunctions;
	}

	/**
	 * Removes the first entry from this Aggregate.
	 * 
	 * @return the removed entry; null if no such entry existed.
	 */
	public Entry<G, ArrayList<AggregateFunction<I, ?>>> removeFirst() {
		Iterator<Entry<G, ArrayList<AggregateFunction<I, ?>>>> i = group2aggregateFunctions.entrySet().iterator();
		if (!i.hasNext())
			return null;
		Entry<G, ArrayList<AggregateFunction<I, ?>>> next = i.next();
		if (next != null)
			group2aggregateFunctions.remove(next.getKey());
		return next;
	}

	/**
	 * Updates this Aggregate based on the specified values and groups.
	 * 
	 * @param values
	 *            input values.
	 * @param group
	 *            the group value.
	 * @throws InstantiationException
	 *             if an AggregateFunction cannot be instantiated.
	 */
	public void update(ArrayList<I> values, G group) throws InstantiationException {
		ArrayList<AggregateFunction<I, ?>> summaries = group2aggregateFunctions.get(group);
		if (summaries == null) {
			summaries = newSummaries(this.summaries, values);
			group2aggregateFunctions.put(group, summaries);
		} else {
			int i = 0;
			for (AggregateFunction<I, ?> summary : summaries) {
				summary.update(values.get(i));
		//		System.out.println(summary.toString() + " " +  values.get(i));
			}

		}
	}

	@Override
	public String toString() {
		return group2aggregateFunctions.toString();
	}
	
	/**
	 * Constructs a new ArrayList of AggregateFunctions using the specified AggregateFunctions and values.
	 * 
	 * @param aggregateFunctions
	 *            an ArrayList of AggregateFunctions.
	 * @param values
	 *            an array of values.
	 * @return a new ArrayList of AggregateFunctions
	 * @throws InstantiationException
	 *             if an AggregateFunction cannot be instantiated.
	 */
	@SuppressWarnings("unchecked")
	protected ArrayList<AggregateFunction<I, ?>> newSummaries(ArrayList<Class<?>> aggregateFunctions,
			ArrayList<I> values) throws InstantiationException {
		ArrayList<AggregateFunction<I, ?>> s = new ArrayList<AggregateFunction<I, ?>>(values.size());
		for (int i = 0; i < values.size(); i++) {
			try {
				s.add(i, (AggregateFunction<I, ?>) aggregateFunctions.get(i).getConstructor().newInstance());
			} catch (Exception e) {
				throw new InstantiationException();
			}
			s.get(i).update(values.get(i));
		}
		return s;
	}

}
