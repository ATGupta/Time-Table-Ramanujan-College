package master;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import misc.BackHelper;
import misc.SetArray;
import edit.EnterStartTime;


public class GenerateMaster {
	
	private int horizontal,vertical,//passed in constructor from behind
		noOfClasses[],noOfSections[][],noOfPeriods[][],//passed in constructor from behind
		totalSections,//calculated in function
		totalRooms,//initialized in constructor
		roomsWithDepartments[],//calculated in function during allotment of rooms
		totalDepartments,//initialized in constructor
		e
	;
	private String rooms[],departments[],classes[][],roomsAlloted[][],encoder;
	private double ratio;
	private boolean depCheckedOnce;
	private DataOutputStream dos;
	private DataInputStream dis;
	private Thread th;
	private Timer timer;
	
	public static void main(String args[]){
		String r[]={"p1","p2","p3","p4","p5"};
		/*String r[]=new String[30];
		for(int i=0;i<30;i++){
			r[i]="p"+Integer.toString(i+1);
		}*/
		String d[]={"d1","d2"};
		int noc[]={2,2};
		String [][]c={
				{"c1","c2"},
				{"c4","c3"}
		};
		int [][]nos={
				{2,4},
				{2,1}
		};
		int [][]nop={
				{24,18},
				{18,20}
		};
		
		new GenerateMaster(10,10,r,d,noc,c,nos,nop);
	}
	
	public GenerateMaster(int h, int v, String r[], String d[], int noc[], String c[][], int nos[][], int nop[][]){
		horizontal=h;vertical=v;
		noOfClasses=noc;noOfPeriods=nop;noOfSections=nos;departments=d;
		classes=c;rooms=r;
		encoder="Aryan";
		
		totalDepartments=departments.length;
		totalRooms=rooms.length;
		
		processingNotice();
		th.start();
		///*
		//getValues();
		calculate();
		formBucket();
		try {
			allotRooms();
			saveToTimeTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//output();
		//*/
		//th.stop();
		JOptionPane.showMessageDialog(null, "Time table generated.", "Done!", JOptionPane.OK_OPTION);
		System.exit(0);
	}
	
	private void processingNotice(){
		th=new Thread(){
			public void run(){
				JLabel label=new JLabel("Processing...");
				JLabel label2=new JLabel("Please wait..");
				JPanel p=new JPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
				p.add(label);p.add(label2);
				JOptionPane.showOptionDialog(null, "Processing...\n"
						+ "Please Wait..", "Processing..", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
				timer=new Timer(2000, new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						timer.stop();
					}
				});
				timer.start();
			}
		};
	}
	
	private void getValues(){
		noOfClasses=new int[]{1,1,1,1,1,3,2,3,3,3,2};
		/*
		 * if needed to change the data in noOfSections[][]
		 * first check for corresponding value in noOfClasses[]
		 * because the loop in calculate() and everywhere else
		 * runs up to this value only
		 */
		noOfSections=new int[][]
				{
					{2,0,0},//computer science
					{1,0,0},//psychology
					{1,0,0},//statistics
					{2,0,0},//mathematics
					{1,0,0},//economics
					{2,4,2},//b. hons.
					{4,4,0},//b. com.
					{1,1,1},//English
					{1,1,1},//political science
					{1,1,1},//hindi
					{1,1,0} //ba prog.
				};
		noOfPeriods=new int[][]
				{
					{22,25,30},
					{25,24,30},
				};
		depCheckedOnce=false;
	}
	
	private void calculate(){
		totalSections=0;
		for(int i=0;i<totalDepartments;i++){
			for(int j=0;j<noOfClasses[i];j++){
				totalSections=totalSections+noOfSections[i][j];
			}
		}
		ratio=((double)totalRooms)/((double)totalSections);
		if(ratio<0.5){
			JOptionPane.showMessageDialog (
					null,
					/*
					 * null, null will be replaced by say null
					 * later
					 */
					"1 Less rooms availablility.\nCannot proceed.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	private void formBucket() {
		
		roomsWithDepartments=new int[totalDepartments];
		boolean depUpdated[]=new boolean[totalDepartments];
		for(int i=0;i<depUpdated.length;i++){
			depUpdated[i]=false;
		}
		
		int tra=0;
		for(int i=0;i<totalDepartments;i++){
			int nos=0;//no of sections//total rooms allotted
			for(int j=0;j<noOfClasses[i];j++){
				nos=nos+noOfSections[i][j];
			}
			roomsWithDepartments[i]=((int)(ratio*nos));
			if(Math.ceil(ratio*nos)>roomsWithDepartments[i])
				roomsWithDepartments[i]++;
			tra=tra+roomsWithDepartments[i];
			if(tra>totalRooms){
				int x=tra-totalRooms;
				int updateI=0,
						updateJ=0;
				for(int j=0;j<totalDepartments;j++){
					updateJ++;
					/* 
					 * if(i!=j)
					 * this if condition
					 * so that we do not do operation on that department jisko
					 * rooms ki zaroorat hai
					 * 
					 */
						int sections=0;
						for(int k=0;k<noOfClasses[j];k++){
							sections=sections+noOfSections[j][k];
						}
						if(depUpdated[j]==false
							&& roomsWithDepartments[j]>=
								(int)(Math.ceil(((double)sections)/2)+1))//nos=no. of sections calculated at that department
							//Math.ceil because of conditions like 2 rooms and and 3 sections. Think.
							/*
							 * && periods available>periods wanted
							 * algorith me ye tha jo convert kar diya ki 1 din me 1 period me
							 * 1 room me 2 classes hi aayengi total
							 * roomsWithDepartments[j]>=2*noOfClasses[j]+2
							 * 
							 */
						{
							
							updateI++;
							roomsWithDepartments[j]--;
							depUpdated[j]=true;
							depCheckedOnce=false;
							/*
							System.out.println("Entered"+"\t"+i+"\t"+j+"\t"+updateI+
									"\t"+x+"\t"+roomsWithDepartments[j]+
											"\t"+(int)(Math.ceil(((double)sections)/2)+1)
											+"\t"+sections);
							*/
							
						}
					
					if(updateI==x){
						break;
					}
					else if(j==totalDepartments-1//loop finish
							&& depCheckedOnce==false)
					/*
					 * depCheckedOnce==false
					 * this was because
					 * 2 checks so as to be sure
					 * that seriously yr.. ab kuchh ni bacha
					 */
					{
						depCheckedOnce=true;
						j=0;
						updateJ=updateI=0;
						continue;
					}
				}
				//System.out.println(updateI+"\t"+x+"\t"+updateJ+"\t"+totalDepartments);
				if(updateI==x
						&& updateJ==totalDepartments){
					
					int t=0;
					for(int j=0;j<totalDepartments;j++){
						if(depUpdated[j])
							t++;
						else if(roomsWithDepartments[j]<=1)
							t++;
					}
					if(t==totalDepartments){//all departments are negated once
						for(int j=0;j<totalDepartments;j++){
							if(depUpdated[j])
								depUpdated[j]=false;
						}
					}
				}
			}
		}
		
		int t=0;
		for(int i=0;i<totalDepartments;i++){
			t=t+roomsWithDepartments[i];
		}
		
		if(t!=totalRooms){
			JOptionPane.showMessageDialog (
					null,
					/*
					 * null, null will be replaced by say getContentPane()
					 * later
					 */
					"2 Less rooms availablility.\nCannot proceed.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	private void allotRooms()throws IOException {
		int max=0;
		for(int i=0;i<totalDepartments;i++){
			max=max>roomsWithDepartments[i]?max:roomsWithDepartments[i];
		}
		roomsAlloted=new String[totalDepartments][max];
		int pos=0;
		for(int i=0;i<totalDepartments;i++){
			for(int j=0;j<roomsWithDepartments[i];j++){
				roomsAlloted[i][j]=rooms[pos];
				pos++;
			}
		}
	}
    
    private void encrypt(String a)throws IOException{
        for(int i=0;i<a.length();i++){
            int x=(int)a.charAt(i)+(int)encoder.charAt(e);
            dos.write(x);
            e=(e+1)%encoder.length();
        }
    }
	
	private void saveToTimeTable()throws IOException{
		int nos=Integer.parseInt(getNoOfSlots());
		dos=new DataOutputStream(new FileOutputStream("deps"));
		e=0;
		for(int i=0;i<totalDepartments;i++){
			encrypt(departments[i]+"\n");
			new File(departments[i]).mkdir();
			new File(departments[i]+"/class").createNewFile();
			new File(departments[i]+"/teacher").createNewFile();
			new File(departments[i]+"/paper").createNewFile();
			new File(departments[i]+"/room").createNewFile();
		}
		dos=new DataOutputStream(new FileOutputStream("depPassword"));
		e=0;
		for(int i=0;i<totalDepartments;i++){
			encrypt("\n");
		}
		for(int i=0;i<totalDepartments;i++){
			e=0;
			dos=new DataOutputStream(new FileOutputStream(departments[i]+"/room"));
			for(int j=0;j<roomsWithDepartments[i];j++){
				String a=roomsAlloted[i][j]
						.toString()+"\n";
				encrypt(a);
			}
		}
		for(int i=0;i<totalDepartments;i++){
			e=0;
			dos=new DataOutputStream(new FileOutputStream(departments[i]+"/class"));
			for(int j=0;j<noOfClasses[i];j++){
				for(int k=0;k<noOfSections[i][j];k++){
					encrypt(classes[i][j]+" Section "+(char)('A'+k)+"\n");
				}
			}
		}
		for(int i=0;i<totalDepartments;i++){
			for(int j=0;j<noOfClasses[i];j++){
				for(int k=0;k<noOfSections[i][j];k++){
					dos=new DataOutputStream(new FileOutputStream(departments[i]+"/"+classes[i][j]+" Section "+(char)('A'+k)));
					e=0;
					for(int l=0;l<7*3*nos;l++){
						encrypt("\n");
					}
					dos.close();
				}
			}
		}
		int t;
		for(int i=0;i<totalDepartments;i++){
			t=0;
			for(int j=0;j<noOfClasses[i];j++){
				for(int k=0;k<noOfSections[i][j];k++){
					e=0;
					dos=new DataOutputStream(new FileOutputStream(departments[i]+"/"+classes[i][j]+" Section "+(char)('A'+k)));
					String a=getSuitableRoom(i,noOfPeriods[i][j]);
					if(a==null){
						JOptionPane.showMessageDialog (null, departments[i]+"\n"
								+ "Nothing Possible\n.", "Error", JOptionPane.ERROR_MESSAGE);
						t=1;
						break;
					}
					else formInTimeTable(i,a,noOfPeriods[i][j],j,k);
				}
				if(t==1)break;
			}
		}
	}
	
	private String getSuitableRoom(int dno, int nop){
		SetArray sa=new SetArray(departments[dno]);
		int nos=Integer.parseInt(getNoOfSlots());
		String arr[][][]=sa.set(SetArray.CHECK_ROOM_ROOM_ARRAY);
		int ppdRequired=(int)Math.ceil(((double)nop)/5.0);//periods per day required
		BackHelper ob=new BackHelper();
		int noFreePer;
		for(int i=0;i<roomsWithDepartments[dno];i++){
			noFreePer=0;
			for(int p=0;p<nos;p++){
				int t=0;
				for(int d=0;d<7;d++){
					if(arr[i][d][p].length()==0)t++;
				}
				if(t==7)noFreePer++;
			}
			if(noFreePer>=ppdRequired){
				ob.push(roomsAlloted[dno][i], Integer.toString(noFreePer));
			}
		}
		if(ob.head==null)return null;
		int max=0;
		String maxRName="";
		while(ob.head!=null){
			maxRName=max>Integer.parseInt(ob.tail.positionDescription)
					?maxRName:ob.tail.dataAtPosition;
			max=max>Integer.parseInt(ob.tail.positionDescription)
					?max:Integer.parseInt(ob.tail.positionDescription);
			ob.pop();
		}
		return maxRName;
	}
	
	private void formInTimeTable(int dno, String rnam, int nop, int classI, int sectionI) throws IOException{
		SetArray sa=new SetArray(departments[dno]);
		int nos=Integer.parseInt(getNoOfSlots());
		String arr[][][]=sa.set(SetArray.CHECK_ROOM_ROOM_ARRAY);
		int ppdRequired=(int)Math.ceil(((double)nop)/5.0);//periods per day required
		int pos=-1;
		int noFreePer=0;
		
		int rno=0;
		for(;rno<roomsWithDepartments[dno];rno++){
			if(roomsAlloted[dno][rno].equals(rnam)){
				break;
			}
		}
		
		for(int p=0;p<nos;p++){
			int t=0;
			for(int d=0;d<7;d++){
				if(arr[rno][d][p].length()==0)t++;
			}
			if(t==7){
				noFreePer++;
				if(pos==-1)pos=p;
			}
			else{
				noFreePer=0;
				pos=-1;
			}
		}
		//System.out.println(rnam+"\t"+noFreePer+"\t"+pos);
		
		String input[][]=new String[7][nos];
		
		for(int d=0;d<7;d++){
			for(int p=0;p<nos;p++)
				input[d][p]="";
		}
		
		dis=new DataInputStream(new FileInputStream(departments[dno]+"/"+classes[dno][classI]+" Section "+(char)('A'+sectionI)));
		e=0;
		String a=decrypt();
		int d=0,p=0,n=0;
		String b="";
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)=='\n'){
				n++;
				if(n%3==0){
					input[d][p]=b;
					p++;
					if(p==nos){
						p=0;d++;
					}
					b="";
				}
				else b=b+a.charAt(i);
			}
		}
		dis.close();
		int t=0;
		for(p=pos;p<nos;p++){
			//System.out.println();
			for(d=0;d<7;d++){
				if(d<5){
					input[d][p]=rnam;
					//System.out.print(d+" "+p+"\t");
					t++;
					if(t==nop){
						//System.out.println();
						//System.out.println(rnam+"\t"+t+"\t"+(d+1)+"\t"+(p+1)+"\t"+pos+"\t"+nos);
						break;
					}
				}
			}
			if(t==nop)break;
		}
		
		/*for(d=0;d<7;d++){
			for(p=0;p<nos;p++)
				System.out.print(input[d][p]+"\t");
			System.out.println();
		}*/
		
		dos=new DataOutputStream(new FileOutputStream(departments[dno]+"/"+classes[dno][classI]+" Section "+(char)('A'+sectionI)));
		e=0;
		
		for(d=0;d<7;d++){
			for(p=0;p<nos;p++){
				encrypt("\n");
				encrypt("\n");
				encrypt(input[d][p]+"\n");
			}
		}
		dos.close();
	}
    
    private String getNoOfSlots(){
        try {
            dis=new DataInputStream(new FileInputStream("noOfSlots"));
        } catch (FileNotFoundException ex) {
            return "nothing retrieved";
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(EnterStartTime.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }
    
    private String decrypt()throws IOException{
        String a="";
        for(;;){
            int x=dis.read();
            if(x==-1)break;
            char c=(char)(x-(int)encoder.charAt(e));
            e=(e+1)%encoder.length();
            a=a+c;
        }
        return a;
    }
	
	private void output(){
		int t=0;
		int sections=0;
		for(int i=0;i<totalDepartments;i++){
			t=t+roomsWithDepartments[i];
			System.out.print("Rooms Allotted = "+roomsWithDepartments[i]);
			int s=0;
			for(int j=0;j<noOfClasses[i];j++){
				s=s+noOfSections[i][j];
			}
			sections=sections+s;
			System.out.println("sections = "+s);
		}
		System.out.println("Total rooms = allotted "+t+" total "+totalRooms);
		System.out.println("Total sections = "+sections);
		for(int i=0;i<totalDepartments;i++){
			System.out.println(departments[i]);
			for(int j=0;j<roomsWithDepartments[i];j++){
				System.out.print(roomsAlloted[i][j]);
			}
			System.out.println();
		}
	}
}
