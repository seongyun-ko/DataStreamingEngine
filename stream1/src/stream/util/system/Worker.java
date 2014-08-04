package stream.util.system;

import java.util.Map;
import java.util.TreeMap;

import stream.util.net.CommunicationException;
import stream.util.net.LookupException;

/**
 * A Worker performs tasks assigned by the Master.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Worker extends Host implements WorkerInterface {

	/**
	 * The ID of this Worker.
	 */
	protected int workerID;

	/**
	 * The Workers.
	 */
	protected TreeMap<Integer, WorkerInterface> workers = new TreeMap<Integer, WorkerInterface>();

	/**
	 * The Master.
	 */
	protected MasterInterface master;

	/**
	 * The addresses of the Workers.
	 */
	protected Map<Integer, String> workerAddresses;

	/**
	 * Constructs a Worker.
	 * 
	 * @param workerID
	 *            the ID of the Worker.
	 * @param port
	 *            the port number of the Worker.
	 * @param masterAddress
	 *            the address of the Master in the form of [IP address]:[port number].
	 * @param masterInterface
	 *            the interface the Master interface.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public Worker(int workerID, int port, String masterAddress,
			Class<? extends MasterInterface> masterInterface) throws Exception {
		super(port);
		this.workerID = workerID;
		this.master = Host.lookup(masterAddress, masterInterface);
		master.registerWorker(workerID, address);
		// hey master, register me!
	}

	/**
	 * Returns the ID of this Worker.
	 * 
	 * @return the ID of this Worker.
	 */
	public int workerID() {
		return this.workerID;
	}

	@Override
	public synchronized void shutdown() {
		super.shutdown();
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + workerID + ")";
	}

	@Override
	public void introduceWorkers(Map<Integer, String> workerAddresses) {
		this.workerAddresses = workerAddresses;
	}

	/**
	 * Registers a Worker.
	 * 
	 * @param workerID
	 *            the ID of the Worker.
	 * @param workerAddress
	 *            the address of the Worker.
	 * @return a Worker.
	 * @throws LookupException
	 *             if the Worker cannot be looked up.
	 * @throws CommunicationException
	 *             if the communication fails.
	 */
	protected WorkerInterface registerWorker(Integer workerID, String workerAddress) throws LookupException,
			CommunicationException {
		WorkerInterface w = null;
		Class<?> workerInterface = getClass().getInterfaces()[0];
		if (workerID == this.workerID)
			w = this;
		else
			w = (WorkerInterface) Host.lookup(workerAddress, workerInterface);
		workers.put(workerID, w);
		return w;
	}

	/**
	 * Returns the specified Worker.
	 * 
	 * @param workerID
	 *            the ID of the Worker.
	 * @return the specified Worker.
	 * @throws LookupException
	 *             if the Worker cannot be looked up.
	 * @throws CommunicationException
	 *             if the communication fails.
	 */
	protected WorkerInterface worker(int workerID) throws LookupException, CommunicationException {
		WorkerInterface worker = workers.get(workerID);
		if (worker != null)
			return worker;
		else
			return registerWorker(workerID, workerAddresses.get(workerID));
	}

}
