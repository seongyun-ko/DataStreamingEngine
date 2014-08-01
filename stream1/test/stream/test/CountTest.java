package stream.test;

import stream.query.operator.aggregate.Count;

public class CountTest {
	
	public static void main(String[] args) {
	Count c = new Count();
	c.update("Ya");
	c.update("Ya");
	System.out.println(c);
	}
}
