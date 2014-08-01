package stream.test;

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
		int port = 12000;
		Registry registry = new Registry(port);
		registry.register("", new TreeSet<Integer>()); // register a TreeSet<Integer>
		System.out.println("added a TreeSet<Integer> to the registry.");

		// client-side
		RegistryClient client = new RegistryClient("127.0.0.1", port);
		Set<Integer> stub = client.lookup("", Set.class); // get a stub/proxy to access the TreeSet<Integer>
		stub.add(2);
		stub.add(1);
		System.out.println("content of TreeSet<Integer>: " + stub.toString()); // add 2 and 1 and then print the content

		registry.shutdown();
	}

}
