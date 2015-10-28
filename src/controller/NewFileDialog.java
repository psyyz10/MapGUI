package controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;


/** When create a new file it appears */
public class NewFileDialog extends JDialog{
	private String name;
	private double minLat;
	private double minLon;
	private double maxLat;
	private double maxLon;
	private boolean done = false;
	
	JLabel jlbFileName = new JLabel("File Name: ");
	JLabel jlbBound = new JLabel("Enter Display Bounds:");
	JLabel jlb = new JLabel("");
	JLabel jlbMinLat = new JLabel("Minimum Latitude: ");
	JLabel jlbMinLon = new JLabel("Minimum Longtitude: ");
	JLabel jlbMaxLat = new JLabel("Max Latitude: ");
	JLabel jlbMaxLon = new JLabel("Max Longtitude: ");
	JTextField jtfName = new JTextField();
	JTextField jtfMinLat = new JTextField();
	JTextField jtfMinLon = new JTextField();
	JTextField jtfMaxLat = new JTextField();
	JTextField jtfMaxLon = new JTextField();
	
	// Create two buttons
	private JButton jbtOK = new JButton("OK");
	private JButton jbtCancel = new JButton("Cancel");
	
	public NewFileDialog(){
		this(null, true);
	}
	
	public NewFileDialog (java.awt.Frame parent, boolean modal){
		super(parent, modal);
		setTitle("New File");
		
		jlbBound.setFont(new Font("Arial",Font.ITALIC, 13));
		
		// Group two buttons OK and Cancel
	    JPanel jpButtons = new JPanel();
	    jpButtons.add(jbtOK);
	    jpButtons.add(jbtCancel);
	    
	    JPanel jpInput = new JPanel(new GridLayout(6,2,5,5));
	    TitledBorder titledBorder = BorderFactory.createTitledBorder(
				null, "Create a new GPS file", TitledBorder.DEFAULT_JUSTIFICATION, 
				TitledBorder.DEFAULT_POSITION, new Font("Verdana",Font.PLAIN, 12), Color.BLUE);
	    jpInput.setBorder(titledBorder);
	    jpInput.add(jlbFileName);
	    jpInput.add(jtfName);
	    jpInput.add(jlbBound);
	    jpInput.add(jlb);
	    jpInput.add(jlbMinLat);
	    jpInput.add(jtfMinLat);
	    jpInput.add(jlbMinLon);
	    jpInput.add(jtfMinLon);
	    jpInput.add(jlbMaxLat);
	    jpInput.add(jtfMaxLat);
	    jpInput.add(jlbMaxLon);
	    jpInput.add(jtfMaxLon);		
	    
	    add(jpButtons, BorderLayout.SOUTH);
	    add(jpInput, BorderLayout.CENTER);
	    pack();
	    
	    // Add listener to OK button
	    jbtOK.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		try{
	    			done = true;
	    			name = jtfName.getText();
	    			minLat = Double.parseDouble(jtfMinLat.getText());
	    			minLon = Double.parseDouble(jtfMinLon.getText());
	    			maxLat = Double.parseDouble(jtfMaxLat.getText());
	    			maxLon = Double.parseDouble(jtfMaxLon.getText());
	    			setVisible(false);
	    			
	    			if (minLat > maxLat){
	    				JOptionPane.showMessageDialog(null, "maxLat should be greater than minLat");
	    				done = false;
	    				setVisible(true);
	    			}
	    			else if (minLon > maxLon){
	    				JOptionPane.showMessageDialog(null, "maxLon should be greater than minLon");
	    				done = false;
	    				setVisible(true);
	    			}
	    		}catch(Exception ex){
	    			JOptionPane.showMessageDialog(null, "Enter a string for file name" +
	    					"and numbers for " + "latitude, lontitude and elevation");
	    		}
	    	}
	    });
	    
	 // Add listener to Cancel button
	    jbtCancel.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		setVisible(false);
	    	}
	    });
	}
	
	public double[] getDisplayBounds(){
		double[] bounds = {minLat, minLon, maxLat, maxLon};
		return bounds;
	}
	
	public String getFileName(){
		return name;
	}
	
	public boolean isDone(){
		return done;
	}
	
	public Dimension getPreferredSize() {
	    return new java.awt.Dimension(300, 300);
	}
}
