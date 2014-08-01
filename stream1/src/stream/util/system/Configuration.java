package stream.util.system;

import stream.util.StringArrayIterator;

/**
 * A Configuration represents a configuration for a system.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Configuration extends stream.util.Configuration {

	/**
	 * Automatically generated serial version UID.
	 */
	private static final long serialVersionUID = 9049650139842233462L;

	/**
	 * The port number used for the Master.
	 */
	protected int masterPort = 10000;

	/**
	 * The number of workers to use.
	 */
	protected int numberWorkers = 4;

	/**
	 * Returns the port number used for the Master.
	 * 
	 * @return the port number used for the Master.
	 */
	public int masterPort() {
		return masterPort;
	}

	/**
	 * Returns the number of workers.
	 * 
	 * @return the number of workers.
	 */
	public int numberWorkers() {
		return numberWorkers;
	}

	@Override
	protected void update(String argument, StringArrayIterator i) throws ParsingException {
		if (argument.equals("-master-port")) {
			masterPort = Integer.parseInt(i.next());
		} else if (argument.equals("-workers")) {
			numberWorkers = Integer.parseInt(i.next());
		} else
			super.update(argument, i);
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += "master port number: " + masterPort + "\r\n";
		s += "number of workers: " + numberWorkers + "\r\n";
		return s;
	}

}
