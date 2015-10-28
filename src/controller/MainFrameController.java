package controller;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.*;

import com.javadocking.DockingManager;
import com.javadocking.component.DefaultSwComponentFactory;
import com.javadocking.dock.*;
import com.javadocking.dockable.*;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.dockable.action.DefaultPopupMenuFactory;
import com.javadocking.model.FloatDockModel;
import com.javadocking.visualizer.FloatExternalizer;
import com.javadocking.visualizer.LineMinimizer;
import com.javadocking.visualizer.SingleMaximizer;

import view.DisplayDataTablePanel;
import view.MapTabPane;


import model.Model;

/** Note:
 *  
 *  The project use javadocking.jar to implement dock and undock functions
 *  Please add this archive to the project, it is in the lib folder of 
 *  the project, thanks very much.
 *
 */

public class MainFrameController extends JFrame{
	Model model = new Model();
	private MapTabPane mapTabPane = new MapTabPane();
	private JSplitPane jSplitPane2;
	private JSplitPane jSplitPane1;
	private JToolBar jtb = new JToolBar();
	private DisplayDataTablePanel tablePanel = new DisplayDataTablePanel();
	private DataEntryPanel dataEntryPanel = new DataEntryPanel();
	private MenusBar jmb;
	private Dockable[] dockables;

	public MainFrameController(){
		// Create the dock model for the docks.
		FloatDockModel dockModel = new FloatDockModel();
		dockModel.addOwner("frame0", this);

		// Give the dock model to the docking manager.
		DockingManager.setDockModel(dockModel);

		// Create two dockables
		Dockable dockable1 = new DefaultDockable("GPS data", tablePanel, "GPS data", new ImageIcon("image/gps.png"));
		Dockable dockable2 = new DefaultDockable("GPS data entry panel", dataEntryPanel, "GPS data entry panel", new ImageIcon("image/entry.png"));
		dockable1 = new StateActionDockable(dockable1, new DefaultDockableStateActionFactory(), DockableState.statesClosed());
		dockable2 = new StateActionDockable(dockable2, new DefaultDockableStateActionFactory(), DockableState.statesClosed());

		dockables = new Dockable[2];
		dockables[0] = dockable1;
		dockables[1] = dockable2;

		// Create two dockable tabs
		TabDock upTabDock = new TabDock();
		upTabDock.addDockable(dockable1, new Position(0));
		TabDock downTabDock = new TabDock();
		downTabDock.addDockable(dockable2, new Position(0));

		// Add the root dock to the dock model.
		dockModel.addRootDock("upTabDock", upTabDock, this);
		dockModel.addRootDock("downTabDock", downTabDock, this);

		jmb = new MenusBar(dockables);
		setJMenuBar(jmb);

		//Use scroll pane to split the three panel
		jSplitPane2 = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, upTabDock,
				downTabDock);
		jSplitPane1 = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, jSplitPane2 , 
				mapTabPane);
		
		//Link to the model
		dataEntryPanel.setModel(model);
		tablePanel.setModel(model);
		
		jSplitPane2.setOneTouchExpandable(true);

		add(jSplitPane1, BorderLayout.CENTER);
		add(jtb, BorderLayout.NORTH);

		//make them interact to the model
		jmb.setModel(model);
		mapTabPane.setModel(model);

		//make them link to the menus
		jmb.setViewPanel(upTabDock, downTabDock, mapTabPane);
		jmb.setToolBar(jtb); 
		jmb.setSplitPane(jSplitPane1, jSplitPane2); 
		mapTabPane.setMenus(jmb);
	}
	
	// create and shows GUI
	public static void createAndShowGUI(){
		MainFrameController frame = new MainFrameController();
		frame.setDefaultCloseOperation(3);
		frame.setTitle("GPS Map");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); 
	}

	public static void main(String[] args) {

		//Set the software UI look and feel and fonts
		try {  
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
			UIManager.put( "Button.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "Label.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "MenuItem.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "TabbedPane.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "Table.font", new Font("Verdana",Font.PLAIN, 11) );
		}  
		catch (Exception e) {  
			e.printStackTrace();  
		}  

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}
}
