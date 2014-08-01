package stream.test;

import stream.util.system.Configuration;
import stream.util.system.Host;
import stream.util.system.Master;
import stream.util.system.MasterInterface;
import stream.util.system.Worker;
import stream.util.system.WorkerInterface;

/**
 * This SystemTest shows an example of using the hwanglab.system package.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class MasterWorkersTest {

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
		new Master(config, WorkerInterface.class);

		MasterInterface master = Host.lookup("127.0.0.1:10000", MasterInterface.class);

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
					new Worker(workerID, workerPort, masterAddress, MasterInterface.class);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}