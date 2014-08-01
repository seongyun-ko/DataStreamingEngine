package stream.query.operator;

import java.util.ArrayList;
import java.util.TreeMap;

import stream.data.Tuple;
import stream.query.operator.aggregate.Aggregate;

/**
 * An AggregateOperator produces an aggregate value for each group.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class AggregateOperator extends Operator {

	/**
	 * The attributes that are aggregated.
	 */
	protected String[] inputAttributes;

	/**
	 * The attributes for storing the aggregation results.
	 */
	protected String[] outputAttributes;

	/**
	 * The grouping attributes.
	 */
	protected String[] groupingAttributes;

	/**
	 * The aggregate functions.
	 */
	protected ArrayList<Class<?>> aggregateFunctions;

	/**
	 * The window size.
	 */
	protected double windowSize;

	/**
	 * The step size.
	 */
	protected double stepSize;

	/**
	 * The windows.
	 */
	protected TreeMap<Double, Window> windows = new TreeMap<Double, Window>();

	/**
	 * A Window maintains summary values obtained from tuples that belong to a specific time window.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 */
	class Window {

		/**
		 * The Aggregate that this AggregateOperator manages.
		 */
		protected Aggregate<ArrayList<Object>, Object> aggregate = new Aggregate<ArrayList<Object>, Object>(
				aggregateFunctions);

		/**
		 * Updates this Window.
		 * 
		 * @param attributeValues
		 *            the attribute values.
		 * @param groupValues
		 *            the group values.
		 * @throws InstantiationException
		 *             if an AggregateFunction cannot be instantiated.
		 */
		public void update(ArrayList<Object> attributeValues, ArrayList<Object> groupValues)
				throws InstantiationException {
			aggregate.update(attributeValues, groupValues);
		}

		@Override
		public String toString() {
			return aggregate.toString();
		}

	}

	/**
	 * Constructs an AggregateOperator.
	 * 
	 * @param aggregationAttributes
	 *            the aggregation attributes to which aggregation is applied.
	 * @param groupingAttributes
	 *            the attributes by which grouping is done.
	 * @param windowSize
	 *            the window size.
	 * @param stepSize
	 *            the step size.
	 * @throws ClassNotFoundException
	 */
	public AggregateOperator(String[] aggregationAttributes, String[] groupingAttributes, String windowSize,
			String stepSize) throws ClassNotFoundException {
		this.outputAttributes = new String[aggregationAttributes.length];
		String[] aggregateFunctionNames = new String[aggregationAttributes.length];
		this.inputAttributes = new String[aggregationAttributes.length];
		int i = 0;
		for (String aggregateAttribute : aggregationAttributes) {
			String[] parts = aggregateAttribute.split(":");
			this.outputAttributes[i] = parts[0];
			aggregateFunctionNames[i] = parts[1];
			this.inputAttributes[i] = parts[2];
			i++;
		}
		this.aggregateFunctions = aggregateFunctions(aggregateFunctionNames);
		this.groupingAttributes = groupingAttributes;
		this.windowSize = Double.parseDouble(windowSize);
		this.stepSize = Double.parseDouble(stepSize);
	}

	/**
	 * Returns an aggregate function.
	 * 
	 * @param aggregationFunctionName
	 *            the name of the aggregate function.
	 * @return an AggregateFunction that matches the specified name.
	 * @throws ClassNotFoundException
	 */
	protected Class<?> aggregateFunction(String aggregationFunctionName) throws ClassNotFoundException {
		return Class.forName("stream.query.operator.aggregate." + aggregationFunctionName);
	}

	/**
	 * Returns an ArrayList of AggreateFunctions.
	 * 
	 * @param aggregateFunctionNames
	 *            the names of AggregateFunctions.
	 * 
	 * @return an ArrayList of AggregateFunctions.
	 * @throws ClassNotFoundException
	 *             if an AggregateFunction cannot be found.
	 */
	protected ArrayList<Class<?>> aggregateFunctions(String[] aggregateFunctionNames) throws ClassNotFoundException {
		ArrayList<Class<?>> summaries = new ArrayList<Class<?>>(aggregateFunctionNames.length);
		for (int i = 0; i < aggregateFunctionNames.length; i++) {
			summaries.add(aggregateFunction(aggregateFunctionNames[i]));
		}
		return summaries;
	}

	@Override
	public void process(int port, Tuple t) {
		// Problem 3
		
		ArrayList<Object> inputsForAggregateFunctions = t.values(new String[] { "Celsius" });
		// AggregateFunctions 에 쓰일 Attributes
		
		ArrayList<Object> group = t.values(new String[] { "Location" });
		// Location을 대상으로 Grouping한 후에, Celsius의 Avr과 Max를 구하고자 함
						
			double init =(double)(int)(t.timestamp() / this.stepSize) - (this.windowSize / this.stepSize -1 );
			Double k;

			while((k = windows.floorKey(init-1)) != null) {
				System.out.println("Key :" + k +" is removed");
				System.out.println(windows.get(k));
				windows.remove(k); 
	//			windows.get(k).aggregate.
			}
			
			while(t.timestamp() >= init*this.stepSize &&
					t.timestamp() < ( init*this.stepSize + this.windowSize)) {
				if(!windows.containsKey(init)) windows.put(init, new Window());
				try {
					System.out.println("windows.get(" + init + ") is updated");
					windows.get(init).update(inputsForAggregateFunctions, group);
				} catch (InstantiationException e) {
					e.printStackTrace();
				}				
				init ++;
			}
			System.out.println();
			
	}

}
