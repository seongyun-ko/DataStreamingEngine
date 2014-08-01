package stream.util.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A Registry maintains a collection of objects that can be used by remote clients.
 * 
 * @author Jeong-Hyon Hwang (jhhwang@cs.albany.edu)
 */
public class Registry {

	/**
	 * The objects managed by this Registry.
	 */
	HashMap<String, Object> objects = new HashMap<String, Object>();

	/**
	 * The server socket.
	 */
	protected java.net.ServerSocket socket;

	/**
	 * The threads that communicate with clients.
	 */
	HashSet<CommunicationThread> communicationThreads = new HashSet<CommunicationThread>();

	/**
	 * A CommunicationThread communicates with a client.
	 * 
	 * @author Jeong-Hyon Hwang (jhhwang@cs.albany.edu)
	 */
	protected class CommunicationThread extends Thread {

		/**
		 * A socket for communication with the client.
		 */
		java.net.Socket socket;

		/**
		 * Constructs a CommunicationThread.
		 * 
		 * @param socket
		 *            the socket to communicate with a client.
		 */
		public CommunicationThread(java.net.Socket socket) {
			this.socket = socket;
		}

		/**
		 * Keeps reading and handling data from the client.
		 */
		public void run() {
			try {
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				for (;;) {
					Object object = inputStream.readObject();

					object = handle(object);
					outputStream.writeObject(object);
					outputStream.flush();
					outputStream.reset();
				}
			} catch (Exception e) {
				synchronized (communicationThreads) {
					communicationThreads.remove(this);
				}
			}
		}
	}

	/**
	 * Constructs a Registry.
	 * 
	 * @param port
	 *            the communication port.
	 * @throws Exception
	 *             if it cannot create a server socket.
	 */
	public Registry(int port) throws Exception {
		super();
		socket = new java.net.ServerSocket(port);
		new Thread() {
			public void run() {
				for (;;) {
					try {
						CommunicationThread communicationThread = new CommunicationThread(socket.accept());
						synchronized (communicationThreads) {
							communicationThreads.add(communicationThread);
							communicationThread.start();
						}
					} catch (Exception e) {
						if (socket.isClosed()) {
							break;
						}
					}
				}
				synchronized (communicationThreads) {
					for (CommunicationThread communicationThread : communicationThreads) {
						try {
							communicationThread.socket.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}.start();
	}

	/**
	 * Adds an object to this Registry.
	 * 
	 * @param objectID
	 *            the identifier of the object.
	 * @param object
	 *            the object to add.
	 */
	public void register(String objectID, Object object) {
		objects.put(objectID, object);
	}

	/**
	 * Removes the specified object.
	 * 
	 * @param objectID
	 *            the identifier of the object.
	 */
	public void remove(String objectID) {
		objects.remove(objectID);
	}

	/**
	 * Shuts down this Registry.
	 */
	public void shutdown() {
		try {
			socket.close();
		} catch (Exception e) {
		}
	}

	@Override
	public String toString() {
		return "[" + getLocalHostName() + ":" + socket.getLocalPort() + "]";
	}

	/**
	 * Returns the name of the host that runs this Registry.
	 * 
	 * @return the name of the host that runs this Registry.
	 */
	protected String getLocalHostName() {
		try {
			return java.net.InetAddress.getLocalHost().getCanonicalHostName();
		} catch (Exception e) {
			return "?";
		}
	}

	/**
	 * Handles the specified object.
	 * 
	 * @param object
	 *            the object to handle.
	 * @return the result of handling the specified object.
	 */
	public Object handle(Object object) {
		if (object instanceof LookupRequest) { // lookup request
			object = handle((LookupRequest) object);
		} else if (object instanceof MethodInvocationRequest) { // method invocation
			object = handle((MethodInvocationRequest) object);
		}
		return object;
	}

	/**
	 * Handles the specified LookupRequest.
	 * 
	 * @param lookupRequest
	 *            the LookupRequest.
	 * @return true if the lookup request succeeds (i.e., the object to access has the type specified by the client);
	 *         false otherwise.
	 */
	protected boolean handle(LookupRequest lookupRequest) {
		String objectID = lookupRequest.objectID();
		Class<?> objectType = lookupRequest.objectType();
		Object object = objects.get(objectID);
		return objectType.isInstance(object); // true if the object to access has the type specified by the client.
	}

	/**
	 * Handles the specified MethodInvocationRequest.
	 * 
	 * @param r
	 *            the MethodInvocationRequest to process.
	 * @return the result of invocation; a MethodInvocationException if the method invocation fails.
	 */
	protected Object handle(MethodInvocationRequest r) {
		Object o = objects.get(r.objectID());
		try {
			Class<?> c = o.getClass();
			Object result = c.getMethod(r.methodName(), r.parameterTypes()).invoke(o, r.args());
			return result; // return the result
		} catch (Throwable t) {
			return new MethodInvocationException(t);
		}
	}

}
