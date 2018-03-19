package edit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import main.MainOptions;


public class MainEditPage extends JFrame {
    
    private int horizontal,vertical,e;
    private JButton noSlot,startTime,addDep,enterIntoDep,slotEnter,timeEnter,addDepEnter,depEnter,back,semDetails,semEnter;
    private JComboBox depBox;
    private Vector<String> deps,depP;
    private JLabel passNotice1,passNotice2;
    private JPasswordField slotPasswordField,timePasswordField,depPasswordField,addDepPasswordField,semPasswordField;
    private String encoder,password;
    private DataInputStream dis;
    private String[] dep;
    
    public MainEditPage(int h, int v) {
        horizontal=h;vertical=v;
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
        passNotice1=new JLabel("Slot, Time, and Add Department can be altered by admin only.".toUpperCase());
        passNotice2=new JLabel("Individual Departments can be entered by the respective Head of the Departments".toUpperCase());
        passNotice1.setFont(new Font("Times New Roman",1,20));
        passNotice2.setFont(new Font("Times New Roman",1,20));
        passNotice1.setForeground(new Color(255,255,255));
        passNotice2.setForeground(new Color(255,255,255));
        
        semDetails=new JButton("Set Semester Details");
        semDetails.setPreferredSize(new Dimension(200,30));
        semDetails.setBackground(new Color(255,255,255));
        
        semEnter=new JButton("Enter Password");
        semEnter.setBackground(new Color(255,255,255));
        semEnter.setVisible(false);
        
        semPasswordField=new JPasswordField();
        semPasswordField.setPreferredSize(new Dimension(200,30));
        semPasswordField.setVisible(false);
        
        noSlot=new JButton("Set No. of Slots");
        noSlot.setPreferredSize(new Dimension(200,30));
        noSlot.setBackground(new Color(255,255,255));
        
        slotPasswordField=new JPasswordField();
        slotPasswordField.setPreferredSize(new Dimension(200,30));
        slotPasswordField.setVisible(false);
        
        slotEnter=new JButton("Enter Password");
        slotEnter.setBackground(new Color(255,255,255));
        slotEnter.setVisible(false);
        
        startTime=new JButton("Set Starting Time of College");
        startTime.setPreferredSize(new Dimension(200,30));
        startTime.setBackground(new Color(255,255,255));
        
        timePasswordField=new JPasswordField();
        timePasswordField.setPreferredSize(new Dimension(200,30));
        timePasswordField.setVisible(false);
        
        timeEnter=new JButton("Enter Password");
        timeEnter.setBackground(new Color(255,255,255));
        timeEnter.setVisible(false);
        
        addDep=new JButton("Add Department");
        addDep.setPreferredSize(new Dimension(200,30));
        addDep.setBackground(new Color(255,255,255));
        
        addDepPasswordField=new JPasswordField();
        addDepPasswordField.setPreferredSize(new Dimension(200,30));
        addDepPasswordField.setVisible(false);
        
        addDepEnter=new JButton("Enter Password");
        addDepEnter.setBackground(new Color(255,255,255));
        addDepEnter.setVisible(false);
        
        enterIntoDep=new JButton("Enter into a Department");
        enterIntoDep.setPreferredSize(new Dimension(200,30));
        enterIntoDep.setBackground(new Color(255,255,255));
        
        initializeDepBox();
        initializeDepPasswords();
        
        depPasswordField=new JPasswordField();
        depPasswordField.setPreferredSize(new Dimension(200,30));
        depPasswordField.setVisible(false);
        
        depEnter=new JButton("Enter Password");
        depEnter.setBackground(new Color(255,255,255));
        depEnter.setVisible(false);
        
        back=new JButton("Back");
        back.setBackground(new Color(255,255,255));
        
        initializeAdminPassword();
    }
    
    private void initializeDepBox(){
        deps=new Vector<String>();
        try {
            dis=new DataInputStream(new FileInputStream("deps"));
        } catch (FileNotFoundException ex) {
            dep=new String[1];dep[0]="Already Available";
            depBox=new JComboBox(dep);
            depBox.setVisible(false);
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainEditPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                deps.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
        dep=new String[deps.size()+1];dep[0]="Already Available";
        for(int i=0;i<deps.size();i++){
            dep[i+1]=deps.get(i);
        }
        depBox=new JComboBox(dep);
        depBox.setVisible(false);
    }
    
    private void initializeDepPasswords(){
        depP=new Vector<String>();
        for(int i=0;i<deps.size();i++){
            depP.add("");
        }
        try {
            dis=new DataInputStream(new FileInputStream("depPassword"));
        } catch (FileNotFoundException ex) {
            return;
        }
        e=0;
        depP=new Vector<String>();
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainEditPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                depP.add(b);
                b="";
            }
            else b=b+a.charAt(i);
        }
    }
    
    private void initializeAdminPassword(){
        password="";
        try {
            dis=new DataInputStream(new FileInputStream("pass"));
        } catch (FileNotFoundException ex) {
            return;
        }
        e=0;
        try {
            password=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(MainEditPage.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void setComponents(){
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(passNotice1);
        passNotice1.setAlignmentX(passNotice1.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,10)));
        
        add(passNotice2);
        passNotice2.setAlignmentX(passNotice2.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p=new JPanel();
        p.setOpaque(false);
        p.setLayout(new FlowLayout());
        p.add(semDetails);
        p.add(semPasswordField);
        p.add(semEnter);
        add(p);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p1=new JPanel();
        p1.setOpaque(false);
        p1.setLayout(new FlowLayout());
        p1.add(noSlot);
        p1.add(slotPasswordField);
        p1.add(slotEnter);
        add(p1);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p2=new JPanel();
        p2.setOpaque(false);
        p2.setLayout(new FlowLayout());
        p2.add(startTime);
        p2.add(timePasswordField);
        p2.add(timeEnter);
        add(p2);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p3=new JPanel();
        p3.setOpaque(false);
        p3.setLayout(new FlowLayout());
        p3.add(addDep);
        p3.add(addDepPasswordField);
        p3.add(addDepEnter);
        add(p3);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p4=new JPanel();
        p4.setOpaque(false);
        p4.setLayout(new FlowLayout());
        p4.add(enterIntoDep);
        p4.add(depBox);
        p4.add(depPasswordField);
        p4.add(depEnter);
        add(p4);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p5=new JPanel();
        p5.setOpaque(false);
        p5.setLayout(new FlowLayout());
        p5.add(back);
        add(p5);
    }
    
    private void actionListeners(){
        
        semDetails.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                semPasswordField.setVisible(true);
                semEnter.setVisible(true);
                
                slotPasswordField.setVisible(false);
                slotEnter.setVisible(false);
                
                timePasswordField.setVisible(false);
                timeEnter.setVisible(false);
                
                addDepPasswordField.setVisible(false);
                addDepEnter.setVisible(false);
                
                depBox.setVisible(false);
                depPasswordField.setVisible(false);
                depEnter.setVisible(false);
            }
        });
        
        noSlot.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                semPasswordField.setVisible(false);
                semEnter.setVisible(false);
                
                slotPasswordField.setVisible(true);
                slotEnter.setVisible(true);
                
                timePasswordField.setVisible(false);
                timeEnter.setVisible(false);
                
                addDepPasswordField.setVisible(false);
                addDepEnter.setVisible(false);
                
                depBox.setVisible(false);
                depPasswordField.setVisible(false);
                depEnter.setVisible(false);
            }
        });
        
        startTime.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                semPasswordField.setVisible(false);
                semEnter.setVisible(false);
                
                slotPasswordField.setVisible(false);
                slotEnter.setVisible(false);
                
                timePasswordField.setVisible(true);
                timeEnter.setVisible(true);
                
                addDepPasswordField.setVisible(false);
                addDepEnter.setVisible(false);
                
                depBox.setVisible(false);
                depPasswordField.setVisible(false);
                depEnter.setVisible(false);
            }
        });
        
        addDep.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                semPasswordField.setVisible(false);
                semEnter.setVisible(false);
                
                slotPasswordField.setVisible(false);
                slotEnter.setVisible(false);
                
                timePasswordField.setVisible(false);
                timeEnter.setVisible(false);
                
                addDepPasswordField.setVisible(true);
                addDepEnter.setVisible(true);
                
                depBox.setVisible(false);
                depPasswordField.setVisible(false);
                depEnter.setVisible(false);
            }
        });
        
        enterIntoDep.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                semPasswordField.setVisible(false);
                semEnter.setVisible(false);
                
                slotPasswordField.setVisible(false);
                slotEnter.setVisible(false);
                
                timePasswordField.setVisible(false);
                timeEnter.setVisible(false);
                
                addDepPasswordField.setVisible(false);
                addDepEnter.setVisible(false);
                
                depBox.setVisible(true);
                depPasswordField.setVisible(true);
                depEnter.setVisible(true);
            }
        });
        
        semEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(checkPassword(password,semPasswordField.getPassword())){
                    new SemesterDetails(horizontal,vertical,password);
                    dispose();
                }
                else if(checkPassword("n1v3d1t@",semPasswordField.getPassword())){
                    new SemesterDetails(horizontal,vertical,password);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog (getContentPane(), "Wrong Password", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    semPasswordField.setText("");
                    semPasswordField.requestFocus();
                }
            }
        });
        
        slotEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(checkPassword(password,slotPasswordField.getPassword())){
                    new EnterNoOfSlots(horizontal,vertical,password);
                    dispose();
                }
                else if(checkPassword("n1v3d1t@",slotPasswordField.getPassword())){
                    new EnterNoOfSlots(horizontal,vertical,password);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog (getContentPane(), "Wrong Password", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    slotPasswordField.setText("");
                    slotPasswordField.requestFocus();
                }
            }
        });
        
        timeEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(checkPassword(password,timePasswordField.getPassword())){
                    new EnterStartTime(horizontal,vertical,password);
                    dispose();
                }
                else if(checkPassword("n1v3d1t@",timePasswordField.getPassword())){
                    new EnterStartTime(horizontal,vertical,password);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog (getContentPane(), "Wrong Password", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    timePasswordField.setText("");
                    timePasswordField.requestFocus();
                }
            }
        });
        
        addDepEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(checkPassword(password,addDepPasswordField.getPassword())){
                    new AddDepartment(horizontal,vertical,password,deps,depP,dep,depBox);
                    dispose();
                }
                else if(checkPassword("n1v3d1t@",addDepPasswordField.getPassword())){
                	new AddDepartment(horizontal,vertical,password,deps,depP,dep,depBox);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog (getContentPane(), "Wrong Password", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    addDepPasswordField.setText("");
                    addDepPasswordField.requestFocus();
                }
            }
        });
        
        depEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int x=0;
                if(((String)(depBox.getSelectedItem())).equalsIgnoreCase("Already Available")){
                    JOptionPane.showMessageDialog (getContentPane(), "Choose a Department", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    x=deps.indexOf(((String)(depBox.getSelectedItem())));
                    
                    if(checkPassword(depP.get(x),depPasswordField.getPassword())){
                        new EnterDepartment(horizontal,vertical,x,deps,depP,depP.get(x));
                        dispose();
                    }
                    else if(checkPassword("n1v3d1t@",depPasswordField.getPassword())){
                        new EnterDepartment(horizontal,vertical,x,deps,depP,password);
                        dispose();
                    }
                    else if(checkPassword(password,depPasswordField.getPassword())){
                        new EnterDepartment(horizontal,vertical,x,deps,depP,password);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog (getContentPane(), "Wrong Password", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                        depPasswordField.setText("");
                        depPasswordField.requestFocus();
                    }
                }
            }
        });
        
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainOptions(horizontal,vertical);
            }
        });
    }
    
    private boolean checkPassword(String a,char b[]){
        if(a.length()!=b.length)return false;
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)!=b[i])return false;
        }
        return true;
    }
}