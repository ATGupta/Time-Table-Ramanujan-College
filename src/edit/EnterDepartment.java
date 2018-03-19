package edit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class EnterDepartment extends JFrame {
    
    private int horizontal,vertical,depIndex,e;
    private Vector<String> deps,depP,teacherVec,roomVec,classVec,paperVec;
    private JButton changeDepPass,change,addTeacher,removeTeacher,addRoom,removeRoom,addClass,removeClass,addPaper,removePaper,editTimeTable,back;
    private JLabel old,new1,new2,teacherAllowed,roomAllowed,classAllowed,paperAllowed;
    private JPasswordField oldPass,newPass1,newPass2;
    private String password,encoder,arr[],entered,passwordEntered;
    private DataOutputStream dos;
    private JTextField nameOfTeacher,nameOfRoom,nameOfClass,nameOfPaper;
    private DataInputStream dis;
    private JComboBox teacherBox,roomBox,classBox,paperBox;
    private JPanel papa;
    
    public EnterDepartment(int h, int v, int DI, Vector<String> ds, Vector<String> DP, String pE) {
        horizontal=h;vertical=v;
        depIndex=DI;
        deps=ds;
        depP=DP;
        password=depP.get(depIndex);
        passwordEntered=pE;
        encoder="Aryan";
        setSize(horizontal,vertical);
        
        getContentPane().setBackground(new Color(0,0,0));
        initialize();
        setComponents();
        actionListeners();
        
        setWindowIcon();
        
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setWindowIcon(){
    	ImageIcon image=new ImageIcon("icon");
    	setIconImage(image.getImage());
    }

	private void initialize(){
        
        initializeAddTeacherItems();
        initializeRemoveTeacherItems();
        
        initializeAddRoomItems();
        initializeRemoveRoomItems();
        
        initializeAddClassItems();
        initializeRemoveClassItems();
        
        initializeAddPaperItems();
        initializeRemovePaperItems();
        
        papa=new JPanel();papa.setOpaque(false);
        
        editTimeTable=new JButton("Edit Time Table");
        editTimeTable.setBackground(new Color(255,255,255));
        
        changeDepPass=new JButton("Change Department Password");
        changeDepPass.setBackground(new Color(255,255,255));
        
        old=new JLabel("Enter Old Password");
        old.setForeground(new Color(255,255,255));
        old.setVisible(false);
        
        oldPass=new JPasswordField();
        oldPass.setMaximumSize(new Dimension(200,30));
        oldPass.setMinimumSize(new Dimension(200,30));
        oldPass.setPreferredSize(new Dimension(200,30));
        oldPass.setVisible(false);
        
        new1=new JLabel("Enter New Password");
        new1.setForeground(new Color(255,255,255));
        new1.setVisible(false);
        
        newPass1=new JPasswordField();
        newPass1.setMaximumSize(new Dimension(200,30));
        newPass1.setMinimumSize(new Dimension(200,30));
        newPass1.setPreferredSize(new Dimension(200,30));
        newPass1.setVisible(false);
        
        new2=new JLabel("Re-enter New Password");
        new2.setForeground(new Color(255,255,255));
        new2.setVisible(false);
        
        newPass2=new JPasswordField();
        newPass2.setMaximumSize(new Dimension(200,30));
        newPass2.setMinimumSize(new Dimension(200,30));
        newPass2.setPreferredSize(new Dimension(200,30));
        newPass2.setVisible(false);
        
        change=new JButton("Change");
        change.setBackground(new Color(255,255,255));
        change.setVisible(false);
        
        back=new JButton("Back");
        back.setBackground(new Color(255,255,255));
    }
    
    private void initializeAddTeacherItems(){
        addTeacher=new JButton("Add Teacher");
        addTeacher.setBackground(new Color(255,255,255));
        addTeacher.setMaximumSize(new Dimension(200,30));
        addTeacher.setMinimumSize(new Dimension(200,30));
        addTeacher.setPreferredSize(new Dimension(200,30));
        
        nameOfTeacher=new JTextField();
        nameOfTeacher.setMaximumSize(new Dimension(500,30));
        nameOfTeacher.setMinimumSize(new Dimension(500,30));
        nameOfTeacher.setPreferredSize(new Dimension(500,30));
        
        teacherAllowed=new JLabel();
        teacherAllowed.setOpaque(false);
    }
    
    private void initializeRemoveTeacherItems(){
        teacherVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(deps.get(depIndex)+"\\teacher"));
        } catch (FileNotFoundException ex) {
            arr=new String[1];arr[0]="Teachers";
            teacherBox=new JComboBox(arr);
            teacherBox.setMaximumSize(new Dimension(500,30));
            teacherBox.setMinimumSize(new Dimension(500,30));
            teacherBox.setPreferredSize(new Dimension(500,30));
            
            removeTeacher=new JButton("Remove Teacher");
            removeTeacher.setBackground(new Color(255,255,255));
            removeTeacher.setMaximumSize(new Dimension(200,30));
            removeTeacher.setMinimumSize(new Dimension(200,30));
            removeTeacher.setPreferredSize(new Dimension(200,30));
            
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(EnterDepartment.class.getName()).log(Level.SEVERE, null, ex);
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
        teacherBox.setMaximumSize(new Dimension(500,30));
        teacherBox.setMinimumSize(new Dimension(500,30));
        teacherBox.setPreferredSize(new Dimension(500,30));
        
        removeTeacher=new JButton("Remove Teacher");
        removeTeacher.setBackground(new Color(255,255,255));
        removeTeacher.setMaximumSize(new Dimension(200,30));
        removeTeacher.setMinimumSize(new Dimension(200,30));
        removeTeacher.setPreferredSize(new Dimension(200,30));
    }
    
    private void initializeAddRoomItems(){
        addRoom=new JButton("Add Room");
        addRoom.setBackground(new Color(255,255,255));
        addRoom.setMaximumSize(new Dimension(200,30));
        addRoom.setMinimumSize(new Dimension(200,30));
        addRoom.setPreferredSize(new Dimension(200,30));
        
        nameOfRoom=new JTextField();
        nameOfRoom.setMaximumSize(new Dimension(500,30));
        nameOfRoom.setMinimumSize(new Dimension(500,30));
        nameOfRoom.setPreferredSize(new Dimension(500,30));
        
        roomAllowed=new JLabel();
        roomAllowed.setOpaque(false);
    }
    
    private void initializeRemoveRoomItems(){
        roomVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(deps.get(depIndex)+"\\room"));
        } catch (FileNotFoundException ex) {
            arr=new String[1];arr[0]="Rooms";
            roomBox=new JComboBox(arr);
            roomBox.setMaximumSize(new Dimension(500,30));
            roomBox.setMinimumSize(new Dimension(500,30));
            roomBox.setPreferredSize(new Dimension(500,30));
            
            removeRoom=new JButton("Remove Room");
            removeRoom.setBackground(new Color(255,255,255));
            removeRoom.setMaximumSize(new Dimension(200,30));
            removeRoom.setMinimumSize(new Dimension(200,30));
            removeRoom.setPreferredSize(new Dimension(200,30));
            
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(EnterDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                roomVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
        arr=new String[roomVec.size()+1];
        arr[0]="Rooms";
        for(int i=0;i<roomVec.size();i++){
            arr[i+1]=roomVec.get(i);
        }
        roomBox=new JComboBox(arr);
        roomBox.setMaximumSize(new Dimension(500,30));
        roomBox.setMinimumSize(new Dimension(500,30));
        roomBox.setPreferredSize(new Dimension(500,30));
        
        removeRoom=new JButton("Remove Room");
        removeRoom.setBackground(new Color(255,255,255));
        removeRoom.setMaximumSize(new Dimension(200,30));
        removeRoom.setMinimumSize(new Dimension(200,30));
        removeRoom.setPreferredSize(new Dimension(200,30));
    }
    
    private void initializeAddClassItems(){
        addClass=new JButton("Add Class");
        addClass.setBackground(new Color(255,255,255));
        addClass.setMaximumSize(new Dimension(200,30));
        addClass.setMinimumSize(new Dimension(200,30));
        addClass.setPreferredSize(new Dimension(200,30));
        
        nameOfClass=new JTextField();
        nameOfClass.setMaximumSize(new Dimension(500,30));
        nameOfClass.setMinimumSize(new Dimension(500,30));
        nameOfClass.setPreferredSize(new Dimension(500,30));
        
        classAllowed=new JLabel();
        classAllowed.setOpaque(false);
    }
    
    private void initializeRemoveClassItems(){
        classVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(deps.get(depIndex)+"\\class"));
        } catch (FileNotFoundException ex) {
            arr=new String[1];arr[0]="Classes";
            classBox=new JComboBox(arr);
            classBox.setMaximumSize(new Dimension(500,30));
            classBox.setMinimumSize(new Dimension(500,30));
            classBox.setPreferredSize(new Dimension(500,30));
            
            removeClass=new JButton("Remove Class");
            removeClass.setBackground(new Color(255,255,255));
            removeClass.setMaximumSize(new Dimension(200,30));
            removeClass.setMinimumSize(new Dimension(200,30));
            removeClass.setPreferredSize(new Dimension(200,30));
            
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(EnterDepartment.class.getName()).log(Level.SEVERE, null, ex);
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
        classBox.setMaximumSize(new Dimension(500,30));
        classBox.setMinimumSize(new Dimension(500,30));
        classBox.setPreferredSize(new Dimension(500,30));
        
        removeClass=new JButton("Remove Class");
        removeClass.setBackground(new Color(255,255,255));
        removeClass.setMaximumSize(new Dimension(200,30));
        removeClass.setMinimumSize(new Dimension(200,30));
        removeClass.setPreferredSize(new Dimension(200,30));
    }
    
    private void initializeAddPaperItems(){
        addPaper=new JButton("Add Paper");
        addPaper.setBackground(new Color(255,255,255));
        addPaper.setMaximumSize(new Dimension(200,30));
        addPaper.setMinimumSize(new Dimension(200,30));
        addPaper.setPreferredSize(new Dimension(200,30));
        
        nameOfPaper=new JTextField();
        nameOfPaper.setMaximumSize(new Dimension(500,30));
        nameOfPaper.setMinimumSize(new Dimension(500,30));
        nameOfPaper.setPreferredSize(new Dimension(500,30));
        
        paperAllowed=new JLabel();
        paperAllowed.setOpaque(false);
    }
    
    private void initializeRemovePaperItems(){
        paperVec=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream(deps.get(depIndex)+"\\paper"));
        } catch (FileNotFoundException ex) {
            arr=new String[1];arr[0]="Papers";
            paperBox=new JComboBox(arr);
            paperBox.setMaximumSize(new Dimension(500,30));
            paperBox.setMinimumSize(new Dimension(500,30));
            paperBox.setPreferredSize(new Dimension(500,30));
            
            removePaper=new JButton("Remove Paper");
            removePaper.setBackground(new Color(255,255,255));
            removePaper.setMaximumSize(new Dimension(200,30));
            removePaper.setMinimumSize(new Dimension(200,30));
            removePaper.setPreferredSize(new Dimension(200,30));
            
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(EnterDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                paperVec.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
        arr=new String[paperVec.size()+1];
        arr[0]="Papers";
        for(int i=0;i<paperVec.size();i++){
            arr[i+1]=paperVec.get(i);
        }
        paperBox=new JComboBox(arr);
        paperBox.setMaximumSize(new Dimension(500,30));
        paperBox.setMinimumSize(new Dimension(500,30));
        paperBox.setPreferredSize(new Dimension(500,30));
        
        removePaper=new JButton("Remove Paper");
        removePaper.setBackground(new Color(255,255,255));
        removePaper.setMaximumSize(new Dimension(200,30));
        removePaper.setMinimumSize(new Dimension(200,30));
        removePaper.setPreferredSize(new Dimension(200,30));
    }
    
    private void setComponents(){
        papa.setLayout(new BoxLayout(papa,BoxLayout.Y_AXIS));
        
        papa.add(Box.createRigidArea(new Dimension(10,30)));
        
        JPanel p=new JPanel();
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(horizontal,50));
        p.setMinimumSize(new Dimension(horizontal,50));
        p.setPreferredSize(new Dimension(horizontal,50));
        p.setLayout(new FlowLayout());
        p.add(nameOfTeacher);
        p.add(teacherAllowed);
        p.add(addTeacher);
        papa.add(p);
        
        JPanel p1=new JPanel();
        p1.setOpaque(false);
        p1.setMaximumSize(new Dimension(horizontal,50));
        p1.setMinimumSize(new Dimension(horizontal,50));
        p1.setPreferredSize(new Dimension(horizontal,50));
        p1.setLayout(new FlowLayout());
        p1.add(teacherBox);
        p1.add(removeTeacher);
        papa.add(p1);
        
        papa.add(Box.createRigidArea(new Dimension(10,30)));
        
        JPanel p2=new JPanel();
        p2.setOpaque(false);
        p2.setMaximumSize(new Dimension(horizontal,50));
        p2.setMinimumSize(new Dimension(horizontal,50));
        p2.setPreferredSize(new Dimension(horizontal,50));
        p2.setLayout(new FlowLayout());
        p2.add(nameOfRoom);
        p2.add(roomAllowed);
        p2.add(addRoom);
        papa.add(p2);
        
        JPanel p3=new JPanel();
        p3.setOpaque(false);
        p3.setMaximumSize(new Dimension(horizontal,50));
        p3.setMinimumSize(new Dimension(horizontal,50));
        p3.setPreferredSize(new Dimension(horizontal,50));
        p3.setLayout(new FlowLayout());
        p3.add(roomBox);
        p3.add(removeRoom);
        papa.add(p3);
        
        papa.add(Box.createRigidArea(new Dimension(10,30)));
        
        JPanel p4=new JPanel();
        p4.setOpaque(false);
        p4.setMaximumSize(new Dimension(horizontal,50));
        p4.setMinimumSize(new Dimension(horizontal,50));
        p4.setPreferredSize(new Dimension(horizontal,50));
        p4.setLayout(new FlowLayout());
        p4.add(nameOfClass);
        p4.add(classAllowed);
        p4.add(addClass);
        papa.add(p4);
        
        JPanel p5=new JPanel();
        p5.setOpaque(false);
        p5.setMaximumSize(new Dimension(horizontal,50));
        p5.setMinimumSize(new Dimension(horizontal,50));
        p5.setPreferredSize(new Dimension(horizontal,50));
        p5.setLayout(new FlowLayout());
        p5.add(classBox);
        p5.add(removeClass);
        papa.add(p5);
        
        papa.add(Box.createRigidArea(new Dimension(10,30)));
        
        JPanel p6=new JPanel();
        p6.setOpaque(false);
        p6.setMaximumSize(new Dimension(horizontal,50));
        p6.setMinimumSize(new Dimension(horizontal,50));
        p6.setPreferredSize(new Dimension(horizontal,50));
        p6.setLayout(new FlowLayout());
        p6.add(nameOfPaper);
        p6.add(paperAllowed);
        p6.add(addPaper);
        papa.add(p6);
        
        JPanel p7=new JPanel();
        p7.setOpaque(false);
        p7.setMaximumSize(new Dimension(horizontal,50));
        p7.setMinimumSize(new Dimension(horizontal,50));
        p7.setPreferredSize(new Dimension(horizontal,50));
        p7.setLayout(new FlowLayout());
        p7.add(paperBox);
        p7.add(removePaper);
        papa.add(p7);
        
        papa.add(Box.createRigidArea(new Dimension(10,30)));
        
        papa.add(editTimeTable);
        editTimeTable.setAlignmentX(editTimeTable.CENTER_ALIGNMENT);
        
        papa.add(Box.createRigidArea(new Dimension(10,20)));
        
        papa.add(changeDepPass);
        changeDepPass.setAlignmentX(changeDepPass.CENTER_ALIGNMENT);
        
        papa.add(old);
        old.setAlignmentX(old.CENTER_ALIGNMENT);
        
        papa.add(oldPass);
        oldPass.setAlignmentX(oldPass.CENTER_ALIGNMENT);
        
        papa.add(new1);
        new1.setAlignmentX(new1.CENTER_ALIGNMENT);
        
        papa.add(newPass1);
        newPass1.setAlignmentX(newPass1.CENTER_ALIGNMENT);
        
        papa.add(new2);
        new2.setAlignmentX(new2.CENTER_ALIGNMENT);
        
        papa.add(newPass2);
        newPass2.setAlignmentX(newPass2.CENTER_ALIGNMENT);
        
        papa.add(change);
        change.setAlignmentX(change.CENTER_ALIGNMENT);
        
        JPanel p8=new JPanel();
        p8.setOpaque(false);
        p8.setLayout(new FlowLayout());
        p8.setMaximumSize(new Dimension(500,100));
        p8.add(back);
        papa.add(p8);
        
        JScrollPane scroll=new JScrollPane(papa);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getHorizontalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setOpaque(false);
        add(scroll);
        
        setVisibleAccordingToPasswordUser();
    }
    
    private void setVisibleAccordingToPasswordUser() {
    	String adminPassword=getAdminPassword();
    	if(adminPassword.equals(passwordEntered)){
    		
    	}
    	else{
    		addTeacher.setVisible(false);
    		removeTeacher.setVisible(false);
    		addRoom.setVisible(false);
    		removeRoom.setVisible(false);
    		addClass.setVisible(false);
    		removeClass.setVisible(false);
    		addPaper.setVisible(false);
    		removePaper.setVisible(false);
    		
    		nameOfTeacher.setVisible(false);
    		nameOfRoom.setVisible(false);
    		nameOfClass.setVisible(false);
    		nameOfPaper.setVisible(false);
    		
    		teacherBox.setVisible(false);
    		roomBox.setVisible(false);
    		classBox.setVisible(false);
    		paperBox.setVisible(false);
    	}
	}
    
    private String getAdminPassword(){
        try {
            dis=new DataInputStream(new FileInputStream("pass"));
        } catch (FileNotFoundException ex) {
            return null;
        }
        e=0;
        try {
            return decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainEditPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

	private void actionListeners(){
		
        editTimeTable.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new EditTimeTable(horizontal,vertical,teacherVec,roomVec,classVec,paperVec,deps,depIndex,teacherBox,roomBox,classBox,paperBox,depP,passwordEntered);
                dispose();
            }
        });
        
        changeDepPass.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                changeDepPass.setVisible(false);
                
                old.setVisible(true);
                oldPass.setVisible(true);
                new1.setVisible(true);
                newPass1.setVisible(true);
                new2.setVisible(true);
                newPass2.setVisible(true);
                change.setVisible(true);
            }
        });
        
        change.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!checkPassword(password,oldPass.getPassword())){
                    JOptionPane.showMessageDialog (getContentPane(), "Old password Incorrect", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    oldPass.setText("");
                    newPass1.setText("");
                    newPass2.setText("");
                }
                else if(!checkPassword(newPass1.getPassword(),newPass2.getPassword())){
                    JOptionPane.showMessageDialog (getContentPane(), "New passwords mismatch", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    oldPass.setText("");
                    newPass1.setText("");
                    newPass2.setText("");
                }
                else{
                    writeDepPassword();
                    JOptionPane.showMessageDialog (getContentPane(), "Password Changed", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    oldPass.setText("");
                    newPass1.setText("");
                    newPass2.setText("");
                }
            }
        });
        
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainEditPage(horizontal,vertical);
            }
        });
        
        if(!getAdminPassword().equals(passwordEntered) && !getAdminPassword().equals("Nivedita")){
        	return;
        }
        
        nameOfTeacher.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent arg0) {}
            public void keyReleased(KeyEvent arg0) {
                checkEnteredTeacher();
            }
            public void keyTyped(KeyEvent arg0) {}
        });
        
        addTeacher.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(teacherAllowed.getText().equalsIgnoreCase("Allowed")){
                    addEnteredTeacher();
                    printEnteredTeacher();
                    JOptionPane.showMessageDialog (getContentPane(), "Teacher Added", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    nameOfTeacher.setText("");
                    nameOfTeacher.requestFocus(true);
                }
            }
        });
        
        removeTeacher.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!((String)(teacherBox.getSelectedItem())).equalsIgnoreCase("Teachers")){
                    entered=(String)teacherBox.getSelectedItem();
                    int i=teacherBox.getSelectedIndex();
                    teacherBox.removeItemAt(i);
                    teacherVec.remove(i-1);
                    printEnteredTeacher();
                    JOptionPane.showMessageDialog (getContentPane(), "Teacher Removed", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        nameOfRoom.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent arg0) {}
            public void keyReleased(KeyEvent arg0) {
                checkEnteredRoom();
            }
            public void keyTyped(KeyEvent arg0) {}
        });
        
        addRoom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(roomAllowed.getText().equalsIgnoreCase("Allowed")){
                    addEnteredRoom();
                    printEnteredRoom();
                    JOptionPane.showMessageDialog (getContentPane(), "Room Added", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    nameOfRoom.setText("");
                    nameOfRoom.requestFocus(true);
                }
            }
        });
        
        removeRoom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!((String)(roomBox.getSelectedItem())).equalsIgnoreCase("Rooms")){
                    entered=(String)roomBox.getSelectedItem();
                    int i=roomBox.getSelectedIndex();
                    roomBox.removeItemAt(i);
                    roomVec.remove(i-1);
                    printEnteredRoom();
                    JOptionPane.showMessageDialog (getContentPane(), "Room Removed", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        nameOfClass.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent arg0) {}
            public void keyReleased(KeyEvent arg0) {
                checkEnteredClass();
            }
            public void keyTyped(KeyEvent arg0) {}
        });
        
        addClass.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(classAllowed.getText().equalsIgnoreCase("Allowed")){
                    addEnteredClass();
                    printEnteredClass();
                    JOptionPane.showMessageDialog (getContentPane(), "Class Added", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    nameOfClass.setText("");
                    nameOfClass.requestFocus(true);
                }
            }
        });
        
        removeClass.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!((String)(classBox.getSelectedItem())).equalsIgnoreCase("Classes")){
                    entered=(String)classBox.getSelectedItem();
                    int i=classBox.getSelectedIndex();
                    classBox.removeItemAt(i);
                    classVec.remove(i-1);
                    printEnteredClass();
                    JOptionPane.showMessageDialog (getContentPane(), "Class Removed", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        nameOfPaper.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent arg0) {}
            public void keyReleased(KeyEvent arg0) {
                checkEnteredPaper();
            }
            public void keyTyped(KeyEvent arg0) {}
        });
        
        addPaper.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(paperAllowed.getText().equalsIgnoreCase("Allowed")){
                    addEnteredPaper();
                    printEnteredPaper();
                    JOptionPane.showMessageDialog (getContentPane(), "Paper Added", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    nameOfPaper.setText("");
                    nameOfPaper.requestFocus(true);
                }
            }
        });
        
        removePaper.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!((String)(paperBox.getSelectedItem())).equalsIgnoreCase("Papers")){
                    entered=(String)paperBox.getSelectedItem();
                    int i=paperBox.getSelectedIndex();
                    paperBox.removeItemAt(i);
                    paperVec.remove(i-1);
                    printEnteredPaper();
                    JOptionPane.showMessageDialog (getContentPane(), "Paper Removed", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void writeDepPassword(){
        try {
            dos=new DataOutputStream(new FileOutputStream("depPassword"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnterStartTime.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        String a="";
        for(int i=0;i<newPass1.getPassword().length;i++){
            a=a+newPass1.getPassword()[i];
        }
        
        depP.remove(depIndex);
        depP.add(depIndex,a);
        
        for(int i=0;i<depP.size();i++){
            try {
                encrypt(depP.get(i)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(EnterStartTime.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean checkPassword(String a,char b[]){
        if(a.length()!=b.length)return false;
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)!=b[i])return false;
        }
        return true;
    }
    
    private boolean checkPassword(char a[],char b[]){
        if(a.length!=b.length)return false;
        for(int i=0;i<a.length;i++){
            if(a[i]!=b[i])return false;
        }
        return true;
    }
    
    private void encrypt(String a)throws IOException{
        for(int i=0;i<a.length();i++){
            int x=(int)a.charAt(i)+(int)encoder.charAt(e);
            dos.write(x);
            e=(e+1)%encoder.length();
        }
    }
    
    private void checkEnteredTeacher(){
        entered=nameOfTeacher.getText();
        int t=0;
        for(int i=0;i<teacherVec.size();i++){
            if(teacherVec.get(i).equalsIgnoreCase(entered)){
                t=1;break;
            }
        }
        if(t==1 || entered.compareTo("")==0){
            teacherAllowed.setText("Not Allowed");
            teacherAllowed.setVerticalAlignment(SwingConstants.CENTER);
            teacherAllowed.setForeground(Color.RED);
        }
        else {
            teacherAllowed.setText("Allowed");
            teacherAllowed.setVerticalAlignment(SwingConstants.CENTER);
            teacherAllowed.setForeground(Color.GREEN);
        }
    }
    
    private void addEnteredTeacher(){
        entered=nameOfTeacher.getText();
        teacherVec.add(entered);
        teacherBox.addItem(entered);
    }
    
    private void printEnteredTeacher(){
        
        try {
            dos=new DataOutputStream(new FileOutputStream(deps.get(depIndex)+"\\teacher"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        for(int i=0;i<teacherVec.size();i++){
            try {
                encrypt(teacherVec.get(i)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void checkEnteredRoom(){
        entered=nameOfRoom.getText();
        int t=0;
        for(int i=0;i<roomVec.size();i++){
            if(roomVec.get(i).equalsIgnoreCase(entered)){
                t=1;break;
            }
        }
        if(t==1 || entered.compareTo("")==0){
            roomAllowed.setText("Not Allowed");
            roomAllowed.setVerticalAlignment(SwingConstants.CENTER);
            roomAllowed.setForeground(Color.RED);
        }
        else {
            roomAllowed.setText("Allowed");
            roomAllowed.setVerticalAlignment(SwingConstants.CENTER);
            roomAllowed.setForeground(Color.GREEN);
        }
    }
    
    private void addEnteredRoom(){
        entered=nameOfRoom.getText();
        roomVec.add(entered);
        roomBox.addItem(entered);
    }
    
    private void printEnteredRoom(){
        
        try {
            dos=new DataOutputStream(new FileOutputStream(deps.get(depIndex)+"\\room"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        for(int i=0;i<roomVec.size();i++){
            try {
                encrypt(roomVec.get(i)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void checkEnteredClass(){
        entered=nameOfClass.getText();
        int t=0;
        for(int i=0;i<classVec.size();i++){
            if(classVec.get(i).equalsIgnoreCase(entered)){
                t=1;break;
            }
        }
        if(t==1 || entered.compareTo("")==0){
            classAllowed.setText("Not Allowed");
            classAllowed.setVerticalAlignment(SwingConstants.CENTER);
            classAllowed.setForeground(Color.RED);
        }
        else {
            classAllowed.setText("Allowed");
            classAllowed.setVerticalAlignment(SwingConstants.CENTER);
            classAllowed.setForeground(Color.GREEN);
        }
    }
    
    private void addEnteredClass(){
        entered=nameOfClass.getText();
        classVec.add(entered);
        classBox.addItem(entered);
    }
    
    private void printEnteredClass(){
        
        try {
            dos=new DataOutputStream(new FileOutputStream(deps.get(depIndex)+"\\class"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        for(int i=0;i<classVec.size();i++){
            try {
                encrypt(classVec.get(i)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void checkEnteredPaper(){
        entered=nameOfPaper.getText();
        int t=0;
        for(int i=0;i<paperVec.size();i++){
            if(paperVec.get(i).equalsIgnoreCase(entered)){
                t=1;break;
            }
        }
        if(t==1 || entered.compareTo("")==0){
            paperAllowed.setText("Not Allowed");
            paperAllowed.setVerticalAlignment(SwingConstants.CENTER);
            paperAllowed.setForeground(Color.RED);
        }
        else {
            paperAllowed.setText("Allowed");
            paperAllowed.setVerticalAlignment(SwingConstants.CENTER);
            paperAllowed.setForeground(Color.GREEN);
        }
    }
    
    private void addEnteredPaper(){
        entered=nameOfPaper.getText();
        paperVec.add(entered);
        paperBox.addItem(entered);
    }
    
    private void printEnteredPaper(){
        
        try {
            dos=new DataOutputStream(new FileOutputStream(deps.get(depIndex)+"\\paper"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        for(int i=0;i<paperVec.size();i++){
            try {
                encrypt(paperVec.get(i)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
}
