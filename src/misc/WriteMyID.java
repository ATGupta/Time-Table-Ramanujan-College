package misc;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;


public class WriteMyID {
	
	private DataInputStream dis;
	private DataOutputStream dos;
	private String encoder,todays;
	private int e;
	private Vector<String> vec;
	private Date date;
	private int y,mon,d,h,min,sec,yearMon;
	
	public WriteMyID(){
		return;
		/*
		encoder="Aryan";
		todays="";
		
		date=new Date();
		y=date.getYear();y=y+1900;
		mon=date.getMonth();mon=mon+1;
		d=date.getDate();
		h=date.getHours();
		min=date.getMinutes();
		sec=date.getSeconds();
		
		yearMon=y*100+mon;
		
		///*
		checkNDeleteEntries();
		writeEntries();
		//showEntries();
		//*/
		
		/*
		e=0;
		try {
			dos=new DataOutputStream(new FileOutputStream("file"));
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.04WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		try {
			encrypt("Nivedita");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
	}
	
	private void checkNDeleteEntries(){
		
		vec=new Vector<String>();
		
		e=0;
		String a=null;
		try {
			dis=new DataInputStream(new FileInputStream("file"));
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.00WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		try {
			a=decrypt();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.01WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		TimeTableTokenizer ttt=new TimeTableTokenizer(a);
		for(int i=0;i<ttt.countTokens();i++){
			
			if(i==0){
				if(!ttt.tokenAt(i).equalsIgnoreCase("Nivedita")){
					JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.02WMID\n"
							+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				continue;
			}
			
			int storedYMD=getYearMonthDate(ttt.tokenAt(i));
			
			int timeBW=(storedYMD)%100;//gets stored month
			timeBW=d-timeBW;
			timeBW=timeBW+((mon-((storedYMD/100)%100)+12)%12)*30;
			
			if(timeBW<=30)
				vec.add(ttt.tokenAt(i));
		}
		
		try {
			dis.close();
		} catch (IOException e2) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.03WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	private void writeEntries(){
		e=0;
		try {
			dos=new DataOutputStream(new FileOutputStream("file"));
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.04WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		try {
			encrypt("Nivedita");
		} catch (IOException e2) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.05WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		for(int i=0;i<vec.size();i++){
			try {
				encrypt("/"+vec.get(i));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.06WMID\n"
						+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		todays=todays+Integer.toString(yearMon);
		if(Integer.toString(d).length()==1)todays=todays+"0";todays=todays+Integer.toString(d);
		if(Integer.toString(h).length()==1)todays=todays+"0";todays=todays+Integer.toString(h);
		if(Integer.toString(min).length()==1)todays=todays+"0";todays=todays+Integer.toString(min);
		if(Integer.toString(sec).length()==1)todays=todays+"0";todays=todays+Integer.toString(sec);
		todays=todays+"\n";
		
		writeIPAddNMacAdd();
		
		try {
			encrypt("/");
			encrypt(todays);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.07WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		try {
			dos.close();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.08WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
    private void writeIPAddNMacAdd() {
    	String add=null;
    	try {
    		add=Inet4Address.getLocalHost().getHostAddress();
			todays=todays+add+"\n";
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.09WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
    	
    	try{
            InetAddress inet = InetAddress.getByName(add);
            boolean status = inet.isReachable(5000);
            if (status){
                todays=todays+inet.getCanonicalHostName()+"\n";///////////////////name of computer "ARYAN"
            }
            else{
    			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.10WMID\n"
    					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
    			System.exit(0);
            }

    	} catch (UnknownHostException e){
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.11WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
        }
        catch (IOException e){
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.12WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
        }
    	
    	
    	
    	InetAddress address=null;
		try {
			address = InetAddress.getByName(add);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.13WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		NetworkInterface ni=null;
    	try {
			ni = NetworkInterface.getByInetAddress(address);
		} catch (SocketException e) {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.14WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
    	if (ni != null) {
            byte[] mac=null;
			try {
				mac = ni.getHardwareAddress();
			} catch (SocketException e) {
				JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.15WMID\n"
						+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
            if (mac != null) {
                /*
                 * Extract each array of mac address and convert it 
                 * to hexa with the following format 
                 * 08-00-27-DC-4A-9E.
                 */
            	StringBuilder sb=new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                todays=todays+sb.toString()+"\n";
            }
            else {
    			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.16WMID\n"
    					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
    			System.exit(0);
            }
        }
    	else {
			JOptionPane.showMessageDialog (null, "An important file in this software cannot be accessed properly.17WMID\n"
					+ "Start the program again or\nContact the the developer.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
        }
		
    	String un=System.getProperty("user.name");
    	for(int i=0;i<un.length();i++){
    		if(un.charAt(i)=='/')
    			todays=todays+"\\";
    		else todays=todays+un.charAt(i);
        }
    	todays=todays+"\n\n";
	}

	private int getYearMonthDate(String tokenAt) {
		tokenAt=tokenAt.substring(0,8);
		return Integer.parseInt(tokenAt);
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
    
    private void encrypt(String a)throws IOException{
        for(int i=0;i<a.length();i++){
            int x=(int)a.charAt(i)+(int)encoder.charAt(e);
            dos.write(x);
            e=(e+1)%encoder.length();
        }
    }
    
    public void showEntries(){
    	e=0;
    	try {
			dis=new DataInputStream(new FileInputStream("file"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String a=null;
    	try {
			a=decrypt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	TimeTableTokenizer ttt=new TimeTableTokenizer(a);
    	for(int i=0;i<ttt.countTokens();i++){
    		if(i==0){
    			continue;
    		}
    		System.out.println(ttt.tokenAt(i));
    	}
    	try {
			dis.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
