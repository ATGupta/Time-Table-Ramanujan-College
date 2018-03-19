package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;


public class MainClass {
    public static void main(String[] args) throws IOException{
    	
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        
        checkForMainFiles();
        
        int h=(int)width,v=(int)height;
        new HomePage(h,v);
    }
    
    private static void checkForMainFiles(){
        
        if(new File("depPassword").exists()==false){
        	noticeNExit(1);
        }
        else if(new File("deps").exists()==false){
        	noticeNExit(2);
        }
        else if(new File("file").exists()==false){
        	noticeNExit(3);
        }
        else if(new File("noOfSlots").exists()==false){
        	noticeNExit(4);
        }
        else if(new File("pass").exists()==false){
        	noticeNExit(5);
        }
        else if(new File("semDetails").exists()==false){
        	noticeNExit(6);
        }
        else if(new File("startTime").exists()==false){
        	noticeNExit(7);
        }
    }
    
    private static void noticeNExit(int i){
    	
    	JOptionPane.showMessageDialog (null, i+"The software has been altered fatally.\n", "Fatal Error", JOptionPane.ERROR_MESSAGE);
    	System.exit(0);
    }
}