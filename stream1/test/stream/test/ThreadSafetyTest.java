package stream.test;

import stream.data.Tuple;
import stream.expression.ParsingException;
import stream.query.operator.JoinOperator;
import stream.query.operator.Operator;

public class ThreadSafetyTest {

	public static void main(String[] args) throws ParsingException {
		JoinOperator o = new JoinOperator(new String[] { "Plate" }, new String[] { "Plate" }, 1000);
		new DataGenerator(o, 0, "Pohang").start();
		new DataGenerator(o, 1, "Seoul").start();
	}

	static class DataGenerator extends Thread {

		/**
		 * The Operator to consume data.
		 */
		protected Operator operator;

		/**
		 * The port of the Operator.
		 */
		protected int port;

		/**
		 * The location.
		 */
		protected String location;

		/**
		 * Constructs a DataGenerator.
		 * 
		 * @param operator
		 *            an Operator.
		 * @param port
		 *            an input port.
		 * @param location
		 *            a location.
		 */
		public DataGenerator(Operator operator, int port, String location) {
			this.operator = operator;
			this.port = port;
			this.location = location;
		}

		@Override
		public void run() {
		//	System.out.println("Start!");
			for (int i = 0; i < 10; i++) {
				synchronized(operator) {
					try {
						operator.notify();
			//			System.out.println("I am waiting!");
						operator.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						operator.process(port, new Tuple(new String[] { "Location", "Plate" },
								new Object[] { location, "" + i }, i));
			//			System.out.println("What's going on here?");
					}
				}
			}
		}

	}

}
