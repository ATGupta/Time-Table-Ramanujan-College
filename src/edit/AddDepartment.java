package edit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.File;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;


class AddDepartment extends JFrame {
    
    private int horizontal,vertical,e;
    private String password,dep[],encoder,entered;
    private Vector<String> deps,depP;
    private JComboBox depBox;
    private JButton changeAdminPass,change,add,remove,save,back;
    private JLabel old,new1,new2,allowed,dep1,dep2;
    private JPasswordField oldPass,newPass1,newPass2,depPass1,depPass2;
    private JTextField nameOfDep;
    private DataOutputStream dos;

    public AddDepartment(int h, int v, String p, Vector<String> ds, Vector<String>dP, String[] d, JComboBox dB) {
        horizontal=h;vertical=v;
        password=p;
        deps=ds;
        dep=d;
        encoder="Aryan";
        depBox=dB;depBox.setVisible(true);
        depP=dP;
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
        add=new JButton("Add Department");
        add.setBackground(new Color(255,255,255));
        
        nameOfDep=new JTextField();
        nameOfDep.setMaximumSize(new Dimension(200,30));
        nameOfDep.setMinimumSize(new Dimension(200,30));
        nameOfDep.setPreferredSize(new Dimension(200,30));
        
        dep1=new JLabel("Enter Password");
        dep1.setForeground(new Color(255,255,255));
        dep1.setVisible(false);
        
        depPass1=new JPasswordField();
        depPass1.setMaximumSize(new Dimension(200,30));
        depPass1.setMinimumSize(new Dimension(200,30));
        depPass1.setPreferredSize(new Dimension(200,30));
        depPass1.setVisible(false);
        
        dep2=new JLabel("Re-enter Password");
        dep2.setForeground(new Color(255,255,255));
        dep2.setVisible(false);
        
        depPass2=new JPasswordField();
        depPass2.setMaximumSize(new Dimension(200,30));
        depPass2.setMinimumSize(new Dimension(200,30));
        depPass2.setPreferredSize(new Dimension(200,30));
        depPass2.setVisible(false);
        
        save=new JButton("Save");
        save.setBackground(new Color(255,255,255));
        save.setVisible(false);
        
        allowed=new JLabel();
        
        remove=new JButton("Remove Department");
        remove.setBackground(new Color(255,255,255));
        
        changeAdminPass=new JButton("Change Admin Password");
        changeAdminPass.setBackground(new Color(255,255,255));
        
        old=new JLabel("Enter Old Password");
        old.setVisible(false);
        old.setForeground(new Color(255,255,255));
        
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
    
    private void setComponents(){
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p=new JPanel();
        p.setOpaque(false);
        p.setLayout(new FlowLayout());
        p.setMaximumSize(new Dimension(horizontal,100));
        p.setMinimumSize(new Dimension(horizontal,100));
        p.setPreferredSize(new Dimension(horizontal,100));
        p.add(nameOfDep);
        p.add(allowed);
        p.add(add);
        add(p);
        add(depPass1);
        
        add(dep1);
        dep1.setAlignmentX(dep1.CENTER_ALIGNMENT);
        
        add(depPass1);
        depPass1.setAlignmentX(depPass1.CENTER_ALIGNMENT);
        
        add(dep2);
        dep2.setAlignmentX(dep2.CENTER_ALIGNMENT);
        
        add(depPass2);
        depPass2.setAlignmentX(depPass2.CENTER_ALIGNMENT);
        
        add(save);
        save.setAlignmentX(save.CENTER_ALIGNMENT);
        
        add(Box.createRigidArea(new Dimension(10,20)));
        
        JPanel p2=new JPanel();
        p2.setOpaque(false);
        p2.setLayout(new FlowLayout());
        p2.setMaximumSize(new Dimension(horizontal,100));
        p2.setMinimumSize(new Dimension(horizontal,100));
        p2.setPreferredSize(new Dimension(horizontal,100));
        p2.add(depBox);
        p2.add(remove);
        add(p2);
        
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
        nameOfDep.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent arg0) {}
            public void keyReleased(KeyEvent arg0) {
                checkEntered();
            }
            public void keyTyped(KeyEvent arg0) {}
        });
        
        add.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!nameOfDep.getText().equals("")){
                    add.setVisible(false);
                    
                    dep1.setVisible(true);
                    depPass1.setVisible(true);
                    dep2.setVisible(true);
                    depPass2.setVisible(true);
                    save.setVisible(true);
                    nameOfDep.setEditable(false);
                    
                    depPass1.requestFocus();
                }
            }
        });
		
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String check=allowed.getText();
                if(depPass1.getPassword().length==0){
                    JOptionPane.showMessageDialog (getContentPane(), "Password(s) must be entered", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    depPass1.setText("");
                    depPass2.setText("");
                }
                else if(!checkPassword(depPass1.getPassword(),depPass2.getPassword())){
                    JOptionPane.showMessageDialog (getContentPane(), "Passwords mismatch", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    depPass1.setText("");
                    depPass2.setText("");
                }
                else if(check.compareTo("Allowed")==0 && checkPassword(depPass1.getPassword(),depPass2.getPassword())){
                    allowed.setText("Added");
                    allowed.setVerticalAlignment(SwingConstants.CENTER);
                    allowed.setForeground(Color.WHITE);
                    
                    addEntered();
                    new File(entered).mkdir();
                    printEntered();
                    
                    JOptionPane.showMessageDialog (getContentPane(), "Department Saved", "Text Read Error", JOptionPane.ERROR_MESSAGE);
                    add.setVisible(true);
                    
                    dep1.setVisible(false);
                    depPass1.setVisible(false);
                    dep2.setVisible(false);
                    depPass2.setVisible(false);
                    save.setVisible(false);
                    nameOfDep.setEditable(true);
                    nameOfDep.setText("");
                    nameOfDep.requestFocus();
                }
            }
        });
        
        remove.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!((String)(depBox.getSelectedItem())).equalsIgnoreCase("Already Available")){
                    entered=(String)depBox.getSelectedItem();
                    int i=depBox.getSelectedIndex();
                    depBox.removeItemAt(i);
                    deps.remove(i-1);
                    depP.remove(i-1);
                    new File(entered).delete();
                    printEntered();
                    JOptionPane.showMessageDialog (getContentPane(), "Department Removed", "Text Read Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void encrypt(String a)throws IOException{
        for(int i=0;i<a.length();i++){
            int x=(int)a.charAt(i)+(int)encoder.charAt(e);
            dos.write(x);
            e=(e+1)%encoder.length();
        }
    }
    
    private void checkEntered(){
        entered=nameOfDep.getText();
        int t=0;
        for(int i=0;i<deps.size();i++){
            if(deps.get(i).equalsIgnoreCase(entered)){
                t=1;break;
            }
        }
        if(t==1 || entered.compareTo("")==0){
            allowed.setText("Not Allowed");
            allowed.setVerticalAlignment(SwingConstants.CENTER);
            allowed.setForeground(Color.RED);
        }
        else {
            allowed.setText("Allowed");
            allowed.setVerticalAlignment(SwingConstants.CENTER);
            allowed.setForeground(Color.GREEN);
        }
    }
    
    private void addEntered(){
        entered=nameOfDep.getText();
        deps.add(entered);
        depBox.addItem(entered);
        String a="";
        for(int i=0;i<depPass1.getPassword().length;i++){
            a=a+depPass1.getPassword()[i];
        }
        depP.add(a);
    }
    
    private void printEntered(){
        
        try {
            dos=new DataOutputStream(new FileOutputStream("depPassword"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        for(int i=0;i<depP.size();i++){
            try {
                encrypt(depP.get(i)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            dos=new DataOutputStream(new FileOutputStream("deps"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        e=0;
        for(int i=0;i<deps.size();i++){
            try {
                encrypt(deps.get(i)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(AddDepartment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
