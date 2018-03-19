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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import misc.SetArray;
import misc.TimeTableTokenizer;


public class EditTimeTable extends JFrame {
    
    private int horizontal,vertical,depIndex,nos,e;
    private Vector<String> teacherVector,roomVector,subjectVector,paperVector,deps,depP;
    private JComboBox teacherBox,roomBox,subjectBox,paperBox;
    private JButton show,save,back;
    private JPanel panel;
    private JLabel teacherLabelA[][],roomLabelA[][],paperLabelA[][];
    private DataInputStream dis;
    private DataOutputStream dos;
    private String encoder,subject,teacherCheckA[][][],roomCheckA[][][],paperCopy,teacherCopy,roomCopy,passwordEntered;
    private Timer timer;
    private JScrollPane scroll;
    
    public EditTimeTable(int h, int v, Vector<String> tV, Vector<String> rV, Vector<String> sV, Vector<String> pV, Vector<String> d, int dI,JComboBox tB,JComboBox rB,JComboBox sB,JComboBox pB,Vector<String> DP,String pE) {
        horizontal=h;vertical=v;
        teacherVector=tV;roomVector=rV;subjectVector=sV;paperVector=pV;
        deps=d;
        depP=DP;
        teacherBox=tB;roomBox=rB;subjectBox=sB;paperBox=pB;
        depIndex=dI;
        encoder="Aryan";
        paperCopy=teacherCopy=roomCopy="";
        passwordEntered=pE;
        setSize(horizontal,vertical);
        
        getContentPane().setBackground(new Color(0,0,0));
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
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
        
        nos=getNoOfSlots();
        
        initializeCheckArrays();
        
        show=new JButton("Show");
        show.setBackground(new Color(255,255,255));
        
        save=new JButton("Save");
        save.setBackground(new Color(255,255,255));
        save.setVisible(false);
        
        panel=new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout());
        
        back=new JButton("Back");
        back.setBackground(new Color(255,255,255));
    }
    
    private void setComponents(){
        JPanel p=new JPanel();
        p.setOpaque(false);
        p.setLayout(new FlowLayout());
        subjectBox.setVisible(true);
        p.add(subjectBox);
        p.add(show);
        
        JPanel p1=new JPanel();
        p1.setOpaque(false);
        p1.setLayout(new FlowLayout());
        p1.add(save);
        
        scroll=new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getHorizontalScrollBar().setOpaque(false);
        scroll.setPreferredSize(new Dimension(horizontal,vertical*8/10));
        
        JPanel p2=new JPanel();
        p2.setOpaque(false);
        p2.add(p);
        p2.add(scroll);
        p2.add(p1);
        add(p2);
        
        JPanel p3=new JPanel();
        p3.setOpaque(false);
        p3.setLayout(new FlowLayout());
        p3.setMaximumSize(new Dimension(500,100));
        p3.add(back);
        add(p3);
    }
    
    private void actionListeners(){
        show.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!((String)subjectBox.getSelectedItem()).equalsIgnoreCase("Classes")){
                    subject=(String)subjectBox.getSelectedItem();
                    panel.setVisible(false);
                    initializePanel();
                    panel.setVisible(true);
                    save.setVisible(true);
                }
                
            }
        });
        
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                saveAll();//includes initializeCheckArrays();
                JOptionPane.showMessageDialog (getContentPane(), "Saved", "Save Notice", JOptionPane.OK_OPTION);
            }
        });
        
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EnterDepartment(horizontal,vertical,depIndex,deps,depP,passwordEntered);
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
        
        save.addKeyListener(new KeyListener(){
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
        
        show.addKeyListener(new KeyListener(){
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
    
    private void initializePanel(){
        initializeLabels();
        
    	panel.removeAll();
        
    	String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    	
        JPanel p2=new JPanel();
        p2.setOpaque(false);
        p2.setLayout(new BoxLayout(p2,BoxLayout.Y_AXIS));
        for(int i=0;i<=7;i++){
            
            JPanel p=new JPanel();
            p.setOpaque(false);
            p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
            
            
            JLabel l;
            if(i==0)l=new JLabel();
            else{
            	l=new JLabel(days[i-1]);
            	//p.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        	}
            l.setForeground(new Color(255,255,255));
            JPanel hidedp=new JPanel();
            hidedp.setOpaque(false);
            hidedp.setLayout(new FlowLayout());
            hidedp.add(l);
            p.add(hidedp);
            
            p.setMaximumSize(new Dimension(150, 150));
            p.setMinimumSize(new Dimension(150, 150));
            p.setPreferredSize(new Dimension(150, 150));
            
            p2.add(p);
        }
        panel.add(p2);
        
        for(int p=0;p<nos;p++){
            JPanel pane=new JPanel();
            pane.setOpaque(false);
            pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
            for(int d=0;d<7;d++){
                if(d==0){
                    JPanel per=new JPanel();
                    per.setOpaque(false);
                    JLabel l=new JLabel(("Period "+Integer.toString(p+1)));
                    l.setForeground(new Color(255,255,255));
                    per.add(l);
                    
                    per.setMaximumSize(new Dimension(300, 30));
                    per.setMinimumSize(new Dimension(300, 30));
                    per.setPreferredSize(new Dimension(300, 30));
                    
                	per.setBorder(BorderFactory.createLineBorder(new Color(255,255,255)));
                    
                    l.setAlignmentX(l.CENTER_ALIGNMENT);
                    pane.add(per);
                }
                
                JPanel per=new JPanel();
                per.setOpaque(false);
                per.setLayout(new BoxLayout(per,BoxLayout.Y_AXIS));
                
                JPanel t=new JPanel();
                t.setOpaque(false);
                t.setOpaque(false);
                t.setLayout(new FlowLayout());
                t.add(teacherLabelA[d][p]);
                
                JPanel r=new JPanel();
                r.setOpaque(false);
                r.setLayout(new FlowLayout());
                r.add(roomLabelA[d][p]);
                
                JPanel pap=new JPanel();
                pap.setOpaque(false);
                pap.setLayout(new FlowLayout());
                pap.add(paperLabelA[d][p]);
                
                final int a=d,b=p;
                JButton add=new JButton("Add");
                add.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						initializeCheckArrays();
						addDialogue(a,b);
						saveAll();//includes initializeCheckArrays();
					}
                });
                
                add.addKeyListener(new KeyListener(){
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
                
                JButton edit=new JButton("Edit");
                edit.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						initializeCheckArrays();
						editDialogue(a,b);
						saveAll();//includes initializeCheckArrays();
					}
                });
                
                edit.addKeyListener(new KeyListener(){
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
                
                JPanel addrem=new JPanel();
                addrem.setOpaque(false);
                addrem.setLayout(new FlowLayout());
                addrem.add(add);
                addrem.add(edit);
                
                JButton copy=new JButton("Copy");
                copy.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						paperCopy=paperLabelA[a][b].getText();
						teacherCopy=teacherLabelA[a][b].getText();
						roomCopy=roomLabelA[a][b].getText();
					}
                });
                
                copy.addKeyListener(new KeyListener(){
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
                
                JButton paste=new JButton("Paste");
                paste.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						if(paperCopy.length()==0 && teacherCopy.length()==0 && roomCopy.length()==0)
							return;
						String notice="";
						TimeTableTokenizer teacherToCopy=new TimeTableTokenizer(teacherCopy);
						TimeTableTokenizer teacherAlready=new TimeTableTokenizer(teacherLabelA[a][b].getText());
						TimeTableTokenizer roomToCopy=new TimeTableTokenizer(roomCopy);
						TimeTableTokenizer roomAlready=new TimeTableTokenizer(roomLabelA[a][b].getText());
						
						for(int i=0;i<teacherToCopy.countTokens();i++){
							int t=0;
							for(int j=0;j<teacherAlready.countTokens();j++){
								if(teacherToCopy.tokenAt(i).equals(teacherAlready.tokenAt(j))){
									t=1;
									break;
								}
							}
							if(t==1)continue;
							if(teacherToCopy.tokenAt(i).length()==0)continue;
							
							int pos=teacherVector.indexOf(teacherToCopy.tokenAt(i));
							if(teacherCheckA[pos][a][b].length()!=0)
								notice=notice+teacherCheckA[pos][a][b]+"\n";
						}
						
						for(int i=0;i<roomToCopy.countTokens();i++){
							int t=0;
							for(int j=0;j<roomAlready.countTokens();j++){
								if(roomToCopy.tokenAt(i).equals(roomAlready.tokenAt(j))){
									t=1;
									break;
								}
							}
							if(t==1)continue;
							if(roomToCopy.tokenAt(i).length()==0)continue;
							
							int pos=roomVector.indexOf(roomToCopy.tokenAt(i));
							if(roomCheckA[pos][a][b].length()!=0)
								notice=notice+roomCheckA[pos][a][b]+"\n";
						}
						
						if(notice.length()!=0){
							JOptionPane.showMessageDialog (getContentPane(), notice, "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						paperLabelA[a][b].setText(paperCopy);
						teacherLabelA[a][b].setText(teacherCopy);
						roomLabelA[a][b].setText(roomCopy);
						saveAll();//includes initializeCheckArrays();
					}
                });
                
                paste.addKeyListener(new KeyListener(){
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
                
                JButton empty=new JButton("Empty Slot");
                empty.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						paperLabelA[a][b].setText("");
						teacherLabelA[a][b].setText("");
						roomLabelA[a][b].setText("");
						saveAll();//includes initializeCheckArrays();
					}
                });
                
                empty.addKeyListener(new KeyListener(){
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
                
                JPanel copyPaste=new JPanel();
                copyPaste.setOpaque(false);
                copyPaste.setLayout(new FlowLayout());
                copyPaste.add(copy);
                copyPaste.add(paste);
                copyPaste.add(empty);
                
                per.add(pap);
                per.add(t);
                per.add(r);
                per.add(addrem);
                per.add(copyPaste);
                
                per.setBorder(BorderFactory.createLineBorder(new Color(255,255,255)));
                
                per.setMaximumSize(new Dimension(300, 150));
                per.setMinimumSize(new Dimension(300, 150));
                per.setPreferredSize(new Dimension(300, 150));
                
                pane.add(per);
            }
            panel.add(pane);
        }
    }

	private void addDialogue(final int d,final int p){
    	final JComboBox paper=copyComboBox(paperBox);
    	
    	final JComboBox teacher=copyComboBox(teacherBox);
    	
    	teacher.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String tt=(String)teacher.getSelectedItem();
				int i=teacherVector.indexOf(tt);
				if(tt.equals("Teachers"))return;
				
				TimeTableTokenizer ttt=new TimeTableTokenizer(teacherLabelA[d][p].getText());
				for(int j=0;j<ttt.countTokens();j++)
					if(ttt.tokenAt(j).equals(tt)){
						teacher.setSelectedIndex(0);
						JOptionPane.showMessageDialog (getContentPane(), "Same Teacher cannot be repeated in a slot.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				if(teacherCheckA[i][d][p].length()!=0){
					teacher.setSelectedIndex(0);
					JOptionPane.showMessageDialog (getContentPane(), "Teacher Already Busy\n\n"+teacherCheckA[i][d][p], "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				String g=teacherCheckA[i][d][p],b="";
				int n=0;
				for(int k=0;k<g.length();k++){
					if(g.charAt(k)=='\n'){
						n++;
						if(n==4)break;
						b="";
					}
					else b=b+g.charAt(i);
				}
				if(tt.equals(b)){
					teacher.setSelectedIndex(0);
					JOptionPane.showMessageDialog (getContentPane(), "Teacher Already Busy\n\n"+teacherCheckA[i][d][p], "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
    	});
    	
    	final JComboBox room=copyComboBox(roomBox);
    	
    	room.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String rr=(String)room.getSelectedItem();
				int i=roomVector.indexOf(rr);
				if(rr.equals("Rooms"))return;
				
				TimeTableTokenizer rrt=new TimeTableTokenizer(roomLabelA[d][p].getText());
				for(int j=0;j<rrt.countTokens();j++)
					if(rrt.tokenAt(j).equals(rr)){
						room.setSelectedIndex(0);
						JOptionPane.showMessageDialog (getContentPane(), "Same Room cannot be repeated in a slot.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				if(roomCheckA[i][d][p].length()!=0){
					room.setSelectedIndex(0);
					JOptionPane.showMessageDialog (getContentPane(), "Room Already Busy\n\n"+roomCheckA[i][d][p], "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				String g=roomCheckA[i][d][p],b="";
				int n=0;
				for(int k=0;k<g.length();k++){
					if(g.charAt(k)=='\n'){
						n++;
						if(n==5)break;
						b="";
					}
					else b=b+g.charAt(i);
				}
				if(rr.equals(b)){
					room.setSelectedIndex(0);
					JOptionPane.showMessageDialog (getContentPane(), "Room Already Busy\n\n"+roomCheckA[i][d][p], "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
    	});
    	
    	JComponent[] addDialogue=new JComponent[]{paper,teacher,room};
    	Object[] click=new Object[]{"Save"};
    	
    	int o=JOptionPane.showOptionDialog(getContentPane(), addDialogue, "Add Period", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, click, click[0]);
    	
    	if(o==JOptionPane.OK_OPTION){
			
			if(((String)(paper.getSelectedItem())).equalsIgnoreCase("Papers") 
					&& ((String)(teacher.getSelectedItem())).equalsIgnoreCase("Teachers") 
					&& ((String)(room.getSelectedItem())).equalsIgnoreCase("Rooms"))
				return;
			
			if(!(paperLabelA[d][p].getText().length()==0
					&& teacherLabelA[d][p].getText().length()==0
					&& roomLabelA[d][p].getText().length()==0)){
				paperLabelA[d][p].setText(paperLabelA[d][p].getText()+"/");
				teacherLabelA[d][p].setText(teacherLabelA[d][p].getText()+"/");
				roomLabelA[d][p].setText(roomLabelA[d][p].getText()+"/");
    		}
			
			
			if(!((String)(paper.getSelectedItem())).equalsIgnoreCase("Papers"))
				paperLabelA[d][p].setText(paperLabelA[d][p].getText()+((String)(paper.getSelectedItem())));
			
			if(!((String)(teacher.getSelectedItem())).equalsIgnoreCase("Teachers"))
				teacherLabelA[d][p].setText(teacherLabelA[d][p].getText()+((String)(teacher.getSelectedItem())));
			
			if(!((String)(room.getSelectedItem())).equalsIgnoreCase("Rooms"))
				roomLabelA[d][p].setText(roomLabelA[d][p].getText()+((String)(room.getSelectedItem())));
			
    	}
    	else return;
    }
    
    private JComboBox copyComboBox(JComboBox cb) {
		int x=cb.getItemCount();
		String arr[]=new String[x];
		for(int i=0;i<x;i++){
			arr[i]=(String)cb.getItemAt(i);
		}
		JComboBox ncb=new JComboBox(arr);
		
		return ncb;
	}

	private void editDialogue(final int d, final int p) {
    	final TimeTableTokenizer pp=new TimeTableTokenizer(paperLabelA[d][p].getText());
    	final TimeTableTokenizer tt=new TimeTableTokenizer(teacherLabelA[d][p].getText());
    	final TimeTableTokenizer rr=new TimeTableTokenizer(roomLabelA[d][p].getText());
    	
    	final JPanel pan[]=new JPanel[pp.countTokens()];
    	JPanel panel=new JPanel();
    	final JComboBox[] paper=new JComboBox[pp.countTokens()];
    	final JComboBox[] room=new JComboBox[pp.countTokens()];
    	final JComboBox[] teacher=new JComboBox[pp.countTokens()];
    	
    	JButton remove[]=new JButton[pp.countTokens()]; 
    	
    	for(int i=0;i<pp.countTokens();i++){
    		pan[i]=new JPanel();
    		pan[i].setLayout(new BoxLayout(pan[i],BoxLayout.Y_AXIS));
    		panel.add(pan[i]);
    		
    		paper[i]=copyComboBox(paperBox);
    		if(pp.tokenAt(i).length()!=0)
    			paper[i].setSelectedItem(pp.tokenAt(i));
    		pan[i].add(paper[i]);
    		
    		teacher[i]=copyComboBox(teacherBox);
    		if(tt.tokenAt(i).length()!=0)
    			teacher[i].setSelectedItem(tt.tokenAt(i));
    		pan[i].add(teacher[i]);
    		
    		room[i]=copyComboBox(roomBox);
    		if(rr.tokenAt(i).length()!=0)
    			room[i].setSelectedItem(rr.tokenAt(i));
    		pan[i].add(room[i]);
    		
    		remove[i]=new JButton("Remove");
    		pan[i].add(remove[i]);
    	}
    	
    	for(int o=0;o<pp.countTokens();o++){
    		final String prevTeacher=(String)teacher[o].getSelectedItem();
    		final String prevRoom=(String)room[o].getSelectedItem();
    		final int l=o;
    		
    		/*
    		paper[l].addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent arg0) {
    				String p=(String)paper[l].getSelectedItem();
    				if(p.equals("Papers"))return;
    				
    				for(int j=0;j<pp.countTokens();j++){
    					if(j==l)continue;
    					if(((String)(paper[j].getSelectedItem())).equals(p)){
    						paper[l].setSelectedIndex(0);
    						JOptionPane.showMessageDialog (getContentPane(), "Same Paper cannot be repeated in a slot.", "Error", JOptionPane.ERROR_MESSAGE);
    					}
    				}
    			}
        	});
        	*/
    		
    		teacher[l].addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent arg0) {
    				String t=(String)teacher[l].getSelectedItem();
    				if(t.equals("Teachers"))return;
    				if(t.equals(prevTeacher))return;
    				int i=teacherVector.indexOf(t);
    				
    				for(int j=0;j<pp.countTokens();j++){
    					if(j==l)continue;
    					if(((String)(teacher[j].getSelectedItem())).equals(t)){
    						teacher[l].setSelectedIndex(0);
    						JOptionPane.showMessageDialog (getContentPane(), "Same Teacher cannot be repeated in a slot.", "Error", JOptionPane.ERROR_MESSAGE);
    						return;
    					}
    				}
    				
    				String g=teacherCheckA[i][d][p],b="";
    				int n=0;
    				for(int k=0;k<g.length();k++){
    					if(g.charAt(k)=='\n'){
    						n++;
        					if(n==4)break;
    						b="";
    					}
    					else b=b+g.charAt(k);
    				}
    				if(t.equals(b)){
						teacher[l].setSelectedIndex(0);
						JOptionPane.showMessageDialog (getContentPane(), "Teacher Already Busy\n\n"+teacherCheckA[i][d][p], "Error", JOptionPane.ERROR_MESSAGE);
    				}
    			}
        	});
    		
    		room[l].addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent arg0) {
    				String r=(String)room[l].getSelectedItem();
    				if(r.equals("Rooms"))return;
    				if(r.equals(prevRoom))return;
    				int i=roomVector.indexOf(r);
    				
    				for(int j=0;j<pp.countTokens();j++){
    					if(j==l)continue;
    					if(((String)(room[j].getSelectedItem())).equals(r)){
    						room[l].setSelectedIndex(0);
    						JOptionPane.showMessageDialog (getContentPane(), "Same Room cannot be repeated in a slot.", "Error", JOptionPane.ERROR_MESSAGE);
    						return;
    					}
    				}
    				
    				for(int h=0;h<pp.countTokens();h++){
    					String f=pp.tokenAt(h)+"\n"
    							+tt.tokenAt(h)+"\n"
    							+rr.tokenAt(h)+"\n";
    					if((f.equals(roomCheckA[i][d][p]))){
    						room[l].setSelectedIndex(0);
    						JOptionPane.showMessageDialog (getContentPane(), "Room Already Busy\n\n"+roomCheckA[i][d][p], "Error", JOptionPane.ERROR_MESSAGE);
    					}
    				}
    				
    				String g=roomCheckA[i][d][p],b="";
    				int n=0;
    				for(int k=0;k<g.length();k++){
    					if(g.charAt(k)=='\n'){
    						n++;
        					if(n==5)break;
    						b="";
    					}
    					else b=b+g.charAt(k);
    				}
    				if(r.equals(b)){
						room[l].setSelectedIndex(0);
						JOptionPane.showMessageDialog (getContentPane(), "Room Already Busy\n\n"+roomCheckA[i][d][p], "Error", JOptionPane.ERROR_MESSAGE);
    				}
    			}
        	});
    		
    		remove[l].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					paper[l].setSelectedIndex(0);
					teacher[l].setSelectedIndex(0);
					room[l].setSelectedIndex(0);
					pan[l].setVisible(false);
				}
    		});
    	}
    	
    	JComponent[] addDialogue=new JComponent[]{panel};
    	Object[] click=new Object[]{"Save"};
    	
    	int o=JOptionPane.showOptionDialog(getContentPane(), addDialogue, "Edit", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, click, click[0]);
    	
    	String p_s="",t_s="",r_s="";
    	int count=0;
    	if(o==JOptionPane.OK_OPTION){
			for(int i=0;i<pp.countTokens();i++){
				if(((String)(paper[i].getSelectedItem())).equalsIgnoreCase("Papers") 
						&& ((String)(teacher[i].getSelectedItem())).equalsIgnoreCase("Teachers") 
						&& ((String)(room[i].getSelectedItem())).equalsIgnoreCase("Rooms"))
					continue;
				
				if(count>0){
					p_s+="/";
					t_s+="/";
					r_s+="/";
				}
				
				if(!((String)(paper[i].getSelectedItem())).equalsIgnoreCase("Papers"))
					p_s+=(String)(paper[i].getSelectedItem());
				
				if(!((String)(teacher[i].getSelectedItem())).equalsIgnoreCase("Teachers"))
					t_s+=(String)(teacher[i].getSelectedItem());
				
				if(!((String)(room[i].getSelectedItem())).equalsIgnoreCase("Rooms"))
					r_s+=(String)(room[i].getSelectedItem());
				count++;
			}
    	}
    	else return;
    	
    	paperLabelA[d][p].setText(p_s);
    	teacherLabelA[d][p].setText(t_s);
    	roomLabelA[d][p].setText(r_s);
	}
    
    private void initializeLabels(){
        teacherLabelA=new JLabel[7][nos];
        for(int d=0;d<7;d++){
            for(int p=0;p<nos;p++){
                teacherLabelA[d][p]=new JLabel();
                teacherLabelA[d][p].setForeground(new Color(255,255,255));
            }
        }
        
        roomLabelA=new JLabel[7][nos];
        for(int d=0;d<7;d++){
            for(int p=0;p<nos;p++){
                roomLabelA[d][p]=new JLabel();
                roomLabelA[d][p].setForeground(new Color(255,255,255));
            }
        }
        
        paperLabelA=new JLabel[7][nos];
        for(int d=0;d<7;d++){
            for(int p=0;p<nos;p++){
                paperLabelA[d][p]=new JLabel();
                paperLabelA[d][p].setForeground(new Color(255,255,255));
            }
        }
        
        try {
            dis=new DataInputStream(new FileInputStream(deps.get(depIndex)+"/"+((String)(subjectBox.getSelectedItem()))));
        } catch (FileNotFoundException ex) {
            return;
        }
        e=0;
        String a=null;
        try {
            a=decrypt();
        } catch (IOException ex) {
            Logger.getLogger(EditTimeTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int n=0,d=0,p=0;
        String b="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='\n'){
                if(n%3==0){
                    paperLabelA[d][p].setText(b);
                }
                else if(n%3==1){
                    teacherLabelA[d][p].setText(b);
                }
                else if(n%3==2){
                    roomLabelA[d][p].setText(b);
                    p++;
                    if(p==nos){
                        p=0;d++;
                    }
                }
                n++;
                b="";
            }
            else b=b+a.charAt(i);
        }
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
        
        try {
			dis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
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
    
    private void saveAll(){
        try {
            dos=new DataOutputStream(new FileOutputStream(deps.get(depIndex)+"/"+subject));
        } catch (FileNotFoundException ex) {
            saveAll();
            return;
        }
        e=0;
        for(int d=0;d<7;d++){
            for(int p=0;p<nos;p++){
                try {
                	encrypt(paperLabelA[d][p].getText()+"\n");
	                encrypt(teacherLabelA[d][p].getText()+"\n");
	                encrypt(roomLabelA[d][p].getText()+"\n");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog (getContentPane(), "Error writing time table to files.", "Error", JOptionPane.ERROR_MESSAGE);
				}
                
            }
        }
        
        try {
			dos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        initializeCheckArrays();
        
        panel.setVisible(false);
        initializePanel();
        panel.setVisible(true);
    }
    
    private void encrypt(String a)throws IOException{
        for(int i=0;i<a.length();i++){
            int x=(int)a.charAt(i)+(int)encoder.charAt(e);
            dos.write(x);
            e=(e+1)%encoder.length();
        }
    }
    
    private void initializeCheckArrays(){
    	SetArray obj=new SetArray(deps.get(depIndex));
    	
    	teacherCheckA=obj.set(SetArray.CHECK_TEACHER_TEACHER_ARRAY);
    	roomCheckA=obj.set(SetArray.CHECK_ROOM_ROOM_ARRAY);
    }
}