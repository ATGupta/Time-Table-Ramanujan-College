package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.MainOptions;
import misc.SetArray;


public class FreeRooms extends JFrame{
    
    private int horizontal,vertical,e,nos;
    private JPanel panel;
    private JButton back;
    private String encoder;
    private DataInputStream dis;
    private JScrollPane scroll;
    private Vector<String> rooms,depOfRooms,posOfRooms;
    private JComboBox roomBox;
    
    public FreeRooms(int h, int v) {
        horizontal=h;vertical=v;
        encoder="Aryan";
        setSize(horizontal,vertical);
        
        getContentPane().setBackground(new Color(0,0,0));;
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        initialize();
        setComponents();
        actionListeners();
        
        setWindowIcon();
        
        setExtendedState(6);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setWindowIcon(){
    	ImageIcon image=new ImageIcon("icon");
    	setIconImage(image.getImage());
    }
    
    private void initialize(){
        panel=new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        
        back=new JButton("Back");
        back.setForeground(new Color(0,0,0));
        back.setBackground(new Color(255,255,255));
    }
    
    private void setComponents(){
        
    	getContentPane().removeAll();
    	panel.removeAll();
    	setVisible(false);
    	
        getNoOfSlots();
        
        try {
            dis=new DataInputStream(new FileInputStream("deps"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Vector<String> deps=new Vector<String>();
        String b="";
        for(int o=0;o<a.length();o++){
            if(a.charAt(o)=='\n'){
                deps.add(b);
                b="";
            }
            else b=b+a.charAt(o);
        }
        
        rooms=new Vector<String>();
        depOfRooms=new Vector<String>();
        posOfRooms=new Vector<String>();
        for(int o=0;o<deps.size();o++){
            try {
                dis=new DataInputStream(new FileInputStream((deps.get(o))+"\\room"));
            } catch (FileNotFoundException ex) {
                continue;
            }
            
            e=0;
            try {
                a=decrypt();
            } catch (IOException ex) {
                Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int count=0;
            for(int j=0;j<a.length();j++){
                if(a.charAt(j)=='\n'){
                	count++;
                    int t=0;
                    for(int k=0;k<rooms.size();k++){
                        if(rooms.get(k).equalsIgnoreCase(b)){
                            t++;
                            b="";
                            break;
                        }
                    }
                    if(t==0){
                        rooms.add(b);
                        depOfRooms.add(deps.get(o));
                        posOfRooms.add(Integer.toString(count-1));
                        b="";
                    }
                }
                else b=b+a.charAt(j);
            }
        }
        
        boolean room[][][]=new boolean[rooms.size()][7][nos];
        for(int r=0;r<rooms.size();r++){
            for(int d=0;d<7;d++){
                for(int p=0;p<nos;p++){
                    room[r][d][p]=true;
                }
            }
        }
        for(int o=0;o<deps.size();o++){
            
            try {
                dis=new DataInputStream(new FileInputStream((deps.get(o)+"\\class")));
            } catch (FileNotFoundException ex) {
                continue;
            }
            
            e=0;
            try {
                a=decrypt();
            } catch (IOException ex) {
                Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Vector<String> classes=new Vector<String>();
            b="";
            for(int i=0;i<a.length();i++){
                if(a.charAt(i)=='\n'){
                    classes.add(b);
                    b="";
                }
                else b=b+a.charAt(i);
            }
            
            for(int i=0;i<classes.size();i++){
                
                try {
                    dis=new DataInputStream(new FileInputStream((deps.get(o)+"\\"+classes.get(i))));
                } catch (FileNotFoundException ex) {
                    continue;
                }
                
                e=0;
                try {
                    a=decrypt();
                } catch (IOException ex) {
                    Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int n=0,d=0,p=0;
                b="";
                for(int j=0;j<a.length();j++){
                    if(a.charAt(j)=='\n'){
                        if(n%3==2){
                            if(rooms.contains(b)){
                                room[rooms.indexOf(b)][d][p]=false;
                            }
                            
                            p++;
                            if(p==nos){
                                p=0;d++;
                            }
                        }
                        n++;
                        b="";
                    }
                    else b=b+a.charAt(j);
                }
            }
        }
        
        JTextArea texts[][]=new JTextArea[7+1][nos+1];
        String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        int start=timeToInteger(Integer.toString(getStartTime()));
        int timeO1=55;        
        
        for(int d=0;d<8;d++){
            for(int p=0;p<nos+1;p++){
                texts[d][p]=new JTextArea();
                
                texts[d][p].setOpaque(false);
                texts[d][p].setPreferredSize(new Dimension(100,100));
                texts[d][p].setMinimumSize(new Dimension(100,100));
                texts[d][p].setMaximumSize(new Dimension(100,100));
                texts[d][p].setLineWrap(true);
                texts[d][p].setWrapStyleWord(true);
                texts[d][p].setEditable(false);
                texts[d][p].setOpaque(false);
                texts[d][p].setForeground(new Color(255,255,255));
                
                texts[d][p].addKeyListener(new KeyListener(){
        			public void keyPressed(KeyEvent e) {
        				int ho=scroll.getHorizontalScrollBar().getValue();
        				int vi=scroll.getVerticalScrollBar().getValue();
        				if(e.getKeyCode()==e.VK_LEFT)
        						scroll.getHorizontalScrollBar().setValue(ho-10);
        				
        				if(e.getKeyCode()==e.VK_RIGHT)
        						scroll.getHorizontalScrollBar().setValue(ho+10);
        				
        				if(e.getKeyCode()==e.VK_UP){
        						scroll.getVerticalScrollBar().setValue(vi-10);
        				}
        				if(e.getKeyCode()==e.VK_DOWN){
        						scroll.getVerticalScrollBar().setValue(vi+10);
        				}
        			}
        			public void keyReleased(KeyEvent arg0) {}
        			public void keyTyped(KeyEvent arg0) {}
                });
            }
        }
        for(int d=0;d<8;d++){
            if(d==0){
                for(int p=0;p<nos+1;p++){
                	if(p==0){
                		texts[d][p].setText("Periods "+(char)(8594)+"\nDays "+(char)(8595));
                		texts[d][p].setFont(new Font("Times New Roman",1,20));
                	}
                	else{
                		texts[d][p].setText("Period "+Integer.toString(p)+"\n"+integerToTime(start)+" - "+integerToTime(start+timeO1));
                		texts[d][p].setFont(new Font("Times New Roman",1,16));
                	}
                    start=start+timeO1;
                }
                continue;
            }
            
            for(int p=0;p<nos+1;p++){
                
                if(p==0 && d>0){
                    texts[d][p].setText(days[d-1]);
                    texts[d][p].setFont(new Font("Times New Roman",1,16));
                    
                    continue;
                }
                
                for(int r=0;r<rooms.size();r++){
                    if(room[r][d-1][p-1]){
                        texts[d][p].append(rooms.get(r)+"; ");
                        texts[d][p].setFont(new Font("Times New Roman",0,12));
                    }
                }
            }
        }
        
        JLabel l=new JLabel("TABLE OF FREE ROOMS");
        l.setOpaque(false);
        l.setForeground(new Color(255,255,255));
        l.setFont(new Font("Times New Roman",1,25));
        panel.add(l);
        l.setAlignmentX(l.CENTER_ALIGNMENT);
        
        for(int d=0;d<8;d++){
            
            JPanel pane=new JPanel();
            pane.setOpaque(false);
            pane.setLayout(new FlowLayout());
            
            for(int p=0;p<nos+1;p++){
                JScrollPane s=new JScrollPane(texts[d][p]);
                s.setOpaque(false);
                s.getViewport().setOpaque(false);
                pane.add(s);
            }
            panel.add(pane);
        }
        
        scroll=new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getHorizontalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setOpaque(false);
        add(scroll);
        
        String arr[]=new String[rooms.size()+1];
        arr[0]="Free Rooms";
        for(int i=0;i<rooms.size();i++){
        	arr[i+1]=rooms.get(i);
        }
        roomBox=new JComboBox(arr);
        
        JPanel p=new JPanel();p.setLayout(new FlowLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(horizontal,100));
        p.add(back);
        p.add(roomBox);
        back.setAlignmentX(back.CENTER_ALIGNMENT);
        add(p);
        
        setVisible(true);
    }
    
    private void getNoOfSlots(){
        try {
            dis=new DataInputStream(new FileInputStream("noOfSlots"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        nos=Integer.parseInt(a);
    }
    
    private int timeToInteger(String time){
        int a=Integer.parseInt(time);
        int t=(a/100)*60+(a%100);
        return t;
    }
    
    private String integerToTime(int time){
        int t=(time/60)*100+time%60;
        String x=Integer.toString(t);
        for(int i=x.length();i<4;i++){
            x="0"+x;
        }
        return x;
    }
    
    private int getStartTime(){
        try {
            dis=new DataInputStream(new FileInputStream("startTime"));
        } catch (FileNotFoundException ex) {
        	JOptionPane.showMessageDialog(getContentPane(), "Starting time not set yet.", "Error", JOptionPane.ERROR_MESSAGE);
        	System.exit(0);
        }
        
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(a.length()==0){
        	JOptionPane.showMessageDialog(getContentPane(), "Starting time not set yet.", "Error", JOptionPane.ERROR_MESSAGE);
        	System.exit(0);
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
    
    private void actionListeners(){
    	
    	back.setFocusable(true);
    	back.requestFocus();
    	back.requestFocus(true);
    	
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new MainOptions(horizontal,vertical);
                dispose();
            }
        });
        
        back.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				int ho=scroll.getHorizontalScrollBar().getValue();
				int vi=scroll.getVerticalScrollBar().getValue();
				if(e.getKeyCode()==e.VK_LEFT)
						scroll.getHorizontalScrollBar().setValue(ho-10);
				
				if(e.getKeyCode()==e.VK_RIGHT)
						scroll.getHorizontalScrollBar().setValue(ho+10);
				
				if(e.getKeyCode()==e.VK_UP){
						scroll.getVerticalScrollBar().setValue(vi-10);
				}
				if(e.getKeyCode()==e.VK_DOWN){
						scroll.getVerticalScrollBar().setValue(vi+10);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
        });
        
        roomBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String a=(String)(roomBox.getSelectedItem());
				if(a.equalsIgnoreCase("free rooms")){
					setComponents();return;
				}
				displayRoomTT(a);
			}
        });
        
        roomBox.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				int ho=scroll.getHorizontalScrollBar().getValue();
				int vi=scroll.getVerticalScrollBar().getValue();
				if(e.getKeyCode()==e.VK_LEFT)
						scroll.getHorizontalScrollBar().setValue(ho-10);
				
				if(e.getKeyCode()==e.VK_RIGHT)
						scroll.getHorizontalScrollBar().setValue(ho+10);
				
				if(e.getKeyCode()==e.VK_UP){
						scroll.getVerticalScrollBar().setValue(vi-10);
				}
				if(e.getKeyCode()==e.VK_DOWN){
						scroll.getVerticalScrollBar().setValue(vi+10);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
        });
    }
    
    private void displayRoomTT(String a){
    	
    	getContentPane().removeAll();
    	panel.removeAll();
    	setVisible(false);
    	
    	int x=rooms.indexOf(a);
    	SetArray sa=new SetArray(depOfRooms.get(x));
    	String arr[][][]=sa.set(SetArray.CHECK_ROOM_ROOM_ARRAY);
    	x=Integer.parseInt(posOfRooms.get(x));
    	
    	JTextArea texts[][]=new JTextArea[7+1][nos+1];
        String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        int start=timeToInteger(Integer.toString(getStartTime()));
        int timeO1=55;
        
        for(int d=0;d<8;d++){
            for(int p=0;p<nos+1;p++){
                texts[d][p]=new JTextArea();
                
                texts[d][p].setOpaque(false);
                texts[d][p].setPreferredSize(new Dimension(100,100));
                texts[d][p].setMinimumSize(new Dimension(100,100));
                texts[d][p].setMaximumSize(new Dimension(100,100));
                texts[d][p].setLineWrap(true);
                texts[d][p].setWrapStyleWord(true);
                texts[d][p].setEditable(false);
                texts[d][p].setOpaque(false);
                texts[d][p].setForeground(new Color(255,255,255));
                
                texts[d][p].addKeyListener(new KeyListener(){
        			public void keyPressed(KeyEvent e) {
        				int ho=scroll.getHorizontalScrollBar().getValue();
        				int vi=scroll.getVerticalScrollBar().getValue();
        				if(e.getKeyCode()==e.VK_LEFT)
        						scroll.getHorizontalScrollBar().setValue(ho-10);
        				
        				if(e.getKeyCode()==e.VK_RIGHT)
        						scroll.getHorizontalScrollBar().setValue(ho+10);
        				
        				if(e.getKeyCode()==e.VK_UP){
        						scroll.getVerticalScrollBar().setValue(vi-10);
        				}
        				if(e.getKeyCode()==e.VK_DOWN){
        						scroll.getVerticalScrollBar().setValue(vi+10);
        				}
        			}
        			public void keyReleased(KeyEvent arg0) {}
        			public void keyTyped(KeyEvent arg0) {}
                });
            }
        }
        for(int d=0;d<8;d++){
            if(d==0){
                for(int p=0;p<nos+1;p++){
                	if(p==0){
                		texts[d][p].setText("Periods "+(char)(8594)+"\nDays "+(char)(8595));
                		texts[d][p].setFont(new Font("Times New Roman",1,20));
                	}
                	else{
                		texts[d][p].setText("Period "+Integer.toString(p)+"\n"+integerToTime(start)+" - "+integerToTime(start+timeO1));
                		texts[d][p].setFont(new Font("Times New Roman",1,16));
                	}
                    start=start+timeO1;
                }
                continue;
            }
            
            for(int p=0;p<nos+1;p++){
                
                if(p==0 && d>0){
                    texts[d][p].setText(days[d-1]);
                    texts[d][p].setFont(new Font("Times New Roman",1,16));
                    
                    continue;
                }
                texts[d][p].setText(arr[x][d-1][p-1]);
                texts[d][p].setFont(new Font("Times New Roman",0,12));
            }
        }
        
        JLabel l=new JLabel(a);
        l.setOpaque(false);
        l.setForeground(new Color(255,255,255));
        l.setFont(new Font("Times New Roman",1,25));
        panel.add(l);
        l.setAlignmentX(l.CENTER_ALIGNMENT);
        
        for(int d=0;d<8;d++){
            
            JPanel pane=new JPanel();
            pane.setOpaque(false);
            pane.setLayout(new FlowLayout());
            
            for(int p=0;p<nos+1;p++){
                JScrollPane s=new JScrollPane(texts[d][p]);
                s.setOpaque(false);
                s.getViewport().setOpaque(false);
                pane.add(s);
            }
            panel.add(pane);
        }
        
        scroll=new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getHorizontalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setOpaque(false);
        add(scroll);
        
        JPanel p=new JPanel();p.setLayout(new FlowLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(horizontal,100));
        p.add(back);
        p.add(roomBox);
        back.setAlignmentX(back.CENTER_ALIGNMENT);
        add(p);
        setVisible(true);
    }
}