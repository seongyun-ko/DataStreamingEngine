package stream.query.operator;

import stream.data.Tuple;
import stream.expression.ArithmeticExpression;
import stream.expression.ParsingException;
import stream.expression.StringTokenizer;
import stream.expression.UnboundVariableException;

/**
 * A ProjectionOperator converts Tuples into other Tuples.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class ProjectionOperator extends Operator {
	
	Tuple resultT;
	
	protected ArithmeticExpression predicate;
	String s1 = null;
	String s2 = null;
	/**
	 * Constructs a ProjectionOperator.
	 * 
	 * @param attributeDefinitions
	 *            a collection of String objects defining the output schema of this ProjectionOperator.
	 * @throws ParsingException
	 *             if an error occurs while parsing the expressions.
	 */
	public ProjectionOperator(String[] attributeDefinitions) throws ParsingException {
		// ProjectionOperator o = new ProjectionOperator(new String[] { "Fahrenheit:Celsius * 9 / 5 + 32" });
		//this.predicate = new ArithmeticExpression(attributeDefinitions[0]);
		StringTokenizer st = new StringTokenizer(attributeDefinitions[0], ":", '\"', '#');

		boolean mode = false;
		while(st.hasNext()) {
			if(mode == false) {
				s1 = st.next();
				st.next();
				mode = true;
			}
			else {
				if(s2 == null)
					s2 = st.next();
				else
					s2 += " " + st.next();
			}			
		}
		mode = false;
		this.predicate = new ArithmeticExpression(s2);
		
	}

	@Override
	public void process(int port, Tuple t) {		
		try {
//		resultT = new Tuple( new String[] { s1 }, 
//				new Object[] { t.evaluate(predicate) }, t.timestamp() );
//		System.out.println(resultT);
		this.output(new Tuple( new String[] { s1 }, 
				new Object[] { t.evaluate(predicate) }, t.timestamp() ));
		
		} catch (UnboundVariableException e) {
			e.printStackTrace();
		}
	}
	
	public void print() {
		System.out.println(s1);
		System.out.println(s2);
	}
}