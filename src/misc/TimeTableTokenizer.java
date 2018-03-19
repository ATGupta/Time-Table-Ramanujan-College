package misc;
import java.util.Vector;


public class TimeTableTokenizer {
	
	private String t;
	private Vector<String> vec;
	
	public TimeTableTokenizer(String a) {
		t=a;
		initialize();
	}
	
	private void initialize(){
		vec=new Vector<String>();
		String b="";
		for(int i=0;i<t.length();i++){
			if(t.charAt(i)=='/'){
				vec.add(b);
				b="";
			}
			else b=b+t.charAt(i);
		}
		vec.add(b);
	}
	
	public int countTokens(){
		return vec.size();
	}
	
	public String tokenAt(int i){
		return vec.get(i);
	}
}
