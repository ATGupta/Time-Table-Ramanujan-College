package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import display.FreeRooms;
import display.MainDisplayPage;
import edit.MainEditPage;
import master.GetDepartments;
import misc.WriteMyID;


public class MainOptions extends JFrame {
    
    private int horizontal, vertical;
    private JButton tt,edit,disNOFreeRooms,masterTimeTableEnter;
    private ImageIcon backImage;
    
    public MainOptions(int h,int v) {
        horizontal=h;vertical=v;
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
        tt=new JButton("Display Time Table");
        tt.setMinimumSize(new Dimension(200,30));
        tt.setMaximumSize(new Dimension(200,30));
        tt.setPreferredSize(new Dimension(200,30));
        tt.setBackground(new Color(255,255,255));
        
        edit=new JButton("Edit");
        edit.setMinimumSize(new Dimension(200,30));
        edit.setMaximumSize(new Dimension(200,30));
        edit.setPreferredSize(new Dimension(200,30));
        edit.setBackground(new Color(255,255,255));
        
        disNOFreeRooms=new JButton("Display Free Rooms");
        disNOFreeRooms.setMinimumSize(new Dimension(200,30));
        disNOFreeRooms.setMaximumSize(new Dimension(200,30));
        disNOFreeRooms.setPreferredSize(new Dimension(200,30));
        disNOFreeRooms.setBackground(new Color(255,255,255));
        
        masterTimeTableEnter=new JButton("Make Master Time Table");
        masterTimeTableEnter.setMinimumSize(new Dimension(200,30));
        masterTimeTableEnter.setMaximumSize(new Dimension(200,30));
        masterTimeTableEnter.setPreferredSize(new Dimension(200,30));
        masterTimeTableEnter.setBackground(new Color(255,255,255));
    }
    
    private void setComponents(){
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(10,100)));
        add(tt);
        tt.setAlignmentX(tt.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(10,100)));
        add(disNOFreeRooms);
        disNOFreeRooms.setAlignmentX(disNOFreeRooms.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(10,100)));
        add(edit);
        edit.setAlignmentX(edit.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(10,100)));
        add(masterTimeTableEnter);
        masterTimeTableEnter.setAlignmentX(masterTimeTableEnter.CENTER_ALIGNMENT);
    }
    
    private void actionListeners(){
        tt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new MainDisplayPage(horizontal,vertical);
                dispose();
            }
        });
        
        edit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	edit.setEnabled(false);
                new WriteMyID();
                new MainEditPage(horizontal,vertical);
                dispose();
            }
        });
        
        disNOFreeRooms.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new FreeRooms(horizontal,vertical);
                dispose();
            }
        });
        
        masterTimeTableEnter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				GetDepartments ob=new GetDepartments();
				boolean x=ob.execute(horizontal,vertical);
				if(x)dispose();
			}
        });
    }
}