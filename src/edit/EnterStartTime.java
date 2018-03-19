package edit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class EnterStartTime extends JFrame {
    
    private int horizontal,vertical,e;
    private JButton enter,changeAdminPass,change,back;
    private JPasswordField oldPass,newPass1,newPass2;
    private JLabel old,new1,new2,oldTime;
    private JTextField time;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String encoder,password;
    
    public EnterStartTime(int h, int v, String p) {
        horizontal=h;vertical=v;
        encoder="Aryan";
        password=p;
        setSize(horizontal,vertical);
        
        getContentPane().setBackground(new Color(0,0,0));
        initialize();
        setComponents();
        actionListeners();
        
        setWindowIcon();
        
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setWindowIcon(){
    	ImageIcon image=new ImageIcon("icon");
    	setIconImage(image.getImage());
    }
    
    private void initialize(){
        oldTime=new JLabel("Old Starting Time = " + getOldStartTime());
        oldTime.setForeground(new Color(255,255,255));
        
        time=new JTextField();
        time.setMaximumSize(new Dimension(200,30));
        time.setMinimumSize(new Dimension(200,30));
        time.setPreferredSize(new Dimension(200,30));
        
        enter=new JButton("Enter (24 hour format)");
        enter.setBackground(new Color(255,255,255));
        
        changeAdminPass=new JButton("Change Admin Password");
        changeAdminPass.setBackground(new Color(255,255,255));
        
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
    
    private String getOldStartTime(){
        try {
            dis=new DataInputStream(new FileInputStream("startTime"));
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
    
    private void setComponents(){
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(oldTime);
        oldTime.setAlignmentX(oldTime.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(time);
        time.setAlignmentX(time.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(enter);
        enter.setAlignmentX(enter.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(changeAdminPass);
        changeAdminPass.setAlignmentX(changeAdminPass.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(old);
        old.setAlignmentX(old.CENTER_ALIGNMENT);
        
        add(oldPass);
        oldPass.setAlignmentX(oldPass.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(new1);
        new1.setAlignmentX(new1.CENTER_ALIGNMENT);
        
        add(newPass1);
        newPass1.setAlignmentX(newPass1.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(new2);
        new2.setAlignmentX(new2.CENTER_ALIGNMENT);
        
        add(newPass2);
        newPass2.setAlignmentX(newPass2.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        add(change);
        change.setAlignmentX(change.CENTER_ALIGNMENT);
        
        JPanel p4=new JPanel();
        p4.setOpaque(false);
        p4.setLayout(new FlowLayout());
        p4.setMaximumSize(new Dimension(500,100));
        p4.add(back);
        add(p4);
    }
    
    private void actionListeners(){
        enter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(time.getText().equals("")){
                    JOptionPane.showMessageDialog (getContentPane(), "Nothing Entered", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(!isPositiveInteger(time.getText())){
                    JOptionPane.showMessageDialog (getContentPane(), "Entered time is INVALID", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    time.setText("");
                }
                else{
                    enterTime();
                    JOptionPane.showMessageDialog (getContentPane(), "Starting Time Chanaged", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    time.setText("");
                }
            }
        });
        
        changeAdminPass.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                changeAdminPass.setVisible(false);
                
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
                    writeAdminPassword();
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
    }
    	
    private boolean isPositiveInteger(String a){
        if(a.length()==0)return false;
        for(int i=0;i<a.length();i++)
            if(a.charAt(i)<48 || a.charAt(i)>57)
                return false;
        return true;
    }
    
    private void enterTime(){
        try {
            dos=new DataOutputStream(new FileOutputStream("startTime"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnterStartTime.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        try {
            encrypt(time.getText());
        } catch (IOException ex) {
            Logger.getLogger(EnterStartTime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void encrypt(String a)throws IOException{
        for(int i=0;i<a.length();i++){
            int x=(int)a.charAt(i)+(int)encoder.charAt(e);
            dos.write(x);
            e=(e+1)%encoder.length();
        }
    }
    
    private void writeAdminPassword(){
        try {
            dos=new DataOutputStream(new FileOutputStream("pass"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnterStartTime.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        String a="";
        for(int i=0;i<newPass1.getPassword().length;i++){
            a=a+newPass1.getPassword()[i];
        }
        try {
            encrypt(a);
        } catch (IOException ex) {
            Logger.getLogger(EnterStartTime.class.getName()).log(Level.SEVERE, null, ex);
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
}