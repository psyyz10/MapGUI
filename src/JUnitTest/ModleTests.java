package JUnitTest;

import static org.junit.Assert.*;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import model.Model;
import model.Waypoint;

import org.junit.Test;

public class ModleTests {

	/* Test Model can construct */
	@Test
	public void testModelConstructs() {
		Model model = new Model();
		assertNotNull(model);
	}

	/* Test a model has bounds */
	@Test
	public void testModelHoldsBounds() {
		Model model = new Model();
		assertNotNull(model.getBounds());
	}

	/* Test model can construct with a file,
	 * which has a waypoint list, bounds and file name */
	@Test
	public void testModelConstructsWithFile() {
		Model model = new Model(new File("./samplefile.csv"));
		assertNotNull(model.getWaypoints());
		assertNotNull(model.getBounds());
		assertNotNull(model.getFileName());
	}

	/* Test model's get and set method is worked */
	@Test
	public void testModelGetAndSetMethods() {
		Model model = new Model();
		String fileName = "fileExample";
		ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
		ArrayList<ActionListener> actionListenerList = new ArrayList<ActionListener>();

		// Set the method a value
		model.setFileName(fileName);
		model.setWaypoints(waypoints);
		model.setActionListenerList(actionListenerList);

		// Compare with the set value with the value getting form corresponding get method
		assertEquals(fileName, model.getFileName());
		assertEquals(waypoints, model.getWaypoints());
		assertEquals(actionListenerList, model.getActionListenerList());
	}

	/* Test Waypoint can construct and a waypoint contains name, latitude, longitude, 
	 * elevation, description, symbol, showDescription, showSymbol*/
	@Test
	public void testWaypointConstructs(){
		Waypoint waypoint = new Waypoint("waypoint", 1.0, 1.0, 1.0);
		assertNotNull(waypoint);
		assertNotNull(waypoint.getName());
		assertNotNull(waypoint.getLatitude());
		assertNotNull(waypoint.getLongtitude());
		assertNotNull(waypoint.getElevation());
		assertNotNull(waypoint.getSymbol());
		assertNotNull(waypoint.getDescription());
		assertNotNull(waypoint.getName());
		assertNotNull(waypoint.isShowDescription());
		assertNotNull(waypoint.isShowSymbol());
	}
	
	/* Test a Waypoint object's get and set method */
	@Test
	public void testWaypointGetAndSetMethods(){
		Waypoint waypoint = new Waypoint("waypoint", 1.0, 1.0, 1.0);
		String waypointName = "waypoint";
		double latitude = 2.0;
		double longitude = 3.0;
		double elevation = 4.0;
		String symbol = "home";
		String description = "It is my home";

		// Set the method a value
		waypoint.setName(waypointName);
		waypoint.setLatitude(latitude);
		waypoint.setLongitude(longitude);
		waypoint.setElevation(elevation);
		waypoint.setDescription(description);
		waypoint.setSymbol(symbol);

		// Compare with the set value with the value getting form corresponding get method
		assertEquals(waypointName, waypoint.getName());
		assertEquals((int)latitude, (int)waypoint.getLatitude());
		assertEquals((int)longitude, (int)waypoint.getLongtitude());
		assertEquals((int)elevation, (int)waypoint.getElevation());
		assertEquals(symbol , waypoint.getSymbol());
		assertEquals(description , waypoint.getDescription());
	}
}
