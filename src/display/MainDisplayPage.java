package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.MainOptions;
import misc.BackHelper;
import misc.SetArray;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import edit.EditTimeTable;

public class MainDisplayPage extends JFrame {
    
    private int horizontal,vertical,e,nos,teacherSelect,classSelect,s;
    private JComboBox depBox,teacherBox,classBox;
    private DataInputStream dis;
    private String arr[],encoder,department,teacher[][][],room[][][],paper[][][],t,data[][],periods[],Class[][][];
    private Vector<String> depVec,teacherVec,classVec;
    private JButton depEnter,teacherEnter,classEnter,back,printAsPDF,tButton,cButton;
    private JPanel panel;
    private JTable table;
    private ImageIcon backImage;
    private BackHelper backHelp;
    private JScrollPane scroll;
    
    public MainDisplayPage(int h, int v) {
        horizontal=h;vertical=v;
        encoder="Aryan";
        setSize(horizontal,vertical);
        
        backHelp=new BackHelper();
        backHelp.push("MainDisplayPage","0");
        
        getContentPane().setBackground(new Color(0,0,0));
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        initialize();
        setComponents();
        actionListeners();
        
        setWindowIcon();
        
        panel.setOpaque(false);
        
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setWindowIcon(){
    	ImageIcon image=new ImageIcon("icon");
    	setIconImage(image.getImage());
    }
    
    private void initialize(){
        initializeDep();
        
        depEnter=new JButton("Enter");
        depEnter.setBackground(new Color(255,255,255));
        
        panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        back=new JButton("Back");
        back.setBackground(new Color(255,255,255));
        
        printAsPDF=new JButton("Print As PDF");
        printAsPDF.setBackground(new Color(255,255,255));
        printAsPDF.setVisible(false);
    }
    
    private void initializeDep(){
        depVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream("deps"));
        } catch (FileNotFoundException ex) {
            arr=new String[1];arr[0]="Departments";
            depBox=new JComboBox(arr);
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                depVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
        arr=new String[depVec.size()+1];
        arr[0]="Departments";
        for(int i=0;i<depVec.size();i++){
            arr[i+1]=depVec.get(i);
        }
        depBox=new JComboBox(arr);
        depBox.setMaximumSize(new Dimension(300,25));
        depBox.setMinimumSize(new Dimension(300,25));
        depBox.setPreferredSize(new Dimension(300,25));
    }
    
    private void setComponents(){
    	panel.add(Box.createRigidArea(new Dimension(20,vertical*35/100)));
        panel.add(depBox);
        depBox.setAlignmentX(depBox.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(20,10)));
        panel.add(depEnter);
        depEnter.setAlignmentX(depEnter.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        
        scroll=new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getHorizontalScrollBar().setOpaque(false);
        add(scroll);
        
        JPanel p4=new JPanel();
        p4.setLayout(new FlowLayout());
        p4.setMaximumSize(new Dimension(500,100));
        p4.add(back);
        p4.add(printAsPDF);
        p4.setOpaque(false);
        add(p4);
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
        depEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                department=(String)depBox.getSelectedItem();
                if(!department.equalsIgnoreCase("Departments")){
                    depBox.setVisible(false);
                    depEnter.setVisible(false);
                    
                    setTeacher();
                    setClass();
                    
                    panel.setVisible(false);
                    
                    JPanel p=new JPanel();
                    p.setOpaque(false);
                    p.add(tButton);p.add(teacherBox);p.add(teacherEnter);
                    p.setMaximumSize(new Dimension(horizontal*2/3,50));
                    panel.add(p);
                    
                    panel.add(Box.createRigidArea(new Dimension(20,10)));
                    
                    JPanel p1=new JPanel();
                    p1.setOpaque(false);
                    p1.add(cButton);p1.add(classBox);p1.add(classEnter);
                    p1.setMaximumSize(new Dimension(horizontal*2/3,50));
                    panel.add(p1);
                    
                    panel.setOpaque(false);
                    panel.setVisible(true);
                    
                    secondaryActionListeners();
                    
                    backHelp.push(department, "1");
                }
                else JOptionPane.showMessageDialog (getContentPane(), "Select a valid Department name..", "Enter Correctly", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	if(backHelp.tail.positionDescription.equals("0")){
            		dispose();
            		new MainOptions(horizontal,vertical);
            	}
            	else if(backHelp.tail.positionDescription.equals("1")){
            		dispose();
            		new MainDisplayPage(horizontal,vertical);
            	}
            	else if(backHelp.tail.positionDescription.equals("2")){
            		backHelp.pop();
            		
            		getContentPane().removeAll();
            		getContentPane().setVisible(false);
            		
            		initialize();
                    setComponents();
                    actionListeners();
                    
                    depBox.setSelectedItem(backHelp.tail.dataAtPosition);
                    depEnter.doClick();
                    
                    getContentPane().setVisible(true);
            	}
            }
        });
        
        printAsPDF.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                /*try {
                    printExcel();
                } catch (IOException ex) {
                    Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                if(teacherSelect==1)
                    printPDF(t);
                else if(classSelect==1)
                    printPDF(classVec.get(s));
            }
        });
        
        /*
         * this back and printAsPdf buttons have action listeners
         * for KeyListener
         * for scrollPane
         * because focus is gained by these two buttons only
         * or no one throughout the frame
         * 
         * but
         * 
         * this is done by requesting focus for back button
         * as the teacher or class is entered
         * 
         */
        
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
        
        printAsPDF.addKeyListener(new KeyListener(){
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
    
    private void secondaryActionListeners(){
    	
    	tButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				teacherEnter.setVisible(true);
				teacherBox.setVisible(true);
				
				classEnter.setVisible(false);
				classBox.setVisible(false);
			}
    	});
    	
    	cButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				teacherEnter.setVisible(false);
				teacherBox.setVisible(false);
				
				classEnter.setVisible(true);
				classBox.setVisible(true);
			}
    	});
        
        teacherEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(((String)(teacherBox.getSelectedItem())).equals("Teachers")==false){
                    classSelect=0;teacherSelect=1;
                    
                    SetArray st=new SetArray(department);
                    
                    paper=st.set(SetArray.DISPLAY_TEACHER_PAPER_ARRAY);
                    teacher=st.set(SetArray.DISPLAY_TEACHER_TEACHER_ARRAY);
                    room=st.set(SetArray.DISPLAY_TEACHER_ROOM_ARRAY);
                    Class=st.set(SetArray.DISPLAY_TEACHER_CLASS_ARRAY);
                    
                    displayTeacher();
                    printAsPDF.setVisible(true);
                    
                    backHelp.push(("Teacher "+t), "2");
                    
                    back.requestFocus();
                }
                else
                    JOptionPane.showMessageDialog (getContentPane(), "Select a valid Teacher name..", "Enter Correctly", JOptionPane.ERROR_MESSAGE);
                
            }
        });
        
        classEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(((String)(classBox.getSelectedItem())).compareTo("Classes")!=0){
                    classSelect=1;teacherSelect=0;
                    
                    SetArray st=new SetArray(department);
                    
                    paper=st.set(SetArray.DISPLAY_CLASS_PAPER_ARRAY);
                    teacher=st.set(SetArray.DISPLAY_CLASS_TEACHER_ARRAY);
                    room=st.set(SetArray.DISPLAY_CLASS_ROOM_ARRAY);
                    
                    displayClass();
                    
                    printAsPDF.setVisible(true);
                    
                    backHelp.push(("Class "+classVec.get(s)), "2");
                    
                    back.requestFocus();
                }
                else
                    JOptionPane.showMessageDialog (getContentPane(), "Select a valid Class name..", "Enter Correctly", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void displayTeacher(){
    	
    	nos=getNoOfSlots();
    	
        panel.removeAll();
        panel.setVisible(false);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        t = ((String)(teacherBox.getSelectedItem()));
        
        setDisplayLabels(t);
        
        data = new String[7][nos+1];
        for(int r=0;r<7;r++){
            for(int c=0;c<nos+1;c++){
                data[r][c]="";
            }
        }
        String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        periods=new String[nos+1];
        
        periods[0]="";
        for(int i=1;i<nos+1;i++){
            periods[i]="Period "+Integer.toString(i);
        }
        //System.out.println(t+"\t"+nos);
        for(int d=0;d<7;d++){
			data[d][0]=days[d];
			for(int p=0;p<nos;p++){
				for(int i=0;i<teacherVec.size();i++){
					if(t.equals(teacher[i][d][p])){
						data[d][p+1]=paper[i][d][p]+"\n"
								+Class[i][d][p]+"\n"
								+room[i][d][p];
				
					}
				}
			}
        }
        
        /*
        table=new JTable(data,periods);
        table.setRowHeight(100);
        table.setPreferredScrollableViewportSize(new Dimension(horizontal,vertical*2/3));
        table.setFillsViewportHeight(true);
        JScrollPane s=new JScrollPane(table);
        panel.add(s);
        */
        
        int start=timeToInteger(Integer.toString(getStartTime()));
        int timeO1=55;
        
        for(int d=-1;d<7;d++){
            JPanel pane=new JPanel();
            pane.setOpaque(false);//---------------
            pane.setLayout(new FlowLayout());
            
            if(d==-1){
                for(int p=0;p<nos+1;p++){
                    JPanel p0=new JPanel();
                    p0.setOpaque(false);//---------------****
                    p0.setLayout(new BoxLayout(p0,BoxLayout.Y_AXIS));
                    p0.setMinimumSize(new Dimension(150,60));
                    p0.setMaximumSize(new Dimension(150,60));
                    p0.setPreferredSize(new Dimension(150,60));
                    
                    JLabel l1=new JLabel(periods[p]);
                    l1.setForeground(new Color(255,255,255));
                    p0.add(l1);
                    
                    l1.setAlignmentX(l1.CENTER_ALIGNMENT);
                    
                    if(p>0){
                        JLabel l2=new JLabel(integerToTime(start)+" - "+integerToTime(start+timeO1));
                        l2.setOpaque(false);
                        l2.setForeground(new Color(255,255,255));
                        
                        start=start+timeO1;
                        
                        p0.add(l2);
                        l2.setAlignmentX(l2.CENTER_ALIGNMENT);
                    }
                    
                    pane.add(p0);
                }
                panel.add(pane);
                continue;
            }
            
            for(int p=-1;p<nos;p++){
                
                if(p==-1){
                    JPanel p0=new JPanel();
                    p0.setOpaque(false);
                    p0.setLayout(new BoxLayout(p0,BoxLayout.Y_AXIS));
                    p0.setMinimumSize(new Dimension(150,60));
                    p0.setMaximumSize(new Dimension(150,60));
                    p0.setPreferredSize(new Dimension(150,60));
                    
                    JLabel l1=new JLabel(days[d]);
                    l1.setForeground(new Color(255,255,255));
                    l1.setOpaque(false);
                    
                    p0.add(l1);
                    l1.setAlignmentX(l1.CENTER_ALIGNMENT);
                    
                    pane.add(p0);
                    
                    continue;
                }
                
                JPanel p0=new JPanel();
                p0.setOpaque(false);
                p0.setLayout(new BoxLayout(p0,BoxLayout.Y_AXIS));
                p0.setMinimumSize(new Dimension(150,60));
                p0.setMaximumSize(new Dimension(150,60));
                p0.setPreferredSize(new Dimension(150,60));

                for(int s=0;s<teacherVec.size();s++)
                if(t.compareTo(teacher[s][d][p])==0){
                    JLabel l1=new JLabel();
                    JLabel l2=new JLabel();
                    JLabel l3=new JLabel();
                    
                    l1.setText(paper[s][d][p]);
                    l2.setText(Class[s][d][p]);
                    l3.setText(room[s][d][p]);
                    
                    l1.setOpaque(false);
                    l2.setOpaque(false);
                    l3.setOpaque(false);
                    
                    l1.setForeground(new Color(255,255,255));
                    l2.setForeground(new Color(255,255,255));
                    l3.setForeground(new Color(255,255,255));
                    
                    
                    p0.add(l1);
                    l1.setAlignmentX(l1.CENTER_ALIGNMENT);
                    p0.add(l2);
                    l2.setAlignmentX(l2.CENTER_ALIGNMENT);
                    p0.add(l3);
                    l3.setAlignmentX(l3.CENTER_ALIGNMENT);
                }
                
                p0.setBorder(BorderFactory.createLineBorder(new Color(255,255,255)));
                pane.add(p0);
            }
            panel.add(pane);
        }
                    
        panel.setOpaque(false);
        panel.setVisible(true);
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
            Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(FreeRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Integer.parseInt(a);
    }
    
    private void displayClass(){
    	nos=getNoOfSlots();
    	
        panel.removeAll();
        panel.setVisible(false);
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        s = classVec.indexOf(((String)(classBox.getSelectedItem())));
        
        setDisplayLabels(classVec.get(s));
        
        data = new String[7][nos+1];
        for(int r=0;r<7;r++){
            for(int c=0;c<nos+1;c++){
                data[r][c]="";
            }
        }
        String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        periods=new String[nos+1];
        
        periods[0]="";
        for(int i=1;i<nos+1;i++){
            periods[i]="Period "+Integer.toString(i);
        }
        
        for(int d=0;d<7;d++){
            data[d][0]=days[d];
            for(int p=0;p<nos;p++){
                if(!(teacher[s][d][p].equals("") && paper[s][d][p].equals("") && room[s][d][p].equals("")))
                    data[d][p+1]=paper[s][d][p]+"\n"+teacher[s][d][p]+"\n"+room[s][d][p];
            }
        }
        
        /*
        table=new JTable(data,periods);
        table.setRowHeight(100);
        table.setPreferredScrollableViewportSize(new Dimension(horizontal,vertical*2/3));
        table.setFillsViewportHeight(true);
        JScrollPane s1=new JScrollPane(table);
        panel.add(s1);
        */
        
        int start=timeToInteger(Integer.toString(getStartTime()));
        int timeO1=55;
        for(int d=-1;d<7;d++){
            JPanel pane=new JPanel();
            pane.setOpaque(false);
            pane.setLayout(new FlowLayout());
            
            if(d==-1){
                for(int p=0;p<nos+1;p++){
                    JPanel p0=new JPanel();
                    p0.setOpaque(false);
                    p0.setLayout(new BoxLayout(p0,BoxLayout.Y_AXIS));
                    p0.setMinimumSize(new Dimension(150,60));
                    p0.setMaximumSize(new Dimension(150,60));
                    p0.setPreferredSize(new Dimension(150,60));
                    JLabel l1=new JLabel(periods[p]);
                    l1.setOpaque(false);
                    l1.setForeground(new Color(255,255,255));
                    p0.add(l1);
                    l1.setAlignmentX(l1.CENTER_ALIGNMENT);
                    if(p>0){
                        JLabel l2=new JLabel(integerToTime(start)+" - "+integerToTime(start+timeO1));
                        l2.setOpaque(false);
                        l2.setForeground(new Color(255,255,255));
                        start=start+timeO1;
                        p0.add(l2);
                        l2.setAlignmentX(l2.CENTER_ALIGNMENT);
                    }
                    
                    pane.add(p0);
                }
                panel.add(pane);
                continue;
            }
            
            for(int p=-1;p<nos;p++){
                
                if(p==-1){
                    JPanel p0=new JPanel();
                    p0.setOpaque(false);
                    p0.setLayout(new BoxLayout(p0,BoxLayout.Y_AXIS));
                    p0.setMinimumSize(new Dimension(150,60));
                    p0.setMaximumSize(new Dimension(150,60));
                    p0.setPreferredSize(new Dimension(150,60));
                    JLabel l1=new JLabel(days[d]);
                    l1.setOpaque(false);
                    l1.setForeground(new Color(255,255,255));
                    p0.add(l1);
                    l1.setAlignmentX(l1.CENTER_ALIGNMENT);
                    
                    pane.add(p0);
                    
                    continue;
                }
                
                JPanel p0=new JPanel();
                p0.setOpaque(false);
                p0.setLayout(new BoxLayout(p0,BoxLayout.Y_AXIS));
                p0.setMinimumSize(new Dimension(150,60));
                p0.setMaximumSize(new Dimension(150,60));
                p0.setPreferredSize(new Dimension(150,60));
                JLabel l1=new JLabel(paper[s][d][p]);
                JLabel l2=new JLabel(teacher[s][d][p]);
                JLabel l3=new JLabel(room[s][d][p]);

                l1.setOpaque(false);
                l2.setOpaque(false);
                l3.setOpaque(false);
                l1.setForeground(new Color(255,255,255));
                l2.setForeground(new Color(255,255,255));
                l3.setForeground(new Color(255,255,255));
                
                p0.add(l1);
                l1.setAlignmentX(l1.CENTER_ALIGNMENT);
                p0.add(l2);
                l2.setAlignmentX(l2.CENTER_ALIGNMENT);
                p0.add(l3);
                l3.setAlignmentX(l3.CENTER_ALIGNMENT);
                p0.setBorder(BorderFactory.createLineBorder(new Color(255,255,255)));
                pane.add(p0);
            }
            panel.add(pane);
        }
        panel.setVisible(true);
    }
    
    private void setTeacher(){
        teacherVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(department+"/teacher"));
        } catch (FileNotFoundException ex) {
            arr=new String[1];arr[0]="Teachers";
            
            teacherBox=new JComboBox(arr);
            teacherBox.setMaximumSize(new Dimension(300,25));
            teacherBox.setMinimumSize(new Dimension(300,25));
            teacherBox.setPreferredSize(new Dimension(300,25));
            teacherBox.setVisible(false);
            
            tButton=new JButton("Teachers' Time Table");
            tButton.setBackground(new Color(255,255,255));
            tButton.setMaximumSize(new Dimension(300,25));
            tButton.setMinimumSize(new Dimension(300,25));
            tButton.setPreferredSize(new Dimension(300,25));
            
            teacherEnter=new JButton("Enter");
            teacherEnter.setBackground(new Color(255,255,255));
            teacherEnter.setVisible(false);
            
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                teacherVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
        arr=new String[teacherVec.size()+1];
        arr[0]="Teachers";
        for(int i=0;i<teacherVec.size();i++){
            arr[i+1]=teacherVec.get(i);
        }
        
        teacherBox=new JComboBox(arr);
        teacherBox.setMaximumSize(new Dimension(300,25));
        teacherBox.setMinimumSize(new Dimension(300,25));
        teacherBox.setPreferredSize(new Dimension(300,25));
        teacherBox.setVisible(false);
        
        tButton=new JButton("Teachers' Time Table");
        tButton.setBackground(new Color(255,255,255));
        tButton.setMaximumSize(new Dimension(300,25));
        tButton.setMinimumSize(new Dimension(300,25));
        tButton.setPreferredSize(new Dimension(300,25));
        
        teacherEnter=new JButton("Enter");
        teacherEnter.setBackground(new Color(255,255,255));
        teacherEnter.setVisible(false);
    }
    
    private void setClass(){
        classVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(department+"/class"));
        } catch (FileNotFoundException ex) {
            arr=new String[1];arr[0]="Classes";
            
            classBox=new JComboBox(arr);
            classBox.setMaximumSize(new Dimension(300,25));
            classBox.setMinimumSize(new Dimension(300,25));
            classBox.setPreferredSize(new Dimension(300,25));
            classBox.setVisible(false);
            
            cButton=new JButton("Classes' Time Table");
            cButton.setBackground(new Color(255,255,255));
            cButton.setMaximumSize(new Dimension(300,25));
            cButton.setMinimumSize(new Dimension(300,25));
            cButton.setPreferredSize(new Dimension(300,25));
            
            classEnter=new JButton("Enter");
            classEnter.setBackground(new Color(255,255,255));
            classEnter.setVisible(false);
            
            return;
        }
        
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                classVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
        arr=new String[classVec.size()+1];
        arr[0]="Classes";
        for(int i=0;i<classVec.size();i++){
            arr[i+1]=classVec.get(i);
        }
        
        classBox=new JComboBox(arr);
        classBox.setMaximumSize(new Dimension(300,25));
        classBox.setMinimumSize(new Dimension(300,25));
        classBox.setPreferredSize(new Dimension(300,25));
        classBox.setVisible(false);
        
        cButton=new JButton("Classes' Time Table");
        cButton.setBackground(new Color(255,255,255));
        cButton.setMaximumSize(new Dimension(300,25));
        cButton.setMinimumSize(new Dimension(300,25));
        cButton.setPreferredSize(new Dimension(300,25));
        
        classEnter=new JButton("Enter");
        classEnter.setBackground(new Color(255,255,255));
        classEnter.setVisible(false);
    }
    
    private void setDisplayLabels(String heading){
        
        JLabel l1=new JLabel("RAMANUJAN COLLEGE (UNIVERSITY OF DELHI),");
        JLabel l2=new JLabel("KALKAJI, NEW DELHI");
        JLabel l3=new JLabel("INDIA - 110019");
        JLabel l4=new JLabel("TIME TABLE"+" ( "+getSemesterDetails().toUpperCase()+" )");
        JLabel l5=new JLabel(heading.toUpperCase());
        JLabel l6=new JLabel("( "+department.toUpperCase()+" )");
        
        l1.setFont(new java.awt.Font("Times New Roman",1,18));
        l2.setFont(new java.awt.Font("Times New Roman",1,18));
        l3.setFont(new java.awt.Font("Times New Roman",1,18));
        l4.setFont(new java.awt.Font("Times New Roman",1,18));
        l5.setFont(new java.awt.Font("Times New Roman",1,18));
        l6.setFont(new java.awt.Font("Times New Roman",1,18));
        
        l1.setForeground(new Color(255,255,255));
        l2.setForeground(new Color(255,255,255));
        l3.setForeground(new Color(255,255,255));
        l4.setForeground(new Color(255,255,255));
        l5.setForeground(new Color(255,255,255));
        l6.setForeground(new Color(255,255,255));
        
        panel.add(l1);
        panel.add(l2);
        panel.add(l3);
        panel.add(Box.createRigidArea(new Dimension(30,30)));
        panel.add(l4);
        panel.add(l5);
        panel.add(l6);
        panel.add(Box.createRigidArea(new Dimension(30,30)));
        
        l1.setAlignmentX(l1.CENTER_ALIGNMENT);
        l2.setAlignmentX(l2.CENTER_ALIGNMENT);
        l3.setAlignmentX(l3.CENTER_ALIGNMENT);
        l4.setAlignmentX(l4.CENTER_ALIGNMENT);
        l5.setAlignmentX(l5.CENTER_ALIGNMENT);
        l6.setAlignmentX(l6.CENTER_ALIGNMENT);
    }
    
    private String getSemesterDetails(){
        e=0;
        try {
            dis=new DataInputStream(new FileInputStream("semDetails"));
        } catch (FileNotFoundException ex) {
            return "";
        }
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }
    
    private int getNoOfSlots(){
        try {
            dis=new DataInputStream(new FileInputStream("noOfSlots"));
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog (getContentPane(), "Number of slots not found.\nInitialize no. of Slots for each day.\nExiting..", "Text Read Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void printExcel()throws IOException{
        
        String columns[]={"Period 1","Period 2","Period 3","Period 4","Period 5","Period 6","Period 7","Period 8","Period 9","Period 10"};
        String arr[][]=new String[10][7];
        
        String file=chooseFileXLS();
        if(file.length()==0){
            JOptionPane.showMessageDialog (getContentPane(), "File is not saved", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        FileWriter excel=new FileWriter(file);
        
        for(int p=0;p<10;p++){
            excel.write("\t"+"Period"+(p+1));
        }
        excel.write("\n\n");
        
        for(int d=0;d<7;d++){
            excel.write("Day"+(d+1)+"\t");
            
            for(int p=0;p<10;p++){
                if(teacherSelect==1){
                    boolean selected=false;
                    for(int s=0;s<classVec.size();s++){
                        if(t.compareTo(teacher[s][d][p])==0){
                            excel.write(paper[s][d][p]+"\t");
                            selected=true;
                        }
                    }
                    if(!selected)excel.write("\t");
                }
                else{
                    excel.write(paper[s][d][p]+"\t");
                }
            }
        
            excel.write("\n\t");
            
            for(int p=0;p<10;p++){
                if(teacherSelect==1){
                    boolean selected=false;
                    for(int s=0;s<classVec.size();s++){
                        if(t.compareTo(teacher[s][d][p])==0){
                            excel.write(teacher[s][d][p]+"\t");
                            selected=true;
                        }
                    }
                    if(!selected)excel.write("\t");
                }
                else{
                    excel.write(teacher[s][d][p]+"\t");
                }
            }
        
            excel.write("\n\t");
            
            for(int p=0;p<10;p++){
                if(teacherSelect==1){
                    boolean selected=false;
                    for(int s=0;s<classVec.size();s++){
                        if(t.compareTo(teacher[s][d][p])==0){
                            excel.write(room[s][d][p]+"\t");
                            selected=true;
                        }
                    }
                    if(!selected)excel.write("\t");
                }
                else{
                    excel.write(room[s][d][p]+"\t");
                }
            }
            
            excel.write("\n\n");
        }
        
        excel.close();
        
        JOptionPane.showMessageDialog (getContentPane(), "File is saved", "Saved", JOptionPane.OK_OPTION);
    }
    
    private String chooseFileXLS(){
        
        String file = "";
        
        JFileChooser chooser=new JFileChooser();
        
        int i = chooser.showDialog(null,"Save as excel sheet...");
        File f=chooser.getSelectedFile();
        if(f!=null){
            file=f.getAbsolutePath();
        }
        else return "";
        
        int x=file.lastIndexOf('.');
        String a="";
        if(x<file.length() && x>=0){
            a=file.substring(x);
        }
        if(!a.equalsIgnoreCase(".xls"))
            file+=".xls";
        
        return file;
    }
    
    private void printPDF(String heading){
        String file = chooseFilePDF();
        if(file.length()==0){
            JOptionPane.showMessageDialog (getContentPane(), "File is not saved", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Document doc=new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(file));
        } catch (DocumentException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.open();
        
        Paragraph para1=new Paragraph();
        para1.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
        para1.setAlignment(Element.ALIGN_CENTER);
        para1.add("RAMANUJAN COLLEGE (UNIVERSITY OF DELHI),\nKALKAJI, NEW DELHI\nINDIA - 110019\n\n");
        
        Paragraph para2=new Paragraph();
        para2.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
        para2.setAlignment(Element.ALIGN_CENTER);
        para2.add(("TIME TABLE"+" ( "+getSemesterDetails().toUpperCase()+" )\n"+heading.toUpperCase())+"\n"+"( "+department.toUpperCase()+" )"+"\n\n\n\n");
        
        int start=0,end=0;
        for(int c=1;c<nos+1;c++){//because "monday" "tuesday" ki setting me sab gadbad ho jayega
            int i=0;
            for(int r=0;r<7;r++){
                if(data[r][c].equals("")){
                    i++;
                }
            }
            if(i!=7){
                start=c;
                break;
            }
        }
        for(int c=nos+1-1;c>0;c--){
            int i=0;
            for(int r=0;r<7;r++){
                if(data[r][c].equals("")){
                    i++;
                }
            }
            if(i!=7){
                end=c;
                break;
            }
        }
        
        PdfPTable table=new PdfPTable(end-start+1+1);//no. of Columns//(end-start+1)=actully counted; doosra +1 for "monday" "tuesday"
        com.itextpdf.text.Font tableFontB=new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN,8,com.itextpdf.text.Font.BOLD,new BaseColor(0,0,0));
        com.itextpdf.text.Font tableFontN=new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN,8,com.itextpdf.text.Font.NORMAL,new BaseColor(0,0,0));
        table.addCell(periods[0]);
        
        int s=timeToInteger(Integer.toString(getStartTime()));
        int timeO1=55;
        
        for(int p=0;p<=nos+1;p++){
            if(p>=start && p<=end)
                table.addCell(new PdfPCell(new Phrase(periods[p]+"\n"+integerToTime(s)+" - "+integerToTime(s+timeO1),tableFontB)));
            if(p>0)
            s=s+timeO1;
        }
        for(int r=0;r<7;r++){
            table.addCell(new PdfPCell(new Phrase(data[r][0],tableFontB)));
            for(int c=start;c<=end;c++){
                table.addCell(new PdfPCell(new Phrase(data[r][c],tableFontN)));
            }
        }
        
        Paragraph para3=new Paragraph();
        para3.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
        para3.setAlignment(Element.ALIGN_CENTER);
        if(classSelect==1){
        	/*

        	Paragraph p=new Paragraph();
            p.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
            p.add("Principal's Signature");

        	Paragraph p2=new Paragraph();
            p2.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
            p2.add("Teacher In-Charge's Signature");
            
            para3.add(p);
            para3.add(p2);
            */
            
            para3.add("\n\n\nPrincipal's Signature                                Teacher In Charge's Signature");//8*4 spaces
        }
        else if(teacherSelect==1){
        	
        	/*
        	Paragraph p=new Paragraph();
            p.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
            p.add("Principal's Signature");

        	Paragraph p2=new Paragraph();
            p2.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
            p2.add("Teacher In-Charge's Signature");

        	Paragraph p3=new Paragraph();
            p3.setFont(new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD, new BaseColor(0, 0, 0)));
            p3.add("Teacher's Signature");

            para3.add(p);
            para3.add(p2);
            para3.add(p3);
            */
            
        	para3.add("\n\n\nPrincipal's Signature            Teacher In Charge's Signature            Teacher's Signature");//3*4 spaces
        }
        
        try {
            doc.add(para1);
            doc.add(para2); 
            doc.add(table);
            doc.add(para3);
        } catch (DocumentException ex) {
            Logger.getLogger(MainDisplayPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.close();
        
        JOptionPane.showMessageDialog (getContentPane(), "File is saved", "Saved", JOptionPane.OK_OPTION);
    }
    
    private String chooseFilePDF(){
        
        String file = "";
        
        JFileChooser chooser=new JFileChooser();
        
        int i = chooser.showDialog(null,"Save as PDF...");
        File f=chooser.getSelectedFile();
        if(f!=null){
            file=f.getAbsolutePath();
        }
        else return "";
        
        int x=file.lastIndexOf('.');
        String a="";
        if(x<file.length() && x>=0){
            a=file.substring(x);
        }
        if(!a.equalsIgnoreCase(".pdf"))
            file+=".pdf";
        
        return file;
    }
}