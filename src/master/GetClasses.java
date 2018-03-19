package master;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


/*
 * string = string : same name of classes ; checked; WORKED
 * integer in n.o.periods && n.o.sections; checked; WORKED
 * entered in the file; algo \t \n ; WORKED
 */
public class GetClasses {
	
	private int totalDepartments,noOfClasses[],noOfSections[][],noOfPeriods[][],
			horizontal,vertical,e;
	private String departments[],rooms[],classes[][],encoder;
	private GetClasses heads[],prev,next;
	private JPanel panel;
	private JTextField name,sections,periods;
	private JScrollPane scroll;
	private DataInputStream dis;
	private DataOutputStream dos;
	private JFrame frame;
	
	
	public GetClasses(){
		name=new JTextField();
		name.setMaximumSize(new Dimension(500,30));
		name.setMinimumSize(new Dimension(500,30));
		name.setPreferredSize(new Dimension(500,30));
		
		sections=new JTextField();
		sections.setMaximumSize(new Dimension(100,30));
		sections.setMinimumSize(new Dimension(100,30));
		sections.setPreferredSize(new Dimension(100,30));
		
		periods=new JTextField();
		periods.setMaximumSize(new Dimension(100,30));
		periods.setMinimumSize(new Dimension(100,30));
		periods.setPreferredSize(new Dimension(100,30));
		
		prev=next=null;
	}
	
	public static void main(String args[]){
		GetClasses ob=new GetClasses();
		String a[]=new String[]{"p1","p2","p3","p4","p5"};
		String b[]=new String[]{"a","b"};
		int c[]=new int[]{2,2};
		ob.execute(10, 10, a, b, c);
	}
	
	public boolean execute(int h,int v,String room[],String deps[],int noc[]){
		horizontal=h;vertical=v;
		departments=deps;
		rooms=room;
		totalDepartments=deps.length;
		noOfClasses=noc;
		heads=new GetClasses[totalDepartments];
		encoder="Aryan";
		
		getSections();
		
		return true;
	}
	
	private void getSections(){
		frame=new JFrame();
		frame.setSize(horizontal,vertical);
		frame.setDefaultCloseOperation(3);
		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		scroll=new JScrollPane(panel);
		
		JLabel l=new JLabel("Enter -- classes".toUpperCase());
		l.setFont(new Font("Times New Roman",1,30));
		JPanel p=new JPanel();p.setLayout(new FlowLayout());
		p.add(l);
		panel.add(p);
		setPanel(0);
		
		JButton enter=new JButton("Enter");
		panel.add(Box.createRigidArea(new Dimension(20,40)));
		panel.add(enter);
		panel.add(Box.createRigidArea(new Dimension(20,60)));
		enter.addKeyListener(new KeyListener(){
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
		
		enter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0;i<totalDepartments;i++){
					GetClasses ob=heads[i];
					for(int j=0;j<noOfClasses[i];j++){
						String a=ob.name.getText();
						if(a.length()==0){
							JOptionPane.showMessageDialog (null, "Class no. "+(j+1)+"\n"
									+ "at\n"+departments[i]+"\n"
									+ " should have a name.\n", "Error", JOptionPane.ERROR_MESSAGE);
							ob.name.requestFocus();
							return;
						}
						ob=ob.next;
					}
					ob=heads[i];
					for(int j=0;j<noOfClasses[i];j++){
						String a=ob.sections.getText();
						if(!isPositiveInteger(a)){
							JOptionPane.showMessageDialog (null, "No. of sections in \n"
									+ "Class no. "+(j+1)+"\n"
									+ "at\n"+departments[i]+"\n"
									+ " should be Integer.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
							ob.sections.requestFocus();
							return;
						}
						else if(Integer.parseInt(a)==0){
							JOptionPane.showMessageDialog (null, "No. of sections in \n"
									+ "Class no. "+(j+1)+"\n"
									+ "at\n"+departments[i]+"\n"
									+ " should be greater than zero.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
							ob.sections.requestFocus();
							return;
						}
						ob=ob.next;
					}
					ob=heads[i];
					for(int j=0;j<noOfClasses[i];j++){
						String a=ob.periods.getText();
						if(!isPositiveInteger(a)){
							JOptionPane.showMessageDialog (null, "No. of periodss in \n"
									+ "Class no. "+(j+1)+"\n"
									+ "at\n"+departments[i]+"\n"
									+ " should be Integer.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
							ob.periods.requestFocus();
							return;
						}
						else if(Integer.parseInt(a)==0){
							JOptionPane.showMessageDialog (null, "No. of periods in \n"
									+ "Class no. "+(j+1)+"\n"
									+ "at\n"+departments[i]+"\n"
									+ " should be greater than zero.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
							ob.sections.requestFocus();
							return;
						}
						ob=ob.next;
					}
				}
				for(int i=0;i<totalDepartments;i++){
					GetClasses ob=heads[i];
					for(int j=0;j<noOfClasses[i];j++){
						for(int k=0;k<totalDepartments;k++){
							GetClasses ob2=heads[k];
							for(int l=0;l<noOfClasses[k];l++){
								if(i==k)if(j==l)break;
								if(ob.name.getText().
										equalsIgnoreCase
											(ob2.name.getText()))
								{
									JOptionPane.showMessageDialog (null, "Name(s) of \n"
											+ "Class no. "+(j+1)+"\n"
											+ "at\n"+departments[i]+"\n"
											+ "and\n"
											+ "Class no. "+(l+1)+"\n"
											+ "at\n"+departments[k]+"\n"
											+ "are same\n"
											+ "They must be different.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
									ob2.name.requestFocus();
									return;
								}
								ob2=ob2.next;
							}
						}
						ob=ob.next;
					}
				}
				int n=0;
				for(int i=0;i<totalDepartments;i++){
					n=n>noOfClasses[i]?n:noOfClasses[i];
				}
				classes=new String[totalDepartments][n];
				noOfSections=new int[totalDepartments][n];
				noOfPeriods=new int[totalDepartments][n];
				for(int i=0;i<totalDepartments;i++){
					GetClasses ob=heads[i];
					for(int j=0;j<noOfClasses[i];j++){
						classes[i][j]=ob.name.getText();
						noOfSections[i][j]=Integer.parseInt(ob.sections.getText());
						noOfPeriods[i][j]=Integer.parseInt(ob.periods.getText());
						ob=ob.next;
					}
				}
				
				boolean x=askToSave();
				if(x)frame.dispose();
			}
		});
		
		frame.add(scroll);
		
		frame.setVisible(true);
	}
	
	private boolean askToSave(){
		int o=JOptionPane.showConfirmDialog(null, "Save master time-table details?",
				"Save", JOptionPane.YES_NO_OPTION);
		try {
			dos=new DataOutputStream(new FileOutputStream("mtt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		e=0;
		if(o==JOptionPane.YES_OPTION){
			for(int i=0;i<totalDepartments;i++){
				GetClasses ob=heads[i];
				String b=departments[i]+"\t";
				for(int j=0;j<noOfClasses[i];j++){
					b=b+ob.name.getText()+"\t"
							+ob.sections.getText()+"\t"
							+ob.periods.getText();
					if(j!=noOfClasses[i]-1)b=b+"/";
				}
				try {
					encrypt(b+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int o2=JOptionPane.showConfirmDialog (null, "Saved.\n"
					+ "Generate master Time Table?", "Saved", JOptionPane.YES_NO_OPTION);
			if(o2==JOptionPane.YES_OPTION){
				frame.dispose();
				new GenerateMaster(horizontal,vertical,rooms,departments,noOfClasses,classes,noOfSections,noOfPeriods);
				return true;
			}
		}
		return false;
	}
	
	private void setPanel(int n){
		if(n==totalDepartments)return;
		
		JPanel pane=new JPanel();
		pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
		
		JLabel l=new JLabel(departments[n]);
		pane.add(l);
		pane.add(Box.createRigidArea(new Dimension(20,20)));
		
		heads[n]=null;GetClasses tail=null;
		
		for(int i=0;i<noOfClasses[n];i++){
			JPanel p=new JPanel();
			p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
			JLabel sNo=new JLabel(Integer.toString(i+1)+"    ");
			JLabel nam=new JLabel("Name"+"    ");
			JLabel nos=new JLabel("    "+"No. of sections"+"    ");
			JLabel nop=new JLabel("    "+"Total no. of periods in a week"+"    ");
			
			GetClasses ob=new GetClasses();
			
			ob.name.addKeyListener(new KeyListener(){
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
			
			ob.sections.addKeyListener(new KeyListener(){
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
			
			ob.periods.addKeyListener(new KeyListener(){
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
			
			if(i==0){
				heads[n]=tail=ob;
			}
			else{
				tail.next=ob;
				ob.prev=tail;
				tail=ob;
			}
			
			p.add(sNo);
			p.add(nam);
			p.add(ob.name);
			p.add(nos);
			p.add(ob.sections);
			p.add(nop);
			p.add(ob.periods);
			
			pane.add(p);
			pane.add(Box.createRigidArea(new Dimension(20,20)));
		}
		
		panel.add(pane);
		panel.add(Box.createRigidArea(new Dimension(20,20)));
		
		setPanel(n+1);
	}
	
	private boolean isPositiveInteger(String a){
		if(a.length()==0)return false;
		for(int i=0;i<a.length();i++)
			if(a.charAt(i)<48 || a.charAt(i)>57)
				return false;
		return true;
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
}
