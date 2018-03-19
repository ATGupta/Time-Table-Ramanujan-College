package misc;

public class BackHelper {
	
	public BackHelper next,prev;
	public String dataAtPosition;
	public String positionDescription;
	public BackHelper head,tail;
	
	public BackHelper(){
		head=tail=null;
		next=prev=null;
		positionDescription=dataAtPosition="";
	}
	
	public static void main(String args[]){
		BackHelper ob=new BackHelper();
		
		int i=0;
		ob.push(Integer.toString(i),Integer.toString(i+10));
		
		i++;
		ob.push(Integer.toString(i),Integer.toString(i+10));
		
		i++;
		ob.push(Integer.toString(i),Integer.toString(i+10));
		
		ob.pop();ob.pop();ob.pop();
		
		ob.display(ob.head);
		
	}
	
	public void push(String dataAtPos,String posInfo){
		BackHelper ob=new BackHelper();
		
		ob.dataAtPosition=dataAtPos;
		ob.positionDescription=posInfo;
		
		if(head==null && tail==null){
			head=ob;
		}
		else{
			tail.next=ob;
			ob.prev=tail;
		}
		
		tail=ob;
	}
	
	public void pop(){
		if(tail==null){
			return;
		}
		tail=tail.prev;
		if(tail==null){
			head=tail;
			return;
		}
		tail.next=null;
	}
	
	public void display(BackHelper ob){
		while(ob!=null){
			System.out.println(ob.positionDescription);
			System.out.println(ob.dataAtPosition);
			ob=ob.next;
		}
	}
}
