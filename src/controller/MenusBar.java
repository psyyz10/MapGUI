package controller;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.javadocking.dock.TabDock;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.action.DefaultDockableStateAction;
import com.javadocking.event.DockingEvent;
import com.javadocking.event.DockingListener;

import view.DisplayDataTablePanel;
import view.MapTabPane;

import model.Model;


/** As menus, a big controller */
public class MenusBar extends JMenuBar{
	private Model model;
	private JMenuItem jmiNew, jmiOpen, jmiSave, jmiSaveAs, jmiExit, jmiHelp;
	private JCheckBoxMenuItem jmiShowMap, jmiShowToolBar, jmiShowTable, jmiShowEntry;
	private ImageIcon newImageIcon =
			new ImageIcon("image/new.png");
	private ImageIcon openImageIcon =
			new ImageIcon("image/open.png");
	private ImageIcon saveImageIcon =
			new ImageIcon("image/save.png");
	private ImageIcon saveAsImageIcon =
			new ImageIcon("image/saveas.png");
	private ImageIcon exitImageIcon =
			new ImageIcon("image/exit.png");
	private ImageIcon helpImageIcon =
			new ImageIcon("image/help.png");
	private JButton jbtNew = new JButton(newImageIcon);
	private JButton jbtOpen = new JButton(openImageIcon);
	private JButton jbtSave = new JButton (saveImageIcon);
	private JButton jbtSaveAs = new JButton(saveAsImageIcon);
	private JButton jbtHelp = new JButton(helpImageIcon);
	private JButton jbtExit = new JButton (exitImageIcon);
	private JToolBar jtb = new JToolBar();
	private JFileChooser jFileChooser1 
	= new JFileChooser(new File("."));
	private TabDock dataEntryPanel;
	private MapTabPane mapTabPane;
	private JSplitPane jSplitPane1;
	private JSplitPane jSplitPane2;
	private Dockable[] dockables;
	private TabDock tablePanel;

	/** Construct MenusBar */
	public MenusBar(Dockable[] dockables){
		super();
		this.dockables = dockables;

		//Use a file filter to filt ".csv" file
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"GPS data files csv", "csv");
		jFileChooser1.setFileFilter(filter);

		// Add menu "File" to menu bar
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		add(fileMenu);

		// Add menu "View" to menu bar
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		add(viewMenu);

		JMenu helpMenu = new JMenu("Help");
		add(helpMenu);

		//Add file menu item
		fileMenu.add(jmiNew = new JMenuItem("New",'N'));
		fileMenu.add(jmiOpen = new JMenuItem("Open", 'O'));
		fileMenu.add(jmiSave = new JMenuItem("Save", 'S'));
		fileMenu.add(jmiSaveAs = new JMenuItem("Save as"));
		fileMenu.addSeparator();
		fileMenu.add(jmiExit = new JMenuItem("Exit", 'E'));


		helpMenu.add(jmiHelp = new JMenuItem("Help - use cases",'H'));
		
		viewMenu.add(jmiShowTable = new DockableMenuItem(dockables[0]));	
		viewMenu.add(jmiShowEntry = new DockableMenuItem(dockables[1]));
		viewMenu.add(jmiShowMap = new JCheckBoxMenuItem("Show Map panel", new ImageIcon("image/earth.png")));
		viewMenu.add(jmiShowToolBar = new JCheckBoxMenuItem("Show Tool Bar", true));
		jmiShowMap.setState(true);

		//Set menu icon
		jmiNew.setIcon(newImageIcon);
		jmiOpen.setIcon(openImageIcon);
		jmiSave.setIcon(saveImageIcon);
		jmiSaveAs.setIcon(saveAsImageIcon);
		jmiExit.setIcon(exitImageIcon);
		jmiHelp.setIcon(helpImageIcon);

		//Set shortcut key
		jmiNew.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		jmiOpen.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		jmiSave.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));


		//Listener for functions on both menus and tool bar
		NewListener newListener = new NewListener();
		OpenListener openListener = new OpenListener();
		SaveListener saveListner = new SaveListener();
		SaveAsListener saveAsListner = new SaveAsListener();
		HelpListener helpListner = new HelpListener();
		ExitListener exitListner = new ExitListener();

		//Add listener
		jmiNew.addActionListener(newListener);	    
		jmiOpen.addActionListener(openListener);
		jmiSave.addActionListener(saveListner);
		jmiSaveAs.addActionListener(saveAsListner );
		jmiHelp.addActionListener(helpListner);
		jmiExit.addActionListener(exitListner);
		jbtNew.addActionListener(newListener);
		jbtOpen.addActionListener(openListener);
		jbtSave.addActionListener(saveListner);
		jbtSaveAs.addActionListener(saveAsListner);
		jbtHelp.addActionListener(helpListner);
		jbtExit.addActionListener(exitListner);

				/** If selected, make it visible */
				jmiShowTable.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						tablePanel.setVisible(jmiShowTable.isSelected());
						jSplitPane2.resetToPreferredSizes();
						jSplitPane1.resetToPreferredSizes();
					}
				});

		/** If selected, make it visible */
		jmiShowMap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mapTabPane.setVisible(jmiShowMap.isSelected());
				jSplitPane1.resetToPreferredSizes();
			}
		});

				/** If selected, make it visible */
				jmiShowEntry.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						dataEntryPanel.setVisible(jmiShowEntry.isSelected());
						jSplitPane2.resetToPreferredSizes();
						jSplitPane1.resetToPreferredSizes();
					}
				});

		/** If selected, make it visible */
		jmiShowToolBar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				jtb.setVisible(jmiShowToolBar.isSelected());
			}
		});
	}

	/** perform the new action when new button is clicked */
	class NewListener implements ActionListener{
		public void actionPerformed(ActionEvent e) { 	

			//Show a dialogue for use to create a new file and input file name and bounds
			NewFileDialog dialog = new NewFileDialog();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);

			if (dialog.isDone() == true){
				mapTabPane.setDisplayedMapPanelEmpty();
				model.resetModel();	
				model.setBounds(dialog.getDisplayBounds());
				model.setFileName(dialog.getFileName());
				mapTabPane.addMapPane(dialog.getFileName());
			}
		}
	}

	/** perform the open action when Open button is clicked */
	class OpenListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			open();
		}
	}

	/** perform the save action when Save button is clicked */
	class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (model.getFileName() == null){
				JOptionPane.showMessageDialog(null,"Open a file or create a new file first",
						"Error opening ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			save();
		}
	}	

	/** perform the save as action when save as button is clicked */
	class SaveAsListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (model.getFileName() == null){
				JOptionPane.showMessageDialog(null,"Open a file or create a new file first",
						"Error opening ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveAs();
		}
	}

	/** perform the help action when help button is clicked */
	class HelpListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JTextArea jta = new JTextArea();
			jta.setEditable(false);
			try{
				Scanner input = new Scanner(new File("src/HelpText.txt"));

				while (input.hasNextLine()){
					jta.append(input.nextLine() + "\n"); 
				}

				JScrollPane jsp = new JScrollPane(jta);
				jsp.setPreferredSize(new Dimension(700, 400));
				JOptionPane.showMessageDialog(null, jsp, "Help - use cases",
						JOptionPane.INFORMATION_MESSAGE,null);
			}catch(IOException ex){
				ex.printStackTrace();
			}	
		}
	}

	/** perform the exit action when exit button is clicked */
	class ExitListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			//Ask if save the change when click the exit menu item
			int n = JOptionPane.showConfirmDialog(null,"Save Changes?",
					"Save Resource", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,null);

			if (n == JOptionPane.YES_OPTION)
				save();
			else if (n == JOptionPane.NO_OPTION)
				System.exit(0);
		}
	}

	/** Open file */
	private void open() {
		if (jFileChooser1.showOpenDialog(this) ==
				JFileChooser.APPROVE_OPTION)
			open(jFileChooser1.getSelectedFile());
	}

	/** Open file with the specified File instance */
	private void open(File file) {
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

			mapTabPane.setDisplayedMapPanelEmpty();
			//Reset model and set bounds and file name to model
			model.resetModel();
			model.setBounds(bounds);
			model.setFileName(file.getAbsolutePath());

			//Convert waypoints from string to wapoints and add to model
			if (contentTokens.length != 1)
				model.convertStringToWaypoints(contentTokens[1]);
			in.close();

			mapTabPane.addMapPane(file.getName());
			model.setFileName(file.getAbsolutePath());
			mapTabPane.setTitleAt(mapTabPane.getSelectedIndex(), file.getName());
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

	/** Save file */
	public void save(){
		File file = new File(model.getFileName());
		if (file.exists() && mapTabPane.getSelectedIndex() != -1){
			save(file);
			model.setFileName(file.getAbsolutePath());
			mapTabPane.setTitleAt(mapTabPane.getSelectedIndex(), file.getName());
		}
		else{
			saveAs();
		}
	}

	/** Save as file */
	private void saveAs() {
		if (jFileChooser1.showSaveDialog(this) ==
				JFileChooser.APPROVE_OPTION) {

			/** Make the file end with ".csv"  */
			File file = jFileChooser1.getSelectedFile();
			String filePath = file.getAbsolutePath();
			if(!filePath.endsWith(".csv")) {
				file = new File(filePath + ".csv");
			}
			save(file);
			model.setFileName(file.getAbsolutePath());
			mapTabPane.setTitleAt(mapTabPane.getSelectedIndex(), file.getName());
		}
	}

	/** Save file with specified File instance */
	private void save(File file) {
		try {
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(file));
			//Write bounds
			double[] bounds = model.getBounds();
			out.write((bounds[0] + "," + bounds[1] + "," + bounds[2]
					+ "," + bounds[3]).getBytes());

			String wapointsString = null;
			if ((wapointsString = model.convertWaypointsToString()) == null){
				out.close();
				return;
			}

			//Write waypoints
			byte[] b = wapointsString.getBytes();
			out.write(b, 0, b.length);
			out.close();
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null,file.getName() + " cannot save",
					"Error Saving ", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public Model getModel(){
		return model;
	}

	public void setModel(Model model){
		this.model = model;
	}

	/** Set tree view panel for manipulating show view function */
	public void setViewPanel(TabDock tablePanel, TabDock dataEntryPanel, MapTabPane mapPanel){
		this.tablePanel = tablePanel;
		this.dataEntryPanel = dataEntryPanel;
		this.mapTabPane = mapPanel;
	}

	/** Set tree view panel for manipulating tool bar button function*/
	public void setToolBar(JToolBar jtb){
		this.jtb = jtb;
		jtb.add(jbtNew);
		jtb.add(jbtOpen);
		jtb.add(jbtSave);
		jtb.add(jbtSaveAs);
		jtb.add(jbtHelp);
		jtb.add(jbtExit);
	}

	/** Set tree view panel for manipulating show view function */
	public void setSplitPane(JSplitPane jSplitPane1, JSplitPane jSplitPane2){
		this.jSplitPane1 = jSplitPane1;
		this.jSplitPane2 = jSplitPane2;
	}

	/**
	 * A check box menu item to add or remove the dockable.
	 */
	private class DockableMenuItem extends JCheckBoxMenuItem
	{

		public DockableMenuItem(Dockable dockable)
		{
			super(dockable.getTitle(), dockable.getIcon());

			setSelected(dockable.getDock() != null);

			DockableMediator dockableMediator = new DockableMediator(dockable, this);
			dockable.addDockingListener(dockableMediator);
			addItemListener(dockableMediator);
		}

	}

	/**
	 * A listener that listens when menu items with dockables are selected and deselected.
	 * It also listens when dockables are closed or docked.
	 */
	private class DockableMediator implements ItemListener, DockingListener
	{

		private Dockable dockable;
		private Action closeAction;
		private Action restoreAction;
		private JMenuItem dockableMenuItem;

		public DockableMediator(Dockable dockable, JMenuItem dockableMenuItem) 
		{

			this.dockable = dockable;
			this.dockableMenuItem = dockableMenuItem;
			closeAction = new DefaultDockableStateAction(dockable, DockableState.CLOSED);
			restoreAction = new DefaultDockableStateAction(dockable, DockableState.NORMAL);
		}

		public void itemStateChanged(ItemEvent itemEvent)
		{

			dockable.removeDockingListener(this);
			if (itemEvent.getStateChange() == ItemEvent.DESELECTED)
			{
				// Close the dockable.
				closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
			} 
			else 
			{
				// Restore the dockable.
				restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
			}
			dockable.addDockingListener(this);
			jSplitPane2.resetToPreferredSizes();
			jSplitPane1.resetToPreferredSizes();

		}

		public void dockingChanged(DockingEvent dockingEvent) {
			if (dockingEvent.getDestinationDock() != null)
			{
				dockableMenuItem.removeItemListener(this);
				dockableMenuItem.setSelected(true);
				dockableMenuItem.addItemListener(this);	
			}
			else
			{
				dockableMenuItem.removeItemListener(this);
				dockableMenuItem.setSelected(false);
				dockableMenuItem.addItemListener(this);
			}
			jSplitPane2.resetToPreferredSizes();
			jSplitPane1.resetToPreferredSizes();
		}

		public void dockingWillChange(DockingEvent dockingEvent) {}
	}
}
