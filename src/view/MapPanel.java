package view;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.*;
import model.Model;
import model.Waypoint;

/** A panel display the map embedded in the map tab */
public class MapPanel extends JPanel{
	private Model model;
	private ArrayList<Waypoint> waypoints;
	private double[] bounds;
	private int x, y, xDiff, yDiff, width, height, mouseX, mouseY, 
	mouseX2, mouseY2,mapWidth, mapHeight;
	private BufferedImage bufImage; 
	private BufferedImage originalBufImage; 
	private Graphics2D bufImageG; 	
	private double scale = 1.0;  
	private double scaleDiff;
	private Image image;
	private DrawPanel drawPanel = new DrawPanel();
	private JLabel label;
	private Point position;
	private double minLat, minLon, maxLat, maxLon;

	//construct the MapPanel
	public MapPanel(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(drawPanel);
		label = new JLabel("");
		label.setLabelFor(drawPanel);
		label.setFont(new Font("Verdana",Font.PLAIN, 15));
		add(label);

		//Align the left edges of the components.
		drawPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setAlignmentX(Component.LEFT_ALIGNMENT); //redundant
	}

	// This panel is for drawing and displaying the map
	class DrawPanel extends JPanel implements ActionListener {	
		private JPopupMenu popMenus = new JPopupMenu();
		private JMenuItem jmiZoomIn = new JMenuItem("Zoom in", new ImageIcon("image/zoomin.png"));
		private JMenuItem jmiZoomOut = new JMenuItem("Zoom out", new ImageIcon("image/zoomout.png"));
		private JMenuItem jmiCenter = new JMenuItem("Center map here");
		private JMenuItem jmiAdd = new JMenuItem("Add this point to GPS data");
		private boolean start = true;
		private boolean zoom, center;
		private JSlider jsld = new JSlider(JSlider.VERTICAL);
		private Timer timer = new Timer(300, new TimerListener());
		private Image home = new ImageIcon("image/home.png").getImage();
		private Image busstation = new ImageIcon("image/busstation.png").getImage();
		private Image workplace = new ImageIcon("image/workplace.png").getImage();
		private Image hospital = new ImageIcon("image/hospital.png").getImage();
		private Image restaurant = new ImageIcon("image/restaurant.png").getImage();
		private Image cafe = new ImageIcon("image/cafe.png").getImage();
		private Image church = new ImageIcon("image/church.png").getImage();
		private Image shop = new ImageIcon("image/shop.png").getImage();
		private Image school = new ImageIcon("image/school.png").getImage();
		private Image park = new ImageIcon("image/park.png").getImage();

		public DrawPanel(){
			setLayout(new BorderLayout(5, 5));
			loadImage("image/map.gif");
			setBackground(Color.BLACK);
			//new Color(230,230,250)
			setPreferredSize(new Dimension(700, 600));
			setMinimumSize(new Dimension(350, 100));

			jsld.setInverted(true);
			jsld.setMinimum(0);
			jsld.setMaximum(50);
			jsld.setPaintLabels(true);
			jsld.setPaintTicks(true);
			jsld.setMajorTickSpacing(5);
			jsld.setMinorTickSpacing(1);

			popMenus.add(jmiZoomIn);
			popMenus.add(jmiZoomOut);
			popMenus.addSeparator();
			popMenus.add(jmiCenter);
			popMenus.addSeparator();
			popMenus.add(jmiAdd);

			//add(jsld, BorderLayout.EAST);
			addMouseMotionListener(new MouseMotionHandler());
			addMouseWheelListener(new MouseWheelListener1());
			addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					xDiff = x - e.getX();
					yDiff = y - e.getY();
					showPopup(e);
				}

				public void mouseReleased(MouseEvent e){
					mouseX2 = e.getX();
					mouseY2 = e.getY();
					showPopup(e);
				}
			});

			jmiZoomIn.addActionListener(new ButtonActionListener());
			jmiZoomOut.addActionListener(new ButtonActionListener());
			jmiCenter.addActionListener(new ButtonActionListener());
			jmiAdd.addActionListener(new ButtonActionListener());
		}

		// when model changed, repaint
		public void actionPerformed(ActionEvent actionEvent){
			if (model == null) return;
			repaint();
		}

		//load map image to the panel
		public void loadImage(String fileName) {
			image = this.getToolkit().getImage(fileName); 
			MediaTracker mt = new MediaTracker(this); 
			mt.addImage(image, 1); 
			try {
				mt.waitForAll(); 
			} catch (Exception ex) {
				ex.printStackTrace();  
			}
			mapWidth = image.getWidth(null);
			mapHeight = image.getHeight(null);
			originalBufImage =	new BufferedImage(image.getWidth(this),image.getHeight(this),BufferedImage.TYPE_INT_ARGB); 	
			bufImage = originalBufImage;
			bufImageG = bufImage.createGraphics(); 
			bufImageG.drawImage(image, 0, 0, this); 
			start = true;
			repaint(); 
		}

		// apply filter for zoom in and zoom out
		public synchronized void applyFilter() {
			if (bufImage == null)
				return; 
			BufferedImage filteredBufImage =new BufferedImage((int) (image.getWidth(this) * scale),(int) (image.getHeight(this) * scale),BufferedImage.TYPE_INT_ARGB); 
			AffineTransform transform = new AffineTransform(); 
			transform.setToScale(scale, scale); 
			AffineTransformOp imageOp = new AffineTransformOp(transform, null);		
			imageOp.filter(originalBufImage, filteredBufImage);
			bufImage = filteredBufImage; 
			repaint();
		}

		// paint the map
		public void paint(Graphics g) {
			super.paintComponent(g);

			if (start){
				x = (getWidth() - bufImage.getWidth()) / 2;
				y = (getHeight() - bufImage.getHeight()) / 2;			
			}
			if (center){
				x -= (mouseX2 - width / 2);
				y -= (mouseY2 - height / 2);
				center = false;
			}
			if (bufImage != null) {
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(bufImage,x,y,this);
				waypoints = model.getWaypoints();

				for (int i = 0; i < waypoints.size(); i++){
					position = convertWaypointToPoint(waypoints.get(i));
					g2.drawString(waypoints.get(i).getName(), (int) position.getX() - 6, (int) position.getY() + 16);
					
					//draw description
					if (waypoints.get(i).isShowDescription())
						drawDescription(g2, waypoints.get(i).getDescription() , (int) position.getX() + 3, (int) position.getY() + 20);
					
					//draw symbol
					if (waypoints.get(i).isShowSymbol())
						drawSymbol(g2, waypoints.get(i).getSymbol(), (int) position.getX() - 5, (int) position.getY() - 17, this);
				}

				//drawing the red rectangle arcs, when zoom in or zoom out
				if (zoom){
					g2.setStroke(new BasicStroke(3));
					g2.setColor(Color.RED);
					g2.drawLine(mouseX - 20, mouseY - 10, mouseX - 20, mouseY - 15);
					g2.drawLine(mouseX - 15, mouseY - 15, mouseX - 20, mouseY - 15);
					g2.drawLine(mouseX + 15, mouseY - 15, mouseX + 20, mouseY - 15);
					g2.drawLine(mouseX + 20, mouseY - 10, mouseX + 20, mouseY - 15);
					g2.drawLine(mouseX - 15, mouseY + 15, mouseX - 20, mouseY + 15);
					g2.drawLine(mouseX - 20, mouseY + 10, mouseX - 20, mouseY + 15);
					g2.drawLine(mouseX + 20, mouseY + 10, mouseX + 20, mouseY + 15);
					g2.drawLine(mouseX + 15, mouseY + 15, mouseX + 20, mouseY + 15);
				}
				start = false;
				zoom = false;
				height = getHeight();
				width = getWidth();
			}
		}

		private void showPopup(MouseEvent evt){
			if(evt.isPopupTrigger())
				popMenus.show(evt.getComponent(), evt.getX(), evt.getY());
		}


		// Popup menu item's listener
		class ButtonActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JMenuItem jmi = (JMenuItem)e.getSource();
				
				//Zoom in 
				if(jmi == jmiZoomIn) {
					if (scale > 5) return;
					scaleDiff = 0.25;
					scale *= 1.25; 		
					x -= (mouseX - x) * scaleDiff;
					y -= (mouseY - y) * scaleDiff;
					scaleDiff = 0;
					zoom = true;
					timer.start();
					applyFilter(); 
					
				//Zoom out
				} else if(jmi == jmiZoomOut) {
					if (scale < 0.2) return;
					scaleDiff = - 0.2;
					scale *= 0.8; 
					x -= (mouseX - x) * scaleDiff;
					y -= (mouseY - y) * scaleDiff;
					scaleDiff = 0;
					zoom = true;
					timer.start();
					applyFilter(); 
					
				//Center the map	
				}else if(jmi == jmiCenter) {
					center = true;
					repaint();
					
				//Add the map point to gps data model
				}else if(jmi == jmiAdd) {
					String waypointName = JOptionPane.showInputDialog(null, "Entry a waypoint name");
					
					if (waypointName != null)
						model.addAWaypoint(new Waypoint(waypointName, (int)((mouseY - y) * (maxLat - minLat) /(scale * mapHeight) + minLat)
								, (int)((mouseX - x) * (maxLon - minLon) /(scale * mapWidth) + minLon), 0));
				}
			}
		}

		class MouseWheelListener1 implements MouseWheelListener {

			// When mouse wheel rotates, zoom in or zoom out
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation();
				if (notches < 0) {
					if (scale > 5) return;
					scaleDiff = 0.25;
					scale *= 1.25; 
				} else {
					if (scale < 0.2) return;
					scaleDiff = - 0.2;
					scale *= 0.8; 
				}
				x -= (mouseX - x) * scaleDiff;
				y -= (mouseY - y) * scaleDiff;
				scaleDiff = 0;
				zoom = true;
				timer.start();
				applyFilter(); 
			}

		}

		// Use timer to add red rectangle arcs when zoom in or zoom out
		class TimerListener implements ActionListener {
			public void actionPerformed(ActionEvent arg0) {
				repaint();
				timer.stop();
			}

		}

		// convert the waypoint's map location to panel coordinate
		public Point convertWaypointToPoint(Waypoint waypoint){
			double latitude = waypoint.getLatitude();
			double longitude = waypoint.getLongtitude();

			return new Point((int) (x + scale * mapWidth * (longitude - minLon) / (maxLon - minLon) )
					, (int) (y + scale * mapHeight * (latitude - minLat) / (maxLat - minLat) ));
		}

		// draw the description
		private void drawDescription(Graphics2D g2, String text, int x, int y) {
			FontMetrics fm = g2.getFontMetrics();
			int stringWidth = 6;
			int stringAHeight = 5;

			for (String line : text.split("\n")){
				if (stringWidth < fm.stringWidth(line))
					stringWidth = fm.stringWidth(line) + 6;
				stringAHeight += fm.getHeight();
			}

			Rectangle r = new Rectangle(x, y ,stringWidth, stringAHeight);
			g2.setPaint(Color.WHITE);
			g2.fill(r);
			g2.setPaint(Color.black);
			g2.draw(r);

			for (String line : text.split("\n"))
				g2.drawString(line, x + 3, y += g2.getFontMetrics().getHeight());
		}

		// draw the symbol
		private void drawSymbol (Graphics2D g2, String symbol, int x, int y, ImageObserver observer){
			switch (symbol){
			case "home": g2.drawImage(home, x, y, observer); break;
			case "hospital": g2.drawImage(hospital, x, y, observer); break;
			case "church": g2.drawImage(church, x, y, observer); break;
			case "restaurant": g2.drawImage(restaurant, x, y, observer); break;
			case "work place": g2.drawImage(workplace, x, y, observer); break;
			case "bus station": g2.drawImage(busstation, x, y, observer); break;
			case "cafe": g2.drawImage(cafe, x, y, observer); break;
			case "shop": g2.drawImage(shop, x, y, observer); break;
			case "school": g2.drawImage(school, x, y, observer); break;
			case "park": g2.drawImage(park, x, y, observer); break;
			default: g2.fillRect(x + 5, y + 17, 4, 4); break;
			}

		}
	}

	// implements drag map function and display mouse location function
	class MouseMotionHandler extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			x = e.getX() + xDiff;
			y = e.getY() + yDiff;
			repaint();
		}
		public void mouseMoved(MouseEvent e){
			mouseX = e.getX();
			mouseY = e.getY();

			label.setText("This point's latitude and longtitude is (" 
					+ (int)((mouseY - y) * (maxLat - minLat) /(scale * mapHeight) + minLat) + ", "
					+ (int)((mouseX - x) * (maxLon - minLon) /(scale * mapWidth) + minLon) + ")");

		}
	}

	// get the model
	public Model getModel(){
		return model;
	}

	// set the model
	public void setModel(Model model){
		this.model = model;
		bounds = model.getBounds();
		minLat = bounds[0];
		minLon = bounds[1];
		maxLat = bounds[2];
		maxLon = bounds[3];

		if(model != null)
			model.addActionListener(drawPanel);

		repaint();
	}
}


