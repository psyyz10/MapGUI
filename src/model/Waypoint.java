package model;

/** a class as a format to store one way point */
public class Waypoint {
	private String name;
	private double latitude;
	private double longitude;
	private double elevation;
	private String symbol = "default";
	private String description = "No description";
	private boolean showDescription = false;
	private boolean showSymbol = true;
	
	// Construct the class
	public Waypoint(){		
	}
	
	// Construct the class
	public Waypoint(String name, double latitude, double longitude,
			double elevation){
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
	}
	
	public String getName(){
		return name;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongtitude(){
		return longitude;
	}
	
	public double getElevation(){
		return elevation;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean isShowDescription(){
		return showDescription;
	}
	
	public boolean isShowSymbol(){
		return showSymbol;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public void setElevation(double elevation){
		this.elevation = elevation;
	}
	
	public void setSymbol(String symbol){
		this.symbol = symbol;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setShowDescription(boolean showDescription){
		this.showDescription = showDescription;
	}
	
	public void setShowSymbol(boolean showSymbol){
		this.showSymbol = showSymbol;
	}
	
	public String convertToString(){
		return "\r\n" + name + "," + latitude + "," + 
				longitude + "," + elevation + "," + symbol + "," + description;
	}
	
	public void convertToWaypoint(String waypointString){
		String[] tokens = waypointString.split(",", 0);
		name = tokens[0];
		latitude = Double.parseDouble(tokens[1]);
		longitude = Double.parseDouble(tokens[2]);
		elevation = Double.parseDouble(tokens[3]);
		symbol = tokens[4];
		description = tokens[5];
	}
}
