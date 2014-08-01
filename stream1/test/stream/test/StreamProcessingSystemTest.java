package stream.test;

import java.util.Vector;

import stream.util.system.Configuration;
import stream.util.system.Host;
import stream.Master;
import stream.MasterInterface;
import stream.Worker;
import stream.data.Tuple;
import stream.WorkerInterface;

/**
 * This SystemTest shows an example of using the hwanglab.system package.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class StreamProcessingSystemTest {

	/**
	 * The main program.
	 * 
	 * @param args
	 *            the arguments.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration();
		config.update("-master-port", "10000");
		config.update("-workers", "2");

		createWorker(0, 20000, "127.0.0.1:10000");
		createWorker(1, 20001, "127.0.0.1:10000");
		new Master(config);

		MasterInterface master = Host.lookup("127.0.0.1:10000", MasterInterface.class);
		master.createOperator("projection@0", "ProjectionOperator([Fahrenheit:Celsius * 9 / 5 + 32])");
		master.createOperator("print@1", "PrintOperator() [projection@0]");

		WorkerInterface worker0 = Host.lookup("127.0.0.1:20000", WorkerInterface.class);

		Vector<Tuple> tuples = new Vector<Tuple>();
		tuples.add(new Tuple(new String[] { "Celsius" }, new Object[] { 0.0 }, 0.0));
		tuples.add(new Tuple(new String[] { "Celsius" }, new Object[] { 10.0 }, 1000.0));
		tuples.add(new Tuple(new String[] { "Celsius" }, new Object[] { 20.0 }, 2000.0));

		worker0.process("projection", 0, tuples);

		Thread.sleep(100);

		master.shutdown();
	}

	/**
	 * Constructs a Worker.
	 * 
	 * @param workerID
	 *            the ID of the Worker.
	 * @param workerPort
	 *            the port of the Worker.
	 * @param masterAddress
	 *            the address of the Master in the form of [IP address]:[port number].
	 */
	protected static void createWorker(final int workerID, final int workerPort, final String masterAddress) {
		new Thread() {
			public void run() {
				try {
					new Worker(workerID, workerPort, masterAddress);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
