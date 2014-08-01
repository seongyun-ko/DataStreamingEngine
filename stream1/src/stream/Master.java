package stream;

import java.util.Iterator;
import java.util.Vector;

import stream.query.OperatorDefinition;
import stream.query.OperatorID;
import stream.util.system.Configuration;

/**
 * A Master manages the whole stream processing system.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class Master extends stream.util.system.Master implements MasterInterface {

	/**
	 * Constructs a Master.
	 * 
	 * @param configuration
	 *            the system configuration.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public Master(Configuration configuration) throws Exception {
		super(configuration, WorkerInterface.class);
		new Thread() {
			public void run() {
				synchronized (Master.this) {
					try {
						Master.this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public void createOperator(String operatorID, String definition) throws Exception {
		String[] parts = operatorID.split("@");
		OperatorID oID = new OperatorID(parts[0], Integer.parseInt(parts[1]));
		stream.query.OperatorDefinition.StringTokenizer tokenizer = new OperatorDefinition.StringTokenizer(definition);
		OperatorDefinition d = new OperatorDefinition(tokenizer);
		Vector<OperatorID> inputOperators = new Vector<OperatorID>();
		if (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken(); // [
			while (true) {
				String token = tokenizer.nextToken();
				if (token.equals(","))
					token = tokenizer.nextToken();
				if (token.equals("]"))
					break;
				String operatorName = token;
				tokenizer.nextToken(); // @
				inputOperators.add(new OperatorID(operatorName, Integer.parseInt(tokenizer.nextToken())));
			}
		}
		worker(oID.workerID()).createOperator(oID.operatorName(), d, inputOperators);
		System.out.println(this + " has created " + d + " " + inputOperators + " at worker " + oID.workerID() + ".");
	}

	/**
	 * Returns the specified Worker.
	 * 
	 * @param workerID
	 *            the ID of the Worker.
	 * @return the specified Worker.
	 * @throws InvalidWorkerIDException
	 *             if an invalid worker ID is given.
	 */
	public WorkerInterface worker(int workerID) throws InvalidWorkerIDException {
		WorkerInterface worker = (WorkerInterface) workers.get(workerID);
		if (worker == null)
			throw new InvalidWorkerIDException(workerID);
		else
			return worker;
	}

	/**
	 * An InvalidWorkerIDException is thrown if an invalid worker ID is given.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 * 
	 */
	public static class InvalidWorkerIDException extends Exception {

		/**
		 * Automatically generated serial version UID.
		 */
		private static final long serialVersionUID = -6040469951475860012L;

		/**
		 * Constructs an InvalidWorkerIDException.
		 * 
		 * @param workerID
		 */
		public InvalidWorkerIDException(int workerID) {
			super(workerID + ": invalid worker ID");
		}

	}

	/**
	 * Returns the IDs of the Workers.
	 * 
	 * @return the IDs of the Workers.
	 */
	public int[] workerIDs() {
		int[] workerIDs = new int[workers.size()];
		Iterator<Integer> iter = workers.keySet().iterator();
		for (int i = 0; i < workerIDs.length; i++)
			workerIDs[i] = iter.next();
		return workerIDs;
	}

	/**
	 * Returns the specified Worker IDs.
	 * 
	 * @param workerIDs
	 *            the IDs of the Workers.
	 * @return the specified Worker IDs.
	 */
	protected int[] workerIDs(String workerIDs) {
		if (workerIDs == null)
			return workerIDs(workers.size());
		String[] tokens = workerIDs.split("-");
		if (tokens.length == 1) {
			return new int[] { Integer.parseInt(tokens[0]) };
		} else if (tokens.length == 2) {
			int start = Integer.parseInt(tokens[0]);
			int end = Integer.parseInt(tokens[1]);
			int[] ids = new int[end - start + 1];
			for (int i = start; i <= end; i++) {
				try {
					ids[i - start] = i;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return ids;
		} else
			throw new UnsupportedOperationException();
	}

	/**
	 * Returns the specified Worker IDs.
	 * 
	 * @param workers
	 *            the number of Workers.
	 * @return the specified Worker IDs.
	 */
	protected int[] workerIDs(int workers) {
		int[] workerIDs = new int[workers];
		for (int i = 0; i < workers; i++)
			workerIDs[i] = i;
		return workerIDs;
	}

	@Override
	public synchronized void shutdown() {
		super.shutdown();
		notify();
	}

}