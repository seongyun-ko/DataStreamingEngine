import java.util.LinkedHashMap;

import stream.data.Tuple;


public class test implements Runnable{
	public static void main(String[] orgs){
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String,Object>();
		// Key가 String type이고, Value가 Object type인 LinkedHashMap 인시턴스 생성
		map.put("Pohang",1);
		map.put("Seoul",2);
		map.put("Pohang",5);
		// the key value is overwritten. 
		map.put("Daegu", 1);
		System.out.println(map);
		
		Tuple t = new Tuple(12341);
		System.out.println(t);
		Tuple t2 = new Tuple(t);// copy constructor
	//  Tuple t2 = t1;
		System.out.println(t2);

		Tuple t3 = new Tuple(t,t2);
		System.out.println(t3);
		
		new Thread(new test()).start();
		new Thread(new test()).start();
		new Thread(new test()).start();
		System.out.println("Done");
	}
	
	public void run() {
		
		for(int i=0; i < 10; i++) System.out.println(i);
	}
	
}
