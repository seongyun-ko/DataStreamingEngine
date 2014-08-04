package stream.query.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import stream.data.Tuple;
import stream.util.MultiValuedHashMap;

/**
 * A JoinOperator produces an output stream containing objects containing values from two input streams.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class JoinOperator extends Operator {

	private double TimeWindow;
	private String[] leftJoinAttributes;
	private String[] rightJoinAttributes;
//	MultiValuedHashMap<ArrayList<Object>, Tuple> h = new MultiValuedHashMap<ArrayList<Object>, Tuple>();
	MultiValuedHashMap<ArrayList<Object>, Tuple> s0 = new MultiValuedHashMap<ArrayList<Object>, Tuple>();
	MultiValuedHashMap<ArrayList<Object>, Tuple> s1 = new MultiValuedHashMap<ArrayList<Object>, Tuple>();
	Set<Tuple> Redundant = new HashSet<Tuple>();	
//	HashMap<Integer, MultiValuedHashMap<ArrayList<Object>, Tuple>> S = new HashMap<Integer, MultiValuedHashMap<ArrayList<Object>, Tuple>>();
	
	Tuple JoinedTuple;
	/**
	 * Constructs a JoinOperator.
	 * 
	 * @param leftJoinAttributes
	 *            the join attributes for the left input.
	 * @param rightJoinAttributes
	 *            the join attributes for the right input.
	 * @param windowSize
	 *            the window size.
	 */
	public JoinOperator(String[] leftJoinAttributes, String[] rightJoinAttributes, double windowSize) {
		this.TimeWindow = windowSize;
		this.leftJoinAttributes = leftJoinAttributes;
		this.rightJoinAttributes = rightJoinAttributes;
	}

	@Override
	public void process(int port, Tuple t) {	
		if( port == 0) {
			s0.add(t.values(leftJoinAttributes), t);
			FindRedundant(port, t);
		}
		else if ( port == 1) {
			s1.add(t.values(rightJoinAttributes), t);
			FindRedundant(port, t);
		}		
		else {
			System.out.println("Port " + port + " is not valid.");
		}
	}
	
	public void FindRedundant(int port, Tuple t) {
		if( port == 0) {
			if(!Redundant.isEmpty());
				Redundant.clear();
			if(s1.containsKey(t.values(leftJoinAttributes)))
				for(Tuple s1_t : s1.get(t.values(leftJoinAttributes))) {
					if(Math.abs(s1_t.timestamp() - t.timestamp()) <= TimeWindow)
						System.out.println(new Tuple(s1_t,t));
					else
						Redundant.add(s1_t);
//						s1.remove(t.values(rightJoinAttributes), s1_t);
				}
			for(Tuple rs : Redundant)
				s1.remove(t.values(leftJoinAttributes), rs);
						
		}
		else {
			if(!Redundant.isEmpty());
				Redundant.clear();
			if(s0.containsKey(t.values(rightJoinAttributes)))
				for(Tuple s0_t : s0.get(t.values(rightJoinAttributes))) {
					if(Math.abs(s0_t.timestamp() - t.timestamp()) <= TimeWindow)
						System.out.println(new Tuple(s0_t,t));
					else
						Redundant.add(s0_t);
//						s0.remove(t.values(leftJoinAttributes), s0_t);
				}
			
			for(Tuple rs : Redundant)
				s0.remove(t.values(leftJoinAttributes), rs);
		}
		
	}
	
	public void display(){
		System.out.println(s0);
		System.out.println(s1);
		//h.get(0).
	}
	
	//public void result(Tuple s_t, Tuple t) {
	//	Tuple Joined = new Tuple(s_t,t);
	//	System.out.println(Joined);
		
	//}

}
