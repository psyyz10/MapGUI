package model;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

/** Use Model to realize MVC design */
public class Model {
	private String fileName;
	private double minLat;
	private double minLon;
	private double maxLat;
	private double maxLon;
	private ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	private ArrayList<ActionListener> actionListenerList = new ArrayList<ActionListener>();
	
	public Model(){}

	public Model(File file){
		try {			
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(file));
			byte[] b = new byte[in.available()];	
			in.read(b, 0, b.length);			
			String content = new String(b, 0, b.length);

			//Convert bounds form String to array format
			String[] contentTokens = content.split("\r\n", 2);
			String[] boundsString = contentTokens[0].split(",", 0);
			double[] bounds = new double[4];
			for(int i = 0; i < 4; i++){
				bounds[i] = Double.parseDouble(boundsString[i]);
			}
			setBounds(bounds);
			setFileName(file.getAbsolutePath());

			//Convert waypoints from string to wapoints and add to model
			if (contentTokens.length != 1)
				convertStringToWaypoints(contentTokens[1]);
			in.close();
			setFileName(file.getAbsolutePath());
		}catch (IOException ex) {

			//If file not be found, show error message
			JOptionPane.showMessageDialog(null,"Not found",
					"Error opening ", JOptionPane.INFORMATION_MESSAGE);
		}catch (Exception ex) {

			//If the file formation is not correct, show error message
			JOptionPane.showMessageDialog(null,"This is not a GPS file",
					"Error opening ", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public double[] getBounds(){
		double[] bounds = {minLat, minLon, maxLat, maxLon};
		return bounds;
	}

	public ArrayList<Waypoint> getWaypoints(){
		return waypoints;
	}

	public String getFileName(){
		return fileName;
	}

	public ArrayList<ActionListener> getActionListenerList(){
		return actionListenerList;
	}

	public void setBounds(double[] bounds){
		minLat = bounds[0];
		minLon = bounds[1];
		maxLat = bounds[2];
		maxLon = bounds[3];

		// Notify the listener for the change on bounds
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bounds"));
	}

	public void setFileName(String fileName){
		this.fileName = fileName;

		// Notify the listener for the change on bounds
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "fileName"));
	}

	public void setWaypoints(ArrayList<Waypoint> waypoints){
		if (this.waypoints != null)
			this.waypoints.clear();
		else{
			this.waypoints = new ArrayList<Waypoint> ();
		}

		if (waypoints != null){
			for (int i = 0; i < waypoints.size(); i++)
				this.waypoints.add(waypoints.get(i));
		}

		// Notify the listener for the change on bounds
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "waypoints"));
	}	

	public void setActionListenerList
	(ArrayList<ActionListener> actionListenerList){
		this.actionListenerList = actionListenerList;

		// Notify the listener for the change on bounds
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "actionList"));
	}

	public void addAWaypoint(Waypoint waypoint){
		if (waypoints == null)
			waypoints = new ArrayList<Waypoint> ();

		if(!isValidate(waypoint)){
			JOptionPane.showMessageDialog(null, "The waypoint is out of bounds");
			return;
		}
		waypoints.add(waypoint);

		// Notify the listener for the change on waypoints
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "waypoints"));
	}

	private boolean isValidate (Waypoint waypoint){
		double latitude = waypoint.getLatitude(); 
		double longitude = waypoint.getLongtitude(); 

		if (latitude < maxLat && latitude > minLat && longitude < maxLon && longitude > minLon)
			return true;

		return false;	
	}

	public void deleteAWaypoint(int index){
		if (waypoints == null) return;
		waypoints.remove(index);

		// Notify the listener for the change on waypoints
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "deleteAWaypoint"));
	}

	public void addDescription(int index, String description){
		waypoints.get(index).setDescription(description);

		// Notify the listener for the change on waypoints
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "addDescription"));
	}

	public void addASymbol(int index, String symbol){
		waypoints.get(index).setSymbol(symbol);

		// Notify the listener for the change on waypoints
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "addASymbol"));
	}
	
	public void setShowSymbol(int index, boolean b){
		waypoints.get(index).setShowSymbol(b);

		// Notify the listener for the change on waypoints
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "setShowSymbol"));
	}
	
	public void setShowDescription(int index, boolean b){
		waypoints.get(index).setShowDescription(b);

		// Notify the listener for the change on waypoints
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "setShowDescription"));
	}
	
	/** Convert way points from String to Wayponts */
	public void convertStringToWaypoints(String waypointsString){
		if (waypoints == null)
			waypoints = new ArrayList<Waypoint>();

		String[] tokens = waypointsString.split("\r\n", 0);
		for (int i = 0; i < tokens.length; i++){
			waypoints.add(new Waypoint());
			waypoints.get(i).convertToWaypoint(tokens[i]);
		}
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "convertStringToWaypoints"));
	}

	/** Reset the model to empty but not null*/
	public void resetModel(){
		fileName = null;
		minLat = 0;
		minLon = 0;
		maxLat = 0;
		maxLon = 0;
		waypoints = null;

		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "reset"));
	}

	/** Convert way point from Waypoints to String */
	@SuppressWarnings("null")
	public String convertWaypointsToString(){
		StringBuilder output = new StringBuilder();
		if (waypoints != null){
			for(int i = 0; i < waypoints.size(); i++)
				output.append(waypoints.get(i).convertToString());
			return output.toString();
		}
		else{
			return null;
		}
	}


	/** Add an action event listener */
	public synchronized void addActionListener(ActionListener l) {
		if (actionListenerList == null)
			actionListenerList = new ArrayList<ActionListener>();

		actionListenerList.add(l);
	}

	/** Remove an action event listener */
	public synchronized void removeActionListener(ActionListener l) {
		if (actionListenerList != null && actionListenerList.contains(l))
			actionListenerList.remove(l);
	}

	/** Fire TickEvent */
	private void processEvent(ActionEvent e) {
		ArrayList list;

		synchronized (this) {
			if (actionListenerList == null) return;
			list = (ArrayList)actionListenerList.clone();
		}

		for (int i = 0; i < list.size(); i++) {
			ActionListener listener = (ActionListener)list.get(i);
			listener.actionPerformed(e);

		}
	}
}
