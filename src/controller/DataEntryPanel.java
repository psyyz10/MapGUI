package controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;

import model.Model;
import model.Waypoint;

/** Panel for entrying the waypoints data */
public class DataEntryPanel extends JPanel implements ActionListener, DraggableContent{
	private Model model;
	private int pointNumber;
	private JLabel jlbName = new JLabel("Name");
	private JLabel jlbLatitude = new  JLabel("Latitude");
	private JLabel jlbLongtitude = new JLabel("Longtitude");
	private JLabel jlbElevation = new JLabel("Elevation");
	private JLabel jlbPoint1 = new JLabel("Point");
	private JLabel jlbLatitude1 = new JLabel("Latitude");
	private JLabel jlbLongtitude1 = new JLabel("Longtitude");
	private JLabel jlbElevation1 = new JLabel("Elevation");
	private JLabel jlbPointData = new JLabel("Null");	
	private JLabel jlbLatitudeData = new JLabel("0.0");
	private JLabel jlbLongtitudeData = new JLabel("0.0");
	private JLabel jlbElevationData = new JLabel("0.0");
	private JTextField jtfdName = new JTextField();
	private JTextField jtfdLatitude = new JTextField();
	private JTextField jtfdLongtitude = new JTextField();
	private JTextField jtfdElevation = new JTextField();
	private JButton jbtPrevious = new JButton("Previous");
	private JButton jbtNext = new JButton("  Next    ");
	private JButton jbtAdd = new JButton("  Add    ");
	private JButton jbtDelete = new JButton(" Delete");
	Font LargeFont = new Font("Arial",Font.BOLD, 12);
	
	/** Construct the class */
	DataEntryPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set the Font, mnnemonic, tool tip text and foreground
		jbtPrevious.setMnemonic('P');
		jbtNext.setMnemonic('N');
		jbtAdd.setMnemonic('A');
		jbtDelete.setMnemonic('D');
		jbtPrevious.setToolTipText("Display previous data");
		jbtNext.setToolTipText("Display next data");
		jbtAdd.setToolTipText("Add new data to the data list");
		jlbPoint1.setFont(LargeFont);
		jlbLatitude1.setFont(LargeFont);
		jlbLongtitude1.setFont(LargeFont);
		jlbElevation1.setFont(LargeFont);
		jlbPointData.setFont(LargeFont);
		jlbLatitudeData.setFont(LargeFont);
		jlbLongtitudeData.setFont(LargeFont);
		jlbElevationData.setFont(LargeFont);
		jlbPointData.setForeground(Color.RED);
		jlbLatitudeData.setForeground(Color.RED);
		jlbLongtitudeData.setForeground(Color.RED);
		jlbElevationData.setForeground(Color.RED);
		
		//construct panel 1
		JPanel p1 = new JPanel(new GridLayout(5, 2, 5, 5));
		p1.add(jlbName);
		p1.add(jtfdName);
		p1.add(jlbLatitude);
		p1.add(jtfdLatitude);
		p1.add(jlbLongtitude);
		p1.add(jtfdLongtitude);
		p1.add(jlbElevation);
		p1.add(jtfdElevation);
		
		//construct panel 1
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		p2.add(jbtDelete);
		p2.add(jbtAdd);
		
		//use box layout 
		Box box = Box.createVerticalBox();
		box.add(p1);
		box.add(p2);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(
				null, "Add a new point", TitledBorder.DEFAULT_JUSTIFICATION, 
				TitledBorder.DEFAULT_POSITION, new Font("Verdana",Font.PLAIN, 12), Color.BLUE);
		box.setBorder(titledBorder);
		
		//construct panel 3 
		JPanel p3 = new JPanel(new GridLayout(2,4,10,10));
		p3.setBackground(Color.WHITE);
		p3.setBorder(new LineBorder(Color.ORANGE, 2));
		p3.add(jlbPoint1);
		p3.add(jlbLatitude1);
		p3.add(jlbLongtitude1);
		p3.add(jlbElevation1);
		p3.add(jlbPointData);
		p3.add(jlbLatitudeData);
  	    p3.add(jlbLongtitudeData);
		p3.add(jlbElevationData);
		
		//construct panel 5
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		p5.add(jbtPrevious);
		p5.add(jbtNext);		
		
		add(box);
		this.add(Box.createVerticalStrut(15));
		add(p3);
		this.add(Box.createVerticalStrut(10));
		add(p5);
		setPreferredSize(new Dimension(280, 300));
		setMinimumSize(new Dimension(150, 170));
		
		/** add listener to previous button */
		jbtPrevious.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//If model is empty, show message dialogue
				if (model.getFileName() == null) {
					JOptionPane.showMessageDialog(null, "Open a file or " +
							"create a new file first");
					return;
				}
				
				//If waypoints in model is empty, show message dialogue
				if (model.getWaypoints().size() == 0) {
					JOptionPane.showMessageDialog(null, "There is no waypoint data");
					return;
				}
				Waypoint waypoint = (Waypoint) model.getWaypoints().get
						(pointNumber <= 0? pointNumber = model.getWaypoints().size() - 1 : --pointNumber);
				
				//make display panel display previous points information
				jlbPointData.setText(waypoint.getName() + "");
				jlbLatitudeData.setText(waypoint.getLatitude() + "");
				jlbLongtitudeData.setText(waypoint.getLongtitude() + "");
				jlbElevationData.setText(waypoint.getElevation() + "");
			}
		});
		
		/** add listener to next button */
		jbtNext.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//If model is empty, show message dialogue
				if (model.getFileName() == null) {
					JOptionPane.showMessageDialog(null, "Open a file or " +
							"create a new file first");
					return;
				}
				
				//If waypoints in model is empty, show message dialogue
				if (model.getWaypoints().size() == 0) {
					JOptionPane.showMessageDialog(null, "There is no waypoint data");
					return;
				}
				Waypoint waypoint = (Waypoint) model.getWaypoints().get
						(pointNumber >= model.getWaypoints().size() - 1 ? pointNumber = 0 : ++ pointNumber);;
						
				//make display panel display next points information
				jlbPointData.setText(waypoint.getName() + "");
				jlbLatitudeData.setText(waypoint.getLatitude() + "");
				jlbLongtitudeData.setText(waypoint.getLongtitude() + "");
				jlbElevationData.setText(waypoint.getElevation() + "");
			}
		});
		
		/** add listener to add button */
		jbtAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				//If model is empty, show message dialogue
				if (model.getFileName() == null) {
					JOptionPane.showMessageDialog(null, "Open a file or " +
							"create a new file first");
					return;
				}
				
				String name = jtfdName.getText().trim();
				
				if (name.equals("")){
					
					//If the name text field is empty, show message dialogue
					JOptionPane.showMessageDialog(null, "Name cannot be empty");
					return;
				}
				
				try {
					double latitude = Double.parseDouble(jtfdLatitude.getText().trim());
					double longitude = Double.parseDouble(jtfdLongtitude.getText().trim());
					double elevation = Double.parseDouble(jtfdElevation.getText().trim());
					Waypoint waypoint = new Waypoint(name, latitude, longitude, elevation);
					model.addAWaypoint(waypoint);
				}catch (IllegalArgumentException ex){
					
					//If the latitude, latitude or elevation are not numbers, show message dialogue
					JOptionPane.showMessageDialog(null, "Enter numbers for latitude, latitude or elevation");
					return;
				}
				
				//make display panel display next points information
				jtfdName.setText("");
				jtfdLatitude.setText("");
				jtfdLongtitude.setText("");
				jtfdElevation.setText("");
			}
		});	
		
		/** add listener to delete button */
		jbtDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				//If model is empty, show message dialogue
				if (model.getFileName() == null) {
					JOptionPane.showMessageDialog(null, "Open a file or " +
							"create a new file first");
					return;
				}
				//If waypoints is empty, show message dialogue
				if (model.getWaypoints().size() == 0) {
					JOptionPane.showMessageDialog(null, "There is no waypoint");
					return;
				}
				model.deleteAWaypoint(pointNumber);			
				
				if (pointNumber == 0){
					pointNumber = 0;
					jlbPointData.setText("Null");
					jlbLatitudeData.setText("0.0");
					jlbLongtitudeData.setText("0.0");
					jlbElevationData.setText("0.0");
					return;
				}
				
				Waypoint waypoint = (Waypoint) model.getWaypoints().get
						(pointNumber <= 0? pointNumber = model.getWaypoints().size() - 1 : --pointNumber);
				
				//make display panel display next points information
				jlbPointData.setText(pointNumber + "");
				jlbLatitudeData.setText(waypoint.getLatitude() + "");
				jlbLongtitudeData.setText(waypoint.getLongtitude() + "");
				jlbElevationData.setText(waypoint.getElevation() + "");
			}
		});	
	}
	
	/** the display panel as a viewer, when model changes, displayed data changes */
	public void actionPerformed(ActionEvent actionEvent){
		if (model == null || model.getWaypoints() == null||
				model.getWaypoints().size() == 0) return;

		pointNumber = model.getWaypoints().size() - 1;
		Waypoint waypoint = model.getWaypoints().get(pointNumber);
		jlbPointData.setText(waypoint.getName() + "");
		jlbLatitudeData.setText(waypoint.getLatitude() + "");
		jlbLongtitudeData.setText(waypoint.getLongtitude() + "");
		jlbElevationData.setText(waypoint.getElevation() + "");
	}
	
	public void addDragListener(DragListener dragListener)
	{
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
	}
	
	public Model getModel(){
		  return model;
	}
	
	public void setModel(Model model){
		this.model = model;
		
		if(model != null)
			model.addActionListener(this);
	}
	
}
