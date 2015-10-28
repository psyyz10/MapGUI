package view;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.soap.Text;

import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;

import controller.NewFileDialog;


import model.Model;
import model.Waypoint;

/** Panel for displaying the waypoints data */
public class DisplayDataTablePanel extends JPanel implements ActionListener, DraggableContent{
	private Model model;
	private Waypoint waypoint;
	private String[] columnNames = {"Point", "Latitude", "Longtitude", "Elevation"};
	private MyDefaultTableModel tableModel = new MyDefaultTableModel(null, columnNames);
	private JTable jTable = new JTable(tableModel);
	private JScrollPane scrollPane = new JScrollPane(jTable);
	private JLabel jlbMinLat = new JLabel();
	private JLabel jlbMinLon = new JLabel();
	private JLabel jlbMaxLat = new JLabel();
	private JLabel jlbMaxLon = new JLabel();
	private JPanel jplBounds = new JPanel();
	private JPopupMenu popMenus = new JPopupMenu();
	private JMenuItem jmiCopy = new JMenuItem("Copy", new ImageIcon("image/copy.png"));
	private JMenuItem jmiPaste = new JMenuItem("Paste", new ImageIcon("image/paste.png"));
	private JMenuItem jmiCut = new JMenuItem("Cut", new ImageIcon("image/cut.png"));
	private JMenuItem jmiDescription = new JMenuItem("Add description");
	private JMenuItem jmiSymbol = new JMenuItem("Add a symbol");
	private JCheckBoxMenuItem jmiShowSymbol = new JCheckBoxMenuItem("Show Symbol",true);
	private JCheckBoxMenuItem jmiShowDescription = new JCheckBoxMenuItem("Show Description");
	private Clipboard clip = getToolkit().getSystemClipboard();
	private double[] bounds;
	private String[] symbols = {"home", "hospital", "church", "restaurant", 
			"work place","bus station", "cafe", "shop", "school", "park"};

	/** Construct the panel */
	public DisplayDataTablePanel(){
		jplBounds.setPreferredSize(new Dimension(250, 30));
		jplBounds.setLayout(new GridLayout(2,2,0,0));
		jplBounds.add(jlbMinLat);
		jplBounds.add(jlbMinLon);
		jplBounds.add(jlbMaxLat);
		jplBounds.add(jlbMaxLon);
		jplBounds.setBackground(Color.WHITE);

		scrollPane.setViewportView(jTable);
		scrollPane.setSize(jTable.getSize());
		scrollPane.setBackground(Color.WHITE);
		setPreferredSize(new Dimension(250, 300));
		setMinimumSize(new Dimension(150, 100));
		setLayout(new BorderLayout());
		add(jplBounds, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		popMenus.add(jmiCopy);
		popMenus.add(jmiPaste);
		popMenus.add(jmiCut);
		popMenus.addSeparator();
		popMenus.add(jmiSymbol);
		popMenus.add(jmiDescription);
		popMenus.addSeparator();
		popMenus.add(jmiShowSymbol);
		popMenus.add(jmiShowDescription);

		// Initialize jTable
		jTable.setShowGrid(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setRowSelectionAllowed(true);
		jTable.getTableHeader().setFont( new Font( "Arial" , Font.BOLD, 12));
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		jTable.setGridColor(Color.BLUE);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setSelectionForeground(Color.WHITE);

		//Implement copy function
		jmiCopy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {	
				if (jTable.getSelectedRow() == -1)
					return;
				waypoint = model.getWaypoints().get(jTable.getSelectedRow());
				String selection = waypoint.convertToString();
				StringSelection clipString = new StringSelection(selection);
				clip.setContents(clipString, clipString);
			}			
		});

		//Implement paste function
		jmiPaste.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Transferable clipData = clip.getContents(this);
				try{
					String clipString = (String) clipData
							.getTransferData(DataFlavor.stringFlavor);
					waypoint.convertToWaypoint(clipString.substring(1));
					System.out.print(clipString);
					model.addAWaypoint(waypoint);
				} catch (Exception ex){
					JOptionPane.showMessageDialog(null, "Not String flavor ",
							"Not found", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		//Implement cut function
		jmiCut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (jTable.getSelectedRow() == -1){
					JOptionPane.showMessageDialog(null, "Select a row of data",
							"Not found", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				model.deleteAWaypoint(jTable.getSelectedRow());
			}
		});

		//Implement add description function
		jmiDescription.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (jTable.getSelectedRow() == -1){
					JOptionPane.showMessageDialog(null, "Select a row of data",
							"Not found", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				DataDescription dialog = new DataDescription();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});
		
		//Implement add a symbol function
		jmiSymbol.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (jTable.getSelectedRow() == -1){
					JOptionPane.showMessageDialog(null, "Select a row of data",
							"Not found", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				Object symbol = JOptionPane.showInputDialog(null, "Select a symbol", "Symbol selection", 
						JOptionPane.QUESTION_MESSAGE, null, symbols, null);
				if (symbol != null)
					model.addASymbol(jTable.getSelectedRow(), (String)symbol);
			}
		});
		
		//Implement show description function
		jmiShowDescription.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (jTable.getSelectedRow() == -1){
					JOptionPane.showMessageDialog(null, "Select a row of data",
							"Not found", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				model.setShowDescription(jTable.getSelectedRow(), jmiShowDescription.isSelected());
			}
		});
		
		//Implement show symbol function
		jmiShowSymbol.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (jTable.getSelectedRow() == -1){
					JOptionPane.showMessageDialog(null, "Select a row of data",
							"Not found", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				model.setShowSymbol(jTable.getSelectedRow(), jmiShowSymbol.isSelected());
			}
		});

		// mouse listener for popup menu
		jTable.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				showPopup(e);
			}

			public void mouseReleased(MouseEvent e){
				showPopup(e);
			}
		});

		scrollPane.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				showPopup(e);
			}

			public void mouseReleased(MouseEvent e){
				showPopup(e);
			}
		});

	}


	private void showPopup(MouseEvent evt){
		if (jTable.getSelectedRow() != -1){
			jmiShowDescription.setState(model.getWaypoints().get(jTable.getSelectedRow()).isShowDescription());
			jmiShowSymbol.setState(model.getWaypoints().get(jTable.getSelectedRow()).isShowSymbol());
		}
		if(evt.isPopupTrigger())
			popMenus.show(evt.getComponent(), evt.getX(), evt.getY());
	}

	/** When model change, reset the list*/
	public void actionPerformed(ActionEvent actionEvent){
		if (model == null) return;
		reSetTable();
	}

	/** reset the table when model changed */
	public void reSetTable(){
		if (model == null) return;
		String waypointsString = model.convertWaypointsToString();
		if (waypointsString != null && waypointsString != ""){
			resetBounds();
			//tableModel = new DefaultTableModel(null, columnNames);
			tableModel.setRowCount(0);
			String[] tokens = waypointsString.split("\r\n", 0);
			for (int i = 1; i < tokens.length; i++){
				String[] tokens1 = tokens[i].split(",", 0);
				tableModel.addRow(tokens1);
			}
		}	
		else if (model.getFileName() != null){
			resetBounds();
		}
	}

	/** reset the bounds when model changed */
	public void resetBounds(){
		bounds = model.getBounds();
		jlbMinLat.setText("MinLat : " + bounds[0]);
		jlbMinLon.setText("MinLon : " + bounds[1]);
		jlbMaxLat.setText("MaxLat: " + bounds[2]);
		jlbMaxLon.setText("MaxLon: " + bounds[3]);		
	}

	/** rewrite the table model to realize not cell editable but the row can be selected */
	class MyDefaultTableModel extends DefaultTableModel {  
		public MyDefaultTableModel(Object[][] row, Object[] column) {  
			super(row, column);  
		}  
		public boolean isCellEditable(int row, int col) {  
			return false;  
		}  
	}  
	public Model getModel(){
		return model;
	}

	public void setModel(Model model){
		this.model = model;

		if(model != null)
			model.addActionListener(this);

		reSetTable();
	}


	public void addDragListener(DragListener dragListener)
	{
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
	}

	// A Dialog to input description
	class DataDescription extends JDialog{
		JTextArea jta = new JTextArea();
		// Create two buttons
		private JButton jbtOK = new JButton("OK");
		private JButton jbtCancel = new JButton("Cancel");

		public DataDescription(){
			this(null, true);
		}
		public DataDescription (java.awt.Frame parent, boolean modal){
			super(parent, modal);
			
			setLocationRelativeTo(null);

			setTitle("Data description");
			JScrollPane jsp = new JScrollPane(jta);
			jsp.setPreferredSize(new Dimension(700, 400));
			// Group two buttons OK and Cancel
			JPanel jpButtons = new JPanel();
			jpButtons.add(jbtOK);
			jpButtons.add(jbtCancel);

			JPanel jpInput = new JPanel(new BorderLayout());
			jpInput.add(new JLabel("Add a description for the point"), BorderLayout.NORTH);
			jpInput.add(jsp, BorderLayout.CENTER);
			add(jpButtons, BorderLayout.SOUTH);
			add(jpInput, BorderLayout.CENTER);
			pack();

			// Add listener to OK button
			jbtOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.addDescription(jTable.getSelectedRow(), jta.getText());
					setVisible(false);
				}
			});

			// Add listener to Cancel button
			jbtCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
		}
	}
}
