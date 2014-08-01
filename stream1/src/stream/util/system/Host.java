package stream.util.system;

import stream.util.net.CommunicationException;
import stream.util.net.LookupException;
import stream.util.net.Registry;
import stream.util.net.RegistryClient;

/**
 * A Host represents either a Worker or the Master of the system.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Host implements HostInterface {

	/**
	 * The address of this Host in the form of [IP address]:[port number].
	 */
	protected String address;

	/**
	 * The Registry.
	 */
	protected Registry registry;

	/**
	 * The start time of this Host.
	 */
	protected long startTime = System.currentTimeMillis();

	/**
	 * Constructs a Host.
	 * 
	 * @param port
	 *            the port number.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public Host(int port) throws Exception {
		this.address = java.net.InetAddress.getLocalHost().getHostAddress() + ":" + port;
		this.registry = new Registry(port);
		registry.register("", this);
	}

	/**
	 * Returns a stub for accessing the specified Host.
	 * 
	 * @param hostAddress
	 *            the address of the Host in the form of [IP address]:[port number].
	 * @param hostType
	 *            the type of the Host (must be an interface).
	 * @return a stub for accessing the specified Host.
	 * @throws CommunicationException
	 *             if a communication error occurs.
	 * @throws LookupException
	 *             if the specified Host cannot be found.
	 */
	public static <T> T lookup(String hostAddress, Class<?> hostType) throws LookupException, CommunicationException {
		String[] parts = hostAddress.split(":");
		return new RegistryClient(parts[0], Integer.parseInt(parts[1])).lookup("", hostType);
	}

	@Override
	public String hostAddress() {
		return address;
	}

	@Override
	public void shutdown() {
		registry.shutdown();
	}

	/**
	 * Returns the current time.
	 * 
	 * @return the current time.
	 */
	public long getCurrentTime() {
		return System.currentTimeMillis() - startTime;
	}

}
