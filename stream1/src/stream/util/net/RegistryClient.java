package stream.util.net;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A RegistryClient can connect to a Registry and obtain a stub for accessing an object managed by the Registry.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class RegistryClient {

	/**
	 * The address of the Registry.
	 */
	protected String address;

	/**
	 * The port number of the Registry.
	 */
	protected int port;

	/**
	 * The socket used for communication.
	 */
	protected java.net.Socket socket = null;

	/**
	 * The input stream.
	 */
	protected java.io.ObjectInputStream inputStream = null;

	/**
	 * The output stream.
	 */
	protected java.io.ObjectOutputStream outputStream = null;

	/**
	 * Constructs a RegistryClient.
	 * 
	 * @param address
	 *            the address of the Registry.
	 * @param port
	 *            the port number of the Registry.
	 */
	public RegistryClient(String address, int port) {
		this.address = address;
		this.port = port;
		connect();
	}

	/**
	 * Returns a stub for accessing an object managed by the Registry.
	 * 
	 * @param objectID
	 *            the ID of the object.
	 * @param objectType
	 *            the type of the object (must be an interface).
	 * @return a stub for accessing an object managed by the Registry.
	 * @throws CommunicationException
	 *             if a communication error occurs.
	 * @throws LookupException
	 *             if the Registry has no object with the specified ID and type.
	 */
	public <T> T lookup(String objectID, Class<?> objectType) throws LookupException, CommunicationException {
		if (!objectType.isInterface())
			throw new LookupException("" + objectType + " is not an interface.");
		if ((Boolean) sendToRegistry(new LookupRequest(objectID, objectType))) {
			// if the remote object is of the specified interface type
			return createStub(objectID, objectType);
		} else {
			throw new LookupException("The remote object is not of the " + objectType + " type.");
		}
	}

	/**
	 * Constructs a stub for accessing an object managed by the Registry.
	 * 
	 * @param objectID
	 *            the ID of the object.
	 * @param objectType
	 *            the type of the object (must be an interface).
	 * @return a stub for accessing an object managed by the Registry.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T createStub(final String objectID, Class<?> objectType) {
		return (T) Proxy.newProxyInstance(objectType.getClassLoader(), new Class[] { objectType },
				new InvocationHandler() {

					public Object invoke(Object object, Method method, Object[] args) throws Throwable {
						Object invocationResult;
						try {
							// obtain the result of invoking the specified method on the Registry
							invocationResult = sendToRegistry(new MethodInvocationRequest(objectID, method, args));
						} catch (CommunicationException t) { // if the communication fails
							throw new MethodInvocationException(t);
						}
						if (invocationResult instanceof MethodInvocationException) {
							// if received a MethodInvocationException from the Registry
							throw (MethodInvocationException) invocationResult;
						}
						return invocationResult;
					}
				});
	}

	/**
	 * Sends the specified object to the Registry.
	 * 
	 * @param object
	 *            the object to send to the Registry.
	 * @throws CommunicationException
	 *             if the communication fails.
	 */
	public synchronized Object sendToRegistry(Object object) throws CommunicationException {
		try {
			outputStream.writeObject(object);
			outputStream.flush();
			outputStream.reset();

			object = inputStream.readObject();
			return object;
		} catch (Throwable t) {
			throw new CommunicationException(t);
		}
	}

	@Override
	public String toString() {
		return "[->" + address + ":" + port + "]";
	}

	/**
	 * Connects to the Registry.
	 */
	protected void connect() {
		for (;;) {
			try {
				socket = new java.net.Socket(address, port);
				outputStream = new java.io.ObjectOutputStream(socket.getOutputStream());
				inputStream = new java.io.ObjectInputStream(socket.getInputStream());
				return;
			} catch (Exception e) {
				System.err.println(e);
				try { // if failed, sleep for a short while.
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
		}
	}

}
