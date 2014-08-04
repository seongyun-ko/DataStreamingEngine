package stream.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import stream.util.net.Registry;
import stream.util.net.RegistryClient;

/**
 * This RemoteMethodInvocationTest shows an example of using the stream.util.net package.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class RemoteMethodInvocationTest {

	/**
	 * The main program.
	 * 
	 * @throws Exception
	 *             if an error occurs. 
	 */
	public static void main(String args[]) throws Exception {
		
		// registry-side
		int port = 12001;
		Registry registry = new Registry(port);
		registry.register("TreeSet", new TreeSet<Integer>()); // register a TreeSet<Integer>
		registry.register("HashSet", new HashSet<Integer>()); // register a HashSet<Integer>
		System.out.println("added a TreeSet<Integer> to the registry.");

		// client-side
		RegistryClient client = new RegistryClient("127.0.0.1", port);

		// IP-address and port number of the registry that the client wants to use
		Set<Integer> stub1 = client.lookup("TreeSet", Set.class); // get a stub/proxy to access the TreeSet<Integer>
		// "The client wants to use the registry with the key "TreeSet" as a Set. => It links to the Object"
		// What the clients know is only the interface not the implementation.
		Set<Integer> stub2 = client.lookup("HashSet", Set.class); // get a stub/proxy to access the TreeSet<Integer>

		
		stub1.add(4);
		stub1.add(2);
		stub1.add(1); // It invoke the method add(2) at the remote machine as if the object is its own.
		System.out.println("content of TreeSet<Integer>: " + stub1.toString()); // add 2 and 1 and then print the content

		stub2.add(4);
		stub2.add(2);
		stub2.add(1); // It invoke the method add(2) at the remote machine as if the object is its own.
		System.out.println("content of HashSet<Integer>: " + stub2.toString()); // add 2 and 1 and then print the content
	
		registry.shutdown();
	}
}