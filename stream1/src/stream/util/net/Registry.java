package stream.util.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A Registry maintains a collection of objects that can be used by remote clients.
 * 
 * @author Jeong-Hyon Hwang (jhhwang@cs.albany.edu)
 */
public class Registry {
	// Registry of the combination of Server and Map.
		
	/**
	 * The objects managed by this Registry.
	 */
	HashMap<String, Object> objects = new HashMap<String, Object>();
	// Key : String type , Value : Object type
	
	/**
	 * The server socket.
	 */
	protected java.net.ServerSocket socket;

	/**
	 * The threads that communicate with clients.
	 */
	HashSet<CommunicationThread> activecommunicationThreads = new HashSet<CommunicationThread>();
	
	/**
	 * A CommunicationThread communicates with a client.
	 * 
	 * @author Jeong-Hyon Hwang (jhhwang@cs.albany.edu)
	 */
	protected class CommunicationThread extends Thread {
		// class Coo~T implement Runnable    is also possible with new.Thread(new C~T~()).start();
		// Recommendation : Separate Task and Thread.
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
					Object response = handle(object);
					outputStream.writeObject(response);
					
					outputStream.flush();
					outputStream.reset();
				}
			} catch (Exception e) {
				// Suddenly, if the connection failed, it removes itself.
				synchronized (activecommunicationThreads) {
					activecommunicationThreads.remove(this);
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
						// Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
						// Threads can also be used for blocking operations. Main proceeds even though some threads are blocked.
						synchronized (activecommunicationThreads) {
							activecommunicationThreads.add(communicationThread);
							communicationThread.start();
						}
					} catch (Exception e) {
						if (socket.isClosed()) { 
							// If the serversocket is closed, it disconnects all the connections with all clients.
							break;
						}
					}
				}
				synchronized (activecommunicationThreads) {
					for (CommunicationThread communicationThread : activecommunicationThreads) {
						try {
							communicationThread.socket.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}.start();
		// Anonymous blah blah~
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
	 * @param request
	 *            the object to handle.
	 * @return the result of handling the specified object.
	 */
	public Object handle(Object request) {
		if (request instanceof LookupRequest) { // lookup request
			request = handle((LookupRequest) request);
		} else if (request instanceof MethodInvocationRequest) { // method invocation
			request = handle((MethodInvocationRequest) request);
		}
		return request;
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
		// find the Object requested.
		String objectID = lookupRequest.objectID();
		Class<?> objectType = lookupRequest.objectType();
		Object object = objects.get(objectID);
		return objectType.isInstance(object); 
		// true if the object to access has the type specified by the client.
	}

	/**
	 * Handles the specified MethodInvocationRequest.
	 * 
	 * @param r
	 *            the MethodInvocationRequest to process.
	 * @return the result of invocation; a MethodInvocationException if the method invocation fails.
	 */
	protected Object handle(MethodInvocationRequest r) {
		// r includes all the information needed to invoke certain method.
		Object o = objects.get(r.objectID());
		try {
			Class<?> c = o.getClass();
			Method method = c.getMethod(r.methodName(), r.parameterTypes()); // Find the method
			Object result = method.invoke(o, r.args()); // Invoke the method found
			return result; // return the result
		} catch (Throwable t) {
			return new MethodInvocationException(t);
		}
	}

}
