package misc;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import display.MainDisplayPage;
import edit.EditTimeTable;


public class SetArray {

	final public static int DISPLAY_CLASS_PAPER_ARRAY=0;
	final public static int DISPLAY_CLASS_TEACHER_ARRAY=1;
	final public static int DISPLAY_CLASS_ROOM_ARRAY=2;
	
	final public static int DISPLAY_TEACHER_PAPER_ARRAY=3;
	final public static int DISPLAY_TEACHER_TEACHER_ARRAY=4;
	final public static int DISPLAY_TEACHER_ROOM_ARRAY=5;
	final public static int DISPLAY_TEACHER_CLASS_ARRAY=8;
	
	final public static int CHECK_TEACHER_TEACHER_ARRAY=6;
	final public static int CHECK_ROOM_ROOM_ARRAY=7;
	
	private int nos,e;
	private DataInputStream dis;
	private String encoder="Aryan",department;
	private Vector<String> classVec,teacherVec,depVec,roomVec;
	
	public SetArray(String dep){
		department=dep;
		
		displayVectors();
	}
	
	private void displayVectors() {
        setTeacherVector();
        
        setRoomVector();
		
        setClassVector();
	}
	
	private void setTeacherVector(){
		String a=null,b="";
        teacherVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(department+"/teacher"));
        } catch (FileNotFoundException ex) {
        	return;
        }
        e=0;
        a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                teacherVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
	}
	
	private void setRoomVector(){
		String a=null,b="";
        roomVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(department+"/room"));
        } catch (FileNotFoundException ex) {
        	return;
        }
        e=0;
        a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                roomVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
	}
	
	private void setClassVector(){
		String a=null,b="";
		
        classVec=new Vector<String>();
    	File f=new File(department+"/class");
    	String path=f.getAbsolutePath();
        try {
            dis=new DataInputStream(new FileInputStream(path));
        } catch (FileNotFoundException ex) {
        	return;
        }
        e=0;
        a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                classVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
	}

	public String[][][] set(int x){
		
		if(x==0)
			return displayClassPaperArray();
		else if(x==1)
			return displayClassTeacherArray();
		else if(x==2)
			return displayClassRoomArray();
		else if(x==3)
			return displayTeacherPaperArray();
		else if(x==4)
			return displayTeacherTeacherArray();
		else if(x==5)
			return displayTeacherRoomArray();
		else if(x==8)
			return displayTeacherClassArray();
		else if(x==6)
			return checkTeacherTeacherArray();
		else if(x==7)
			return checkRoomRoomArray();
		return null;
	}

	private String[][][] displayClassPaperArray() {
		nos=getNoOfSlots();
		String arr[][][]=new 
				String[classVec.size()]
						[7]
								[nos];
		for(int i=0;i<classVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
        String a="";
        for(int i=0;i<classVec.size();i++){
            try {
                dis=new DataInputStream(new FileInputStream(department+"/"+classVec.get(i)));
            } catch (FileNotFoundException ex) {
                continue;
            }
            e=0;
            try {
                a=decrypt();
            } catch (IOException ex) {
                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            int n=0,d=0,p=0;
            String b="";
            for(int o=0;o<a.length();o++){
                if(a.charAt(o)=='\n'){
                    if(n%3==0){
                        arr[i][d][p]=b;
                    }
                    else if(n%3==1){
                        //teacher[i][d][p]=b;
                    }
                    else if(n%3==2){
                        //room[i][d][p]=b;
                        p++;
                        if(p==nos){
                            p=0;d++;
                        }
                    }
                    n++;
                    b="";
                }
                else b=b+a.charAt(o);
            }
        }
		return arr;
	}

	private String[][][] displayClassTeacherArray() {
		nos=getNoOfSlots();
		String arr[][][]=new String[classVec.size()][7][nos];
		for(int i=0;i<classVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
        String a="";
        for(int i=0;i<classVec.size();i++){
            try {
                dis=new DataInputStream(new FileInputStream(department+"/"+classVec.get(i)));
            } catch (FileNotFoundException ex) {
                continue;
            }
            e=0;
            try {
                a=decrypt();
            } catch (IOException ex) {
                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            int n=0,d=0,p=0;
            String b="";
            for(int o=0;o<a.length();o++){
                if(a.charAt(o)=='\n'){
                    if(n%3==0){
                        //paper[i][d][p]=b;
                    }
                    else if(n%3==1){
                        arr[i][d][p]=b;
                    }
                    else if(n%3==2){
                        //room[i][d][p]=b;
                        p++;
                        if(p==nos){
                            p=0;d++;
                        }
                    }
                    n++;
                    b="";
                }
                else b=b+a.charAt(o);
            }
        }
		return arr;
	}

	private String[][][] displayClassRoomArray() {
		nos=getNoOfSlots();
		String arr[][][]=new String[classVec.size()][7][nos];
		for(int i=0;i<classVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
        String a="";
        for(int i=0;i<classVec.size();i++){
            try {
                dis=new DataInputStream(new FileInputStream(department+"/"+classVec.get(i)));
            } catch (FileNotFoundException ex) {
                continue;
            }
            e=0;
            try {
                a=decrypt();
            } catch (IOException ex) {
                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            int n=0,d=0,p=0;
            String b="";
            for(int o=0;o<a.length();o++){
                if(a.charAt(o)=='\n'){
                    if(n%3==0){
                        //paper[i][d][p]=b;
                    }
                    else if(n%3==1){
                        //teacher[i][d][p]=b;
                    }
                    else if(n%3==2){
                        arr[i][d][p]=b;
                        p++;
                        if(p==nos){
                            p=0;d++;
                        }
                    }
                    n++;
                    b="";
                }
                else b=b+a.charAt(o);
            }
        }
		return arr;
	}

	private String[][][] displayTeacherPaperArray() {
		nos=getNoOfSlots();
		String arr[][][]=new String[teacherVec.size()][7][nos];
		for(int i=0;i<teacherVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
		
		depVec=new Vector<String>();
		try {
			dis=new DataInputStream(new FileInputStream("deps"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		e=0;
		String a="";
		try {
			a=decrypt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String b="";
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)=='\n'){
				depVec.add(b);
				b="";
			}
			else b=b+a.charAt(i);
		}
		
		String pp="",tt="",rr="";
		for(int j=0;j<depVec.size();j++){
			
			a="";
			
			classVec=new Vector<String>();
	    	File f=new File(depVec.get(j)+"/class");
	    	String path=f.getAbsolutePath();
	        try {
	            dis=new DataInputStream(new FileInputStream(path));
	        } catch (FileNotFoundException ex) {
	        	continue;
	        }
	        e=0;
	        a=null;
	        try {
	            a=decrypt();
	        } catch (IOException ex) {
	            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        b="";
	        for(int i=0;i<a.length();i++){
	            if(a.charAt(i)=='\n'){
	                classVec.add(b);
	                b="";
	            }
	            else b=b+a.charAt(i);
	        }
	        
	        a="";
	        
	        for(int i=0;i<classVec.size();i++){
	            try {
	                dis=new DataInputStream(new FileInputStream(depVec.get(j)+"/"+classVec.get(i)));
	            } catch (FileNotFoundException ex) {
	                continue;
	            }
	            e=0;
	            try {
	                a=decrypt();
	            } catch (IOException ex) {
	                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            int n=0,d=0,p=0;
	            b="";
	            for(int o=0;o<a.length();o++){
	                if(a.charAt(o)=='\n'){
	                    if(n%3==0){
	                        //paper[i][d][p]=b;
	                    	pp=b;
	                    }
	                    else if(n%3==1){
	                        //teacher[i][d][p]=b;
	                    	tt=b;
	                    }
	                    else if(n%3==2){
	                    	//room[i][d][p]=b;
	                    	rr=b;
	                        
	                        TimeTableTokenizer ttt=new TimeTableTokenizer(tt);
		            		for(int tok=0;tok<ttt.countTokens();tok++){
		            			for(int tv=0;tv<teacherVec.size();tv++){
		            				if(ttt.tokenAt(tok).equals(teacherVec.get(tv))){
		            					TimeTableTokenizer ppt=new TimeTableTokenizer(pp);
		            					arr[tv][d][p]=ppt.tokenAt(tok);
		    	                    	
		            					//arr[tv][d][p]=ttt.tokenAt(tok);
		    	                    	
		            					//TimeTableTokenizer rrt=new TimeTableTokenizer(rr);
		    	                    	//arr[tv][d][p]=rrt.tokenAt(tok);
		            				}
		            			}
		            		}
	                        
	                    	p++;
	                        if(p==nos){
	                            p=0;d++;
	                        }
	                        
	                    }
	                    n++;
	                    b="";
	                }
	                else b=b+a.charAt(o);
	            }
	        }
		}
		
		return arr;
	}

	private String[][][] displayTeacherTeacherArray() {
		nos=getNoOfSlots();
		String arr[][][]=new String[teacherVec.size()][7][nos];
		for(int i=0;i<teacherVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
		
		depVec=new Vector<String>();
		try {
			dis=new DataInputStream(new FileInputStream("deps"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		e=0;
		String a="";
		try {
			a=decrypt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String b="";
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)=='\n'){
				depVec.add(b);
				b="";
			}
			else b=b+a.charAt(i);
		}
		
		String pp="",tt="",rr="";
		for(int j=0;j<depVec.size();j++){
			
			a="";
			
			classVec=new Vector<String>();
	    	File f=new File(depVec.get(j)+"/class");
	    	String path=f.getAbsolutePath();
	        try {
	            dis=new DataInputStream(new FileInputStream(path));
	        } catch (FileNotFoundException ex) {
	        	continue;
	        }
	        e=0;
	        a=null;
	        try {
	            a=decrypt();
	        } catch (IOException ex) {
	            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        b="";
	        for(int i=0;i<a.length();i++){
	            if(a.charAt(i)=='\n'){
	                classVec.add(b);
	                b="";
	            }
	            else b=b+a.charAt(i);
	        }
	        
	        a="";
	        
	        for(int i=0;i<classVec.size();i++){
	            try {
	                dis=new DataInputStream(new FileInputStream(depVec.get(j)+"/"+classVec.get(i)));
	            } catch (FileNotFoundException ex) {
	            	continue;
	            }
	            e=0;
	            try {
	                a=decrypt();
	            } catch (IOException ex) {
	                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            int n=0,d=0,p=0;
	            b="";
	            for(int o=0;o<a.length();o++){
	                if(a.charAt(o)=='\n'){
	                    if(n%3==0){
	                        //paper[i][d][p]=b;
	                    	pp=b;
	                    }
	                    else if(n%3==1){
	                        //teacher[i][d][p]=b;
	                    	tt=b;
	                    }
	                    else if(n%3==2){
	                    	//room[i][d][p]=b;
	                    	rr=b;
	                        
	                        TimeTableTokenizer ttt=new TimeTableTokenizer(tt);
		            		for(int tok=0;tok<ttt.countTokens();tok++){
		            			for(int tv=0;tv<teacherVec.size();tv++){
		            				if(ttt.tokenAt(tok).equals(teacherVec.get(tv))){
		            					//TimeTableTokenizer ppt=new TimeTableTokenizer(pp);
		    	                    	//arr[tv][d][p]=ppt.tokenAt(tok);
		    	                    	
		            					arr[tv][d][p]=ttt.tokenAt(tok);
		            					
		            					//TimeTableTokenizer rrt=new TimeTableTokenizer(rr);
		    	                    	//arr[tv][d][p]=rrt.tokenAt(tok);
		            				}
		            			}
		            		}
	                        
	                    	p++;
	                        if(p==nos){
	                            p=0;d++;
	                        }
		            		
	                    }
	                    n++;
	                    b="";
	                    
	                }
	                else b=b+a.charAt(o);
	            }
	        }
		}
        
		return arr;
	}

	private String[][][] displayTeacherRoomArray() {
		nos=getNoOfSlots();
		String arr[][][]=new String[teacherVec.size()][7][nos];
		for(int i=0;i<teacherVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
		
		depVec=new Vector<String>();
		try {
			dis=new DataInputStream(new FileInputStream("deps"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		e=0;
		String a="";
		try {
			a=decrypt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String b="";
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)=='\n'){
				depVec.add(b);
				b="";
			}
			else b=b+a.charAt(i);
		}
		
		String pp="",tt="",rr="";
		for(int j=0;j<depVec.size();j++){
			
			a="";
			
			classVec=new Vector<String>();
	    	File f=new File(depVec.get(j)+"/class");
	    	String path=f.getAbsolutePath();
	        try {
	            dis=new DataInputStream(new FileInputStream(path));
	        } catch (FileNotFoundException ex) {
	        	continue;
	        }
	        e=0;
	        a=null;
	        try {
	            a=decrypt();
	        } catch (IOException ex) {
	            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        b="";
	        for(int i=0;i<a.length();i++){
	            if(a.charAt(i)=='\n'){
	                classVec.add(b);
	                b="";
	            }
	            else b=b+a.charAt(i);
	        }
	        
	        a="";
	        
	        for(int i=0;i<classVec.size();i++){
	            try {
	                dis=new DataInputStream(new FileInputStream(depVec.get(j)+"/"+classVec.get(i)));
	            } catch (FileNotFoundException ex) {
	            	continue;
	            }
	            e=0;
	            try {
	                a=decrypt();
	            } catch (IOException ex) {
	                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            int n=0,d=0,p=0;
	            b="";
	            for(int o=0;o<a.length();o++){
	                if(a.charAt(o)=='\n'){
	                    if(n%3==0){
	                        //paper[i][d][p]=b;
	                    	pp=b;
	                    }
	                    else if(n%3==1){
	                        //teacher[i][d][p]=b;
	                    	tt=b;
	                    }
	                    else if(n%3==2){
	                    	//room[i][d][p]=b;
	                    	rr=b;
	                        
	                        TimeTableTokenizer ttt=new TimeTableTokenizer(tt);
		            		for(int tok=0;tok<ttt.countTokens();tok++){
		            			for(int tv=0;tv<teacherVec.size();tv++){
		            				if(ttt.tokenAt(tok).equals(teacherVec.get(tv))){
		            					//TimeTableTokenizer ppt=new TimeTableTokenizer(pp);
		    	                    	//arr[tv][d][p]=ppt.tokenAt(tok);
		    	                    	
		            					//arr[tv][d][p]=ttt.tokenAt(tok);

		            					TimeTableTokenizer rrt=new TimeTableTokenizer(rr);
		    	                    	arr[tv][d][p]=rrt.tokenAt(tok);
		            				}
		            			}
		            		}
	                        
	                    	p++;
	                        if(p==nos){
	                            p=0;d++;
	                        }
	                        
	                    }
	                    n++;
	                    b="";
	                    
	                }
	                else b=b+a.charAt(o);
	            }
	        }
		}
        
		return arr;
	}
	
	private String[][][] displayTeacherClassArray(){
		nos=getNoOfSlots();
		String [][][]arr=new String[teacherVec.size()][7][nos];
		for(int i=0;i<teacherVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
		
		depVec=new Vector<String>();
		try {
			dis=new DataInputStream(new FileInputStream("deps"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		e=0;
		String a="";
		try {
			a=decrypt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String b="";
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)=='\n'){
				depVec.add(b);
				b="";
			}
			else b=b+a.charAt(i);
		}
		
		String pp="",tt="",rr="";
		for(int j=0;j<depVec.size();j++){
			
			a="";
			
			classVec=new Vector<String>();
	    	File f=new File(depVec.get(j)+"/class");
	    	String path=f.getAbsolutePath();
	        try {
	            dis=new DataInputStream(new FileInputStream(path));
	        } catch (FileNotFoundException ex) {
	        	continue;
	        }
	        e=0;
	        a=null;
	        try {
	            a=decrypt();
	        } catch (IOException ex) {
	            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        b="";
	        for(int i=0;i<a.length();i++){
	            if(a.charAt(i)=='\n'){
	                classVec.add(b);
	                b="";
	            }
	            else b=b+a.charAt(i);
	        }
	        
	        a="";
	        
	        for(int i=0;i<classVec.size();i++){
	            try {
	                dis=new DataInputStream(new FileInputStream(depVec.get(j)+"/"+classVec.get(i)));
	                //System.out.println("Found\t\t"+depVec.get(j)+"/"+classVec.get(i));
	            } catch (FileNotFoundException ex) {
	            	//System.out.println("Not Found\t\t"+depVec.get(j)+"/"+classVec.get(i));
	            	continue;
	            }
	            e=0;
	            try {
	                a=decrypt();
	            } catch (IOException ex) {
	                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            int n=0,d=0,p=0;
	            b="";
	            for(int o=0;o<a.length();o++){
	                if(a.charAt(o)=='\n'){
	                    if(n%3==0){
	                        //paper[i][d][p]=b;
	                    	pp=b;
	                    }
	                    else if(n%3==1){
	                        //teacher[i][d][p]=b;
	                    	tt=b;
	                    }
	                    else if(n%3==2){
	                    	//room[i][d][p]=b;
	                    	rr=b;
	                        
	                        TimeTableTokenizer ttt=new TimeTableTokenizer(tt);
		            		for(int tok=0;tok<ttt.countTokens();tok++){
		            			for(int tv=0;tv<teacherVec.size();tv++){
		            				if(ttt.tokenAt(tok).equals(teacherVec.get(tv))){
		            					//TimeTableTokenizer ppt=new TimeTableTokenizer(pp);
		    	                    	//arr[tv][d][p]=ppt.tokenAt(tok);
		    	                    	
		            					//arr[tv][d][p]=ttt.tokenAt(tok);

		            					//TimeTableTokenizer rrt=new TimeTableTokenizer(rr);
		    	                    	//arr[tv][d][p]=rrt.tokenAt(tok);
		            					
		            					arr[tv][d][p]=classVec.get(i);
		            				}
		            			}
		            		}
	                        
	                    	p++;
	                        if(p==nos){
	                            p=0;d++;
	                        }
	                        
	                    }
	                    n++;
	                    b="";
	                    
	                }
	                else b=b+a.charAt(o);
	            }
	        }
		}
        
		return arr;
		
	}
	
    private int getNoOfSlots(){
        try {
            dis=new DataInputStream(new FileInputStream("noOfSlots"));
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog (null, "Number of slots not found.\nInitialize no. of Slots for each day.\nExiting..", "Text Read Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(EditTimeTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Integer.parseInt(a);
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

	private String[][][] checkTeacherTeacherArray() {
		// algorithm copied from displayTeacherTeacherArray();
		
		nos=getNoOfSlots();
		String arr[][][]=new String[teacherVec.size()][7][nos];
		
		for(int i=0;i<teacherVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
		
		depVec=new Vector<String>();
		try {
			dis=new DataInputStream(new FileInputStream("deps"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		e=0;
		String a="";
		try {
			a=decrypt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String b="";
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)=='\n'){
				depVec.add(b);
				b="";
			}
			else b=b+a.charAt(i);
		}
		
		String pp="",tt="",rr="";
		for(int j=0;j<depVec.size();j++){
			
	        classVec=new Vector<String>();
	        try {
	        	File f=new File(depVec.get(j)+"/class");
	        	String path=f.getAbsolutePath();
	            dis=new DataInputStream(new FileInputStream(path));
	        } catch (FileNotFoundException ex) {
	        	continue;
	        }
	        e=0;
	        a=null;
	        try {
	            a=decrypt();
	        } catch (IOException ex) {
	            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        b="";
	        for(int o=0;o<a.length();o++){
	            if(a.charAt(o)=='\n'){
	                classVec.add(b);
	                b="";
	            }
	            else b=b+a.charAt(o);
	        }
	        
			a="";
	        for(int i=0;i<classVec.size();i++){
	        	
	            try {
	                dis=new DataInputStream(new FileInputStream(depVec.get(j)+"/"+classVec.get(i)));
	            } catch (FileNotFoundException ex) {
	            	continue;
	            }
	            e=0;
	            try {
	                a=decrypt();
	            } catch (IOException ex) {
	                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            int n=0,d=0,p=0;
	            b="";
	            for(int o=0;o<a.length();o++){
	                if(a.charAt(o)=='\n'){
	                    if(n%3==0){
	                        //paper[i][d][p]=b;
	                    	pp=b;
	                    }
	                    else if(n%3==1){
	                        //teacher[i][d][p]=b;
	                    	tt=b;
	                    }
	                    else if(n%3==2){
	                    	//room[i][d][p]=b;
	                    	rr=b;

	                        
	                        TimeTableTokenizer ttt=new TimeTableTokenizer(tt);
		            		for(int tok=0;tok<ttt.countTokens();tok++){
		            			for(int tv=0;tv<teacherVec.size();tv++){
		            				if(ttt.tokenAt(tok).equals(teacherVec.get(tv))){
		            					TimeTableTokenizer ppt=new TimeTableTokenizer(pp);
		            					
		            					TimeTableTokenizer rrt=new TimeTableTokenizer(rr);
		    	                    	
		            					arr[tv][d][p]=depVec.get(j)+"\n"
				            					+classVec.get(i)+"\n"
				            					+ppt.tokenAt(tok)+"\n"
		            							+ttt.tokenAt(tok)+"\n"
		            							+rrt.tokenAt(tok)+"\n";
		            					
		            					
		            				}
		            			}
		            		}
		            		
	                    	p++;
	                        if(p==nos){
	                            p=0;d++;
	                        }
		            		
	                    }
	                    n++;
	                    b="";
	                    
	                }
	                else b=b+a.charAt(o);
	            }
	        }
		}
        
		return arr;
	}

	private String[][][] checkRoomRoomArray() {
		//algorithm copied from checkTeacherTeacherArray()
		
		nos=getNoOfSlots();
		String arr[][][]=new String[roomVec.size()][7][nos];
		
		for(int i=0;i<roomVec.size();i++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    arr[i][d][p]="";
                }
            }
        }
		
		depVec=new Vector<String>();
		try {
			dis=new DataInputStream(new FileInputStream("deps"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		e=0;
		String a="";
		try {
			a=decrypt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String b="";
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)=='\n'){
				depVec.add(b);
				b="";
			}
			else b=b+a.charAt(i);
		}
		
		String pp="",tt="",rr="";
		for(int j=0;j<depVec.size();j++){
			
	        classVec=new Vector<String>();
        	File f=new File(depVec.get(j)+"/class");
        	String path=f.getAbsolutePath();
	        try {
	            dis=new DataInputStream(new FileInputStream(path));
	        } catch (FileNotFoundException ex) {
	        	continue;
	        }
	        e=0;
	        a=null;
	        try {
	            a=decrypt();
	        } catch (IOException ex) {
	            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        b="";
	        for(int i=0;i<a.length();i++){
	            if(a.charAt(i)=='\n'){
	                classVec.add(b);
	                b="";
	            }
	            else b=b+a.charAt(i);
	        }
	        
			a="";
	        for(int i=0;i<classVec.size();i++){
	        	f=new File(depVec.get(j)+"/"+classVec.get(i));
	        	path=f.getAbsolutePath();
	            try {
	                dis=new DataInputStream(new FileInputStream(path));
	            } catch (FileNotFoundException ex) {
	            	continue;
	            }
	            e=0;
	            try {
	                a=decrypt();
	            } catch (IOException ex) {
	                Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            int n=0,d=0,p=0;
	            b="";
	            for(int o=0;o<a.length();o++){
	                if(a.charAt(o)=='\n'){
	                    if(n%3==0){
	                        //paper[i][d][p]=b;
	                    	pp=b;
	                    }
	                    else if(n%3==1){
	                        //teacher[i][d][p]=b;
	                    	tt=b;
	                    }
	                    else if(n%3==2){
	                    	//room[i][d][p]=b;
	                    	rr=b;
	                        
	                        TimeTableTokenizer rrt=new TimeTableTokenizer(rr);
		            		for(int tok=0;tok<rrt.countTokens();tok++){
		            			for(int rv=0;rv<roomVec.size();rv++){
		            				if(rrt.tokenAt(tok).equals(roomVec.get(rv))){
		            					TimeTableTokenizer ppt=new TimeTableTokenizer(pp);
		            					
		            					TimeTableTokenizer ttt=new TimeTableTokenizer(tt);
		    	                    	
		            					arr[rv][d][p]=depVec.get(j)+"\n"
				            					+classVec.get(i)+"\n"
				            					+ppt.tokenAt(tok)+"\n"
		            							+ttt.tokenAt(tok)+"\n"
		            							+rrt.tokenAt(tok)+"\n";
		            					
		            				}
		            			}
		            		}
	                        
	                    	p++;
	                        if(p==nos){
	                            p=0;d++;
	                        }
		            		
	                    }
	                    n++;
	                    b="";
	                    
	                }
	                else b=b+a.charAt(o);
	            }
	        }
		}
		
		return arr;
	}
}
