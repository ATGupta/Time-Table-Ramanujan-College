package master;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edit.MainEditPage;


/*
 * exactly half no. of rooms, WORKED;
 * less than half, WORKED.
 * file deletion, WORKED.
 * folder deletion - to delete folder, first make it empty, WORKED, NOT in eclipse, but in bluej and by .bat (through cmd).
 * 
 */
public class GetDepartments{
	
	private int totalDepartments,totalRooms,
		horizontal,vertical;
	private DataInputStream dis;
	private int e;
	private String encoder;
	private GetDepartments next,prev;//for getNames linked list
	private JTextField text,cNoText;//for getNames linked list
	private String value;//for getNames linked list
	private String rooms[];
	
	public boolean execute(int h,int v){
		
		encoder="Aryan";
		horizontal=h;vertical=v;
		
		boolean x=passwordOptionPane();
		if(!x)return false;
		
		x=askIfTimeTableAlreadyPresent();
		if(!x)return false;
		
		x=getNoOfRooms();
		if(!x)return false;
		
		getRoomsNamesForm(totalRooms,"Rooms");
		
		return true;
	}
	
	public GetDepartments(){
		prev=next=null;
		
		text=new JTextField();
		text.setMaximumSize(new Dimension(500,30));
		text.setMinimumSize(new Dimension(500,30));
		text.setPreferredSize(new Dimension(500,30));
		
		cNoText=new JTextField();
		cNoText.setMaximumSize(new Dimension(100,30));
		cNoText.setMinimumSize(new Dimension(100,30));
		cNoText.setPreferredSize(new Dimension(100,30));
	}
	
	private boolean passwordOptionPane() {
		
		String password=initializeAdminPassword();
		JPasswordField pass=new JPasswordField();
		pass.setMaximumSize(new Dimension(200,30));
		pass.setMinimumSize(new Dimension(200,30));
		pass.setPreferredSize(new Dimension(200,30));
		pass.requestFocus(true);
		Object[] enter=new Object[]{"Enter Password"};
		int o=JOptionPane.showOptionDialog(null, pass, "Enter Password", JOptionPane.OK_OPTION, JOptionPane.OK_OPTION, null, enter, enter[0]);
		if(o==JOptionPane.OK_OPTION){
			if(!checkPassword(password,pass.getPassword())){
				JOptionPane.showMessageDialog (null, "Wrong Password Entered.\n", "Password Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		}
		else return false;
		
	}
	
	private boolean checkPassword(String a,char b[]){
		if(a.length()!=b.length)return false;
		for(int i=0;i<a.length();i++){
			if(a.charAt(i)!=b[i])return false;
		}
		return true;
	}
	
	private String initializeAdminPassword(){
        String password="";
        try {
			dis=new DataInputStream(new FileInputStream("pass"));
		} catch (FileNotFoundException ex) {
			return null;
		}
		e=0;
		try {
			password=decrypt();
		} catch (IOException ex) {
			Logger.getLogger(MainEditPage.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			dis.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return password;
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
	
	private boolean askIfTimeTableAlreadyPresent() {
		File file=new File("deps");
		if(file.length()==0)return true;
		int o=JOptionPane.showConfirmDialog(null, "Old table is already present.\n"
				+ "If you continue, it will be deleted.\n"
				+ "Do you still want to continue?\n"
				+ "(You can save old files with you before you continue..)", "Delete All?", JOptionPane.YES_NO_OPTION);
		if(o==JOptionPane.YES_OPTION){
			
			try {
				dis=new DataInputStream(new FileInputStream("deps"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			String a=null;
			e=0;
			try {
				a=decrypt();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				dis.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			String b="";
			for(int i=0;i<a.length();i++){
				if(a.charAt(i)=='\n'){
					if(new File(b).exists()==false)continue;
					File files[]=new File(b).listFiles();
					for(int j=0;j<files.length;j++){
						files[j].delete();
					}
					new File(b).delete();
					b="";
				}
				else b=b+a.charAt(i);
			}
			
			file.delete();
			new File("depPassword").delete();
			new File("file").delete();
			new File("mtt").delete();
			
			try {
				new File("depPassword").createNewFile();
				new File("deps").createNewFile();
				new File("file").createNewFile();
				new File("mtt").createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return true;
		}
		else return false;
	}
	
	private boolean getNoOfRooms(){
		
		JLabel label=new JLabel("Enter no. of Rooms :");
		JTextField text=new JTextField();
		text.setMaximumSize(new Dimension(200,30));
		text.setMinimumSize(new Dimension(200,30));
		text.setPreferredSize(new Dimension(200,30));
		text.requestFocus(true);
		
		JPanel p=new JPanel();
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		p.add(label);
		label.setAlignmentX(label.CENTER_ALIGNMENT);
		p.add(text);
		
		Object[] enter=new Object[]{"Enter"};
		
		int o=JOptionPane.showOptionDialog(null, p, "Number of Departments", 0, 0, null, enter, enter[0]);
		
		if(o==0){
			String a=text.getText();
			if(!isPositiveInteger(a)){
				JOptionPane.showMessageDialog (null, "Entered value should be Integer.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
				boolean x=getNoOfRooms();
				return x;
			}
			else if(Integer.parseInt(a)==0){
				JOptionPane.showMessageDialog (null, "Entered value should be greater than ZERO.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
				boolean x=getNoOfRooms();
				return x;
			}
			totalRooms=Integer.parseInt(a);
			return true;
		}
		else return false;
	}
    
    private boolean getNoOfDepartments(){
		
		JLabel label=new JLabel("Enter no. of Departments :");
		JTextField text=new JTextField();
		text.setMaximumSize(new Dimension(200,30));
		text.setMinimumSize(new Dimension(200,30));
		text.setPreferredSize(new Dimension(200,30));
		text.requestFocus(true);
		
		JPanel p=new JPanel();
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		p.add(label);
		label.setAlignmentX(label.CENTER_ALIGNMENT);
		p.add(text);
		
		Object[] enter=new Object[]{"Enter"};
		
		int o=JOptionPane.showOptionDialog(null, p, "Number of Departments", 0, 0, null, enter, enter[0]);
		
		if(o==0){
			String a=text.getText();
			if(!isPositiveInteger(a)){
				JOptionPane.showMessageDialog (null, "Entered value should be Integer.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
				boolean x=getNoOfDepartments();
				return x;
			}
			else if(Integer.parseInt(a)==0){
				JOptionPane.showMessageDialog (null, "Entered value should be greater than ZERO.\n", "Integer Error", JOptionPane.ERROR_MESSAGE);
				boolean x=getNoOfDepartments();
				return x;
			}
			totalDepartments=Integer.parseInt(a);
			return true;
		}
		else return false;
	}
	
	private boolean isPositiveInteger(String a){
		if(a.length()==0)return false;
		for(int i=0;i<a.length();i++)
			if(a.charAt(i)<48 || a.charAt(i)>57)
				return false;
		return true;
	}
	
	private void getRoomsNamesForm(final int n,String topic){
		final JFrame frame=new JFrame();
		frame.setSize(horizontal, vertical);
		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(3);
		
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		final JScrollPane scroll=new JScrollPane(panel);
		
		panel.add(Box.createRigidArea(new Dimension(50,50)));
		JLabel label=new JLabel(("Enter Names of "+topic).toUpperCase());
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(100,100)));
		label.setFont(new Font("Times New Roman",1,30));
		label.setAlignmentX(label.CENTER_ALIGNMENT);
		
		GetDepartments head=null,tail=null;
		for(int i=0;i<n;i++){
			GetDepartments ob=new GetDepartments();
			
			if(head==null){
				head=tail=ob;
			}
			else{
				ob.prev=tail;
				tail.next=ob;
				tail=ob;
			}
		}
		
		GetDepartments ob=head;
		for(int i=0;i<n;i++){
			JLabel l=new JLabel(Integer.toString(i+1)+"    ");//4 spaces
			
			JPanel p=new JPanel();
			p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
			//p.add(Box.createRigidArea(new Dimension(100,10)));
			
			p.add(l);
			ob.text.addKeyListener(new KeyListener(){
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
			p.add(ob.text);
			
			panel.add(p);
			panel.add(Box.createRigidArea(new Dimension(20,20)));
			
			ob=ob.next;
		}
		
		JButton enter=new JButton("Enter");
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
		panel.add(enter);
		panel.add(Box.createRigidArea(new Dimension(50,50)));
		
		frame.add(scroll);
		//frame.add(panel);
		
		final GetDepartments header=head;
		///*
		enter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				GetDepartments ob=header;
				String a[]=new String[n];
				for(int i=0;i<n;i++){
					a[i]=ob.text.getText();
					StringTokenizer st=new StringTokenizer(a[i]);
					if(st.countTokens()==0){
						JOptionPane.showMessageDialog (null, "Enter the name of Room "+Integer.toString(i+1)+"\n", "Error", JOptionPane.ERROR_MESSAGE);
						ob.text.requestFocus();
						return;
					}
					ob=ob.next;
				}
				ob=header;
				for(int i=0;i<n;i++){
					for(int j=0;j<i;j++){
						if(a[j].equalsIgnoreCase(a[i])){
							JOptionPane.showMessageDialog (null, "Room name "+Integer.toString(j+1)+"\n"
									+ "and room name "+Integer.toString(i+1)+"\n"
											+ "are same.\n"
											+ "There must be no same names for 2 room.", "Error", JOptionPane.ERROR_MESSAGE);
							ob.text.requestFocus();
							return;
						}
					}
					ob.value=ob.text.getText();
					ob=ob.next;
				}
				ob=header;
				for(int i=0;i<n;i++){
					a[i]=ob.value;
					ob=ob.next;
				}
				rooms=a;
				boolean x=getNoOfDepartments();
				if(!x)return;
				getDepartmentNamesForm(totalDepartments,"Departments");
				frame.dispose();
			}
		});
		//*/
		
		frame.setVisible(true);
	}
	
	private void getDepartmentNamesForm(final int n,String topic){
		final JFrame frame=new JFrame();
		frame.setSize(horizontal, vertical);
		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(3);
		
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		final JScrollPane scroll=new JScrollPane(panel);
		
		panel.add(Box.createRigidArea(new Dimension(50,50)));
		JLabel label=new JLabel(("Enter Names of "+topic).toUpperCase());
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(100,100)));
		label.setFont(new Font("Times New Roman",1,30));
		label.setAlignmentX(label.CENTER_ALIGNMENT);
		
		GetDepartments head=null,tail=null;
		for(int i=0;i<n;i++){
			GetDepartments ob=new GetDepartments();
			
			if(head==null){
				head=tail=ob;
			}
			else{
				ob.prev=tail;
				tail.next=ob;
				tail=ob;
			}
		}
		
		GetDepartments ob=head;
		for(int i=0;i<n;i++){
			JLabel l=new JLabel("Department "+Integer.toString(i+1));
			JLabel l2=new JLabel("    "+"No. of classes :"+"    ");
			
			JPanel p=new JPanel();
			p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
			
			//p.add(Box.createRigidArea(new Dimension(100,10)));
			ob.text.addKeyListener(new KeyListener(){
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
			
			ob.cNoText.addKeyListener(new KeyListener(){
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
			
			p.add(l);
			p.add(ob.text);
			p.add(l2);
			p.add(ob.cNoText);
			
			panel.add(p);
			panel.add(Box.createRigidArea(new Dimension(20,20)));
			
			ob=ob.next;
		}
		
		JButton enter=new JButton("Enter");
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
		panel.add(enter);
		panel.add(Box.createRigidArea(new Dimension(50,50)));
		
		frame.add(scroll);
		//frame.add(panel);
		
		final GetDepartments header=head;
		///*
		enter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				GetDepartments ob=header;
				String a[]=new String[n];
				for(int i=0;i<n;i++){
					a[i]=ob.text.getText();
					StringTokenizer st=new StringTokenizer(a[i]);
					if(st.countTokens()==0){
						JOptionPane.showMessageDialog (null, "Enter the name of Department "+Integer.toString(i+1)+"\n", "Error", JOptionPane.ERROR_MESSAGE);
						ob.text.requestFocus();
						return;
					}
					ob=ob.next;
				}
				ob=header;
				for(int i=0;i<n;i++){
					for(int j=0;j<i;j++){
						if(a[j].equalsIgnoreCase(a[i])){
							JOptionPane.showMessageDialog (null, "Department name "+Integer.toString(j+1)+"\n"
									+ "and department name "+Integer.toString(i+1)+"\n"
											+ "are same.\n"
											+ "There must be no same names for 2 departments.", "Error", JOptionPane.ERROR_MESSAGE);
							ob.text.requestFocus();
							return;
						}
					}
					ob.value=ob.text.getText();
					ob=ob.next;
				}
				
				ob=header;
				for(int i=0;i<n;i++){
					a[i]=ob.value;
					ob=ob.next;
				}
				
				ob=header;
				for(int i=0;i<n;i++){
					if(!isPositiveInteger(ob.cNoText.getText())){
						JOptionPane.showMessageDialog (null, "No. of classes\n"
								+ "at department no. "+Integer.toString(i+1)
								+" is not a positive Integer.", "Error", JOptionPane.ERROR_MESSAGE);
						ob.cNoText.requestFocus();
						return;
					}
					ob=ob.next;
				}
				
				int noOfClasses[]=new int[totalDepartments];
				ob=header;
				for(int i=0;i<n;i++){
					noOfClasses[i]=Integer.parseInt(ob.cNoText.getText());
					ob=ob.next;
				}
				
				GetClasses obj=new GetClasses();
				boolean x=obj.execute(horizontal,vertical,rooms,a,noOfClasses);
				if(x)frame.dispose();
			}
		});
		//*/
		
		frame.setVisible(true);
	}
	/*
	public static void main(String args[]){
		GetDepartments ob=new GetDepartments();
	}
	*/
}