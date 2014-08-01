package stream.test;

import java.util.ArrayList;
import java.util.Map.Entry;

import stream.data.Tuple;
import stream.expression.ParsingException;
import stream.query.operator.aggregate.Aggregate;
import stream.query.operator.aggregate.AggregateFunction;
import stream.query.operator.aggregate.Average;
import stream.query.operator.aggregate.Maximum;

public class AggregateTest {

	public static void main(String[] args) throws ParsingException, ClassNotFoundException, InstantiationException {
		ArrayList<Class<?>> aggregateFunctions = new ArrayList<Class<?>>(2);
		aggregateFunctions.add(Average.class);
		aggregateFunctions.add(Maximum.class);
		System.out.println(aggregateFunctions);
		
		Aggregate<ArrayList<Object>, Object> aggregate = 
		   new Aggregate<ArrayList<Object>, Object>(aggregateFunctions);
		
		process(aggregate, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 0.0 }, 0.0));
		process(aggregate, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "B", 10.0 }, 0.0));
	
		process(aggregate, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "A", 10.0 }, 1000.0));
		process(aggregate, new Tuple(new String[] { "Location", "Celsius" }, new Object[] { "B", 20.0 }, 1000.0));
		System.out.println(aggregate);
	
		Entry<ArrayList<Object>, ArrayList<AggregateFunction<Object, ?>>> e;
		while ((e = aggregate.removeFirst()) != null) {
			System.out.println("group: " + e.getKey() + ", aggregate functions: " + e.getValue());
			ArrayList<AggregateFunction<Object, ?>> aFunctions = e.getValue();
			for (AggregateFunction<Object, ?> aFunction : aFunctions) {
				System.out.println(" " + aFunction.aggregateValue());
			}
			
		
		} 
  }

	private static void process(Aggregate<ArrayList<Object>, Object> aggregate, Tuple tuple)
			throws InstantiationException {
		
		ArrayList<Object> inputsForAggregateFunctions = tuple.values(new String[] { "Celsius", "Celsius" });
		// AggregateFunctions 에 쓰일 Attributes
		
		ArrayList<Object> group = tuple.values(new String[] { "Location" });
		// Location을 대상으로 Grouping한 후에, Celsius의 Avr과 Max를 구하고자 함
		aggregate.update(inputsForAggregateFunctions, group);
	}
}
