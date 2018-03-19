package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;


class HomePage extends JFrame {
    
    private int horizontal,vertical;
    private JButton enter,about;
    private ImageIcon backImage;
    
    public HomePage(int h, int v) {
        horizontal=h;vertical=v;
        setSize(horizontal,vertical);
        
        setBackground();
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        initialize();
        setComponents();
        actionListeners();
        
        setWindowIcon();
        
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(3);
    }
    
    public void setWindowIcon(){
    	ImageIcon image=new ImageIcon("icon");
    	setIconImage(image.getImage());
    }
    
    private void setBackground() {
    	
        backImage=new ImageIcon("TIMETABLE_hp_IMAGE");
        backImage=setBackImageSize(backImage);
        JLabel background=new JLabel(backImage);
        setContentPane(background);
		
	}
    
    private ImageIcon setBackImageSize(ImageIcon icon){
        
        BufferedImage bi = new BufferedImage(horizontal, vertical, BufferedImage.OPAQUE);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(icon.getImage(), 0, 0, horizontal, vertical, null);
        
        ImageIcon finalIcon=new ImageIcon(bi);
        return finalIcon;
    }
    
	private void initialize(){
        
        enter=new JButton(new ImageIcon("enterImage"));
        enter.setContentAreaFilled(false);
        enter.setBorderPainted(false);
        
        about=new JButton("About");
        about.setPreferredSize(new Dimension(100,30));
        about.setBackground(new Color(255,255,255));
    }
    
    private ImageIcon setImageSize(ImageIcon icon){
        int height = vertical*2/3;
        double t=(double)(height)/(double)(icon.getIconHeight());
        int width=(int)(t*icon.getIconWidth());
        
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.OPAQUE);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(icon.getImage(), 0, 0, width, height, null);
        
        ImageIcon finalIcon=new ImageIcon(bi);
        return finalIcon;
    }
    
    private void setComponents(){
        
        add(Box.createRigidArea(new Dimension(10,vertical*8/10)));
        add(enter);
        enter.setAlignmentX(enter.CENTER_ALIGNMENT);
        
        JPanel p=new JPanel();
        p.setOpaque(false);
        p.setLayout(new FlowLayout());
        p.add(Box.createRigidArea(new Dimension(1000,10)));
        p.add(about);
        about.setAlignmentY(about.TOP_ALIGNMENT);
        add(p);
        p.setAlignmentY(p.TOP_ALIGNMENT);
    }
    
    private void actionListeners(){
        enter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new MainOptions(horizontal,vertical);
                dispose();
            }
        });
        
        about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog (
						getContentPane()
						, 
						"This program is built for Ramanujan College\n"
						+ "to store and manage its timetable.\n"
						+ "\n"
						+ "Algorithm Acknowledgements:\n"
						+ "\n"
						+ "Nivedita Rai,\n"
						+ "Department of Computer Science,\n"
						+ "Ramanujan College (University of Delhi)\n"
						+ "\n"
						+ "Bittu Chauhan,\n"
						+ "Department of Computer Science,\n"
						+ "Ramanujan College (University of Delhi)\n"
						+ "\n"
						,
						
						"About Time Table P. (Ramanujan College)", JOptionPane.OK_OPTION);
			}
        });
    }
}
