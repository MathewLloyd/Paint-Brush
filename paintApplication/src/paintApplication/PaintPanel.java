package paintApplication;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PaintPanel extends JPanel {
	private Color mPaintColour = new Color(200, 80, 100);
	private int brushType, brushThickness, x, y, diff = 10;
	private float multiBrush = 1.5f;
	private String eventDesc = "POS", direction;
	private int centreX, centreY;
	private ArrayList<Action> actions;
	private Graphics2D g3;
	
	private BufferedImage m_image;


	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
		paintAll(img.getGraphics());
		return img;
	}
	
	public void clearImage(){
		actions.clear();
		repaint();
	}
	
	public PaintPanel(){
		super();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		x = centreX = env.getCenterPoint().x;
		y = centreY = env.getCenterPoint().y;
		
		
		//System.out.println("X: " + x + " Y: " + y);
		brushType = 1;
		brushThickness = 5;

		eventDesc = "NULL";

		//setDirection("RIGHT");
		setBackground(Color.WHITE);

		actions = new ArrayList<Action>();

	}
	@Override
	public void paintComponent(Graphics g) // draw graphics in the panel
	{
		super.paintComponent(g);
		Robot robot;
		Graphics2D g2 = (Graphics2D) g;

		//System.out.println(eventDesc);
		Action a = new Action(eventDesc);
		switch (eventDesc) {
		case "POS":
			if (direction.equals("UP")) {
				a.setLine(x, y, x, y - diff,brushType, mPaintColour, brushThickness);
				if (y - diff >= 0) {
					y -= diff;
				}
			} else if (direction.equals("RIGHT")) {
				a.setLine(x, y, x + diff, y,brushType, mPaintColour, brushThickness);
				if (x + diff <= getWidth()) {
					x += diff;
				}
			} else if (direction.equals("LEFT")) {
				a.setLine(x, y, x - diff, y,brushType, mPaintColour, brushThickness);
				if (x - diff >= 0) {
					x -= diff;
				}
			} else {// DOWN
				a.setLine(x, y, x, y + diff,brushType, mPaintColour, brushThickness);
				if (y - diff <= getHeight()) {
					y += diff;
				}
			}
			actions.add(a);
			break;
		case "CENT":
			a.setCentre(x, y);
			break;
		}
		if(!eventDesc.equals("NULL")){
			try {
				robot = new Robot();
				//System.out.println("X: " + x + "CenterX: " + centreX + "Y: " + y + "CenterY: " + centreY);
				robot.mouseMove(getLocationOnScreen().x + x, getLocationOnScreen().y + y);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	
		for (Action act : actions) {
			switch (act.getType()) {
			case "IMG":
				System.out.println("PaintPanel:: paintComponent():: Case:IMG");
				g2.drawImage(m_image, 0, 0, null);
				break;
			case "COL":

				repaint();
				eventDesc = "NULL";
				break;

			case "POS":
				
				if (act.getBrushType() == 1) {
					Stroke s = new BasicStroke(act.getThickness()*3, // Width
							BasicStroke.CAP_SQUARE, // End cap
							BasicStroke.JOIN_MITER, // Join style
							1.0f, // Miter limit
							new float[] { 10.0f, 10.0f }, // Dash pattern
							0.0f);
					g2.setStroke(s);// Dash phase
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				} else if (act.getBrushType() == 2) {

					//System.out.println("BRUSH TYPE == 2:: X: " + x + " Y: " + y);
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						g2.drawOval(act.getLineX(), act.getLineY(), act.getThickness(), act.getThickness());
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}

				} else if (act.getBrushType() == 3) {
					//System.out.println("BRUSH TYPE == 2:: X: " + x + " Y: " + y);
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						g2.fillOval(act.getLineX(), act.getLineY(), act.getThickness()*5, act.getThickness()*5);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				} else if (act.getBrushType() == 4) {
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						g2.drawPolygon(new int[]{(int)(act.getLineX()-(multiBrush*act.getThickness())), act.getLineX(), (int)(act.getLineX()+(multiBrush*act.getThickness()))}, new int[]{(int)(act.getLineY()+(multiBrush*act.getThickness())), (int)(act.getLineY()-(multiBrush*act.getThickness())), (int)(act.getLineY()+(multiBrush*act.getThickness()))}, 3);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				} else if (act.getBrushType() == 5) {
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						g2.fillPolygon(new int[]{(int)(act.getLineX()-(multiBrush*act.getThickness())), act.getLineX(), (int)(act.getLineX()+(multiBrush*act.getThickness()))}, new int[]{(int)(act.getLineY()+(multiBrush*act.getThickness())), (int)(act.getLineY()-(multiBrush*act.getThickness())), (int)(act.getLineY()+(multiBrush*act.getThickness()))}, 3);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				}else if (act.getBrushType() == 6) {
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						double c1 = Math.cos((2*Math.PI/5)) * (multiBrush*act.getThickness());
						double c2 =  Math.cos((Math.PI/5)) * (multiBrush*act.getThickness());
						double s1 =  Math.sin((2*Math.PI)/5) * (multiBrush*act.getThickness());
						double s2 =  Math.sin((4*Math.PI)/5) * (multiBrush*act.getThickness());
						//System.out.println(c1 + " " + c2+ " " + s1+ " " + s2+ " ");
						g2.drawPolygon(new int[]{(int)(act.getLineX()),(int)(act.getLineX()+s1), (int)(act.getLineX() + s2),(int)( act.getLineX() - s2), (int)(act.getLineX() - s1 )}, 
										new int[]{act.getLineY()+(int)(multiBrush*act.getThickness()),(int)(act.getLineY()+c1), (int)(act.getLineY() - c2), (int)(act.getLineY() - c2), (int)(act.getLineY() + c1)}, 5);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				}else if (act.getBrushType() == 7) {
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						double c1 = Math.cos((2*Math.PI/5)) * (multiBrush*act.getThickness());
						double c2 =  Math.cos((Math.PI/5)) * (multiBrush*act.getThickness());
						double s1 =  Math.sin((2*Math.PI)/5) * (multiBrush*act.getThickness());
						double s2 =  Math.sin((4*Math.PI)/5) * (multiBrush*act.getThickness());
						//System.out.println(c1 + " " + c2+ " " + s1+ " " + s2+ " ");
						g2.fillPolygon(new int[]{(int)(act.getLineX()),(int)(act.getLineX()+s1), (int)(act.getLineX() + s2),(int)( act.getLineX() - s2), (int)(act.getLineX() - s1 )}, 
										new int[]{act.getLineY()+(int)(multiBrush*act.getThickness()),(int)(act.getLineY()+c1), (int)(act.getLineY() - c2), (int)(act.getLineY() - c2), (int)(act.getLineY() + c1)}, 5);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				}else if (act.getBrushType() == 8) {
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						int nX = (int)(Math.cos(108) * multiBrush*act.getThickness());
						int nY = (int)(multiBrush*act.getThickness());
						
						g2.fillPolygon(new int[]{act.getLineX() + nX, act.getLineX() + nY, act.getLineX() + nX, act.getLineX() - nX, act.getLineX() - nY, act.getLineX() - nX}, 
										new int[]{act.getLineY() - nY, act.getLineY(), act.getLineY() + nY, act.getLineY() + nY, act.getLineY(), act.getLineY() - nY}, 6);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				}else if (act.getBrushType() == 9) {
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						int nX = (int)(Math.cos(30) * multiBrush*act.getThickness());
						int nY = (int)(multiBrush*act.getThickness());
						
						g2.fillPolygon(new int[]{act.getLineX() + nX, act.getLineX() + nY, act.getLineX() + nX, act.getLineX() - nX, act.getLineX() - nY, act.getLineX() - nX}, 
										new int[]{act.getLineY() - nY, act.getLineY(), act.getLineY() + nY, act.getLineY() + nY, act.getLineY(), act.getLineY() - nY}, 6);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				}else if (act.getBrushType() == 10) {
					g2.setColor(act.getColor());
					if (act.getType().equals("POS")) {
						int nX = (int)(Math.cos(72) * multiBrush*act.getThickness());
						int nY = (int)(multiBrush*act.getThickness());
						
						g2.fillPolygon(new int[]{act.getLineX() + nX, act.getLineX() + nY, act.getLineX() + nX, act.getLineX() - nX, act.getLineX() - nY, act.getLineX() - nX}, 
										new int[]{act.getLineY() - nY, act.getLineY(), act.getLineY() + nY, act.getLineY() + nY, act.getLineY(), act.getLineY() - nY}, 6);
						//g2.drawLine(act.getLineX(), act.getLineY(), act.getLineXDist(), act.getLineYDist());
					}
				}
				

				eventDesc = "NULL";
				break;

			case "CENT":
				//System.out.println("Center, X: " + centreX + " Y: " + centreY);


				try {
					robot = new Robot();
					robot.mouseMove(centreX, centreY);
					// Transparent 16 x 16 pixel cursor image.
					BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

					// Create a new blank cursor.
					Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
							cursorImg, new Point(0, 0), "blank cursor");

					// Set the blank cursor to the JFrame.
					this.setCursor(blankCursor);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));


				// setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				// Make the cursor flash in the center

				//Calling repaint calls the paintComponent method.
				//repaint();
				eventDesc = "NULL";
				break;

			case "UNDO":

				repaint();
				eventDesc = "NULL";
				break;
			default:

				break;
			}
		}

	}

	public Color getmPaintColour() {
		return mPaintColour;
	}

	public void setmPaintColour(Color mPaintColour) {
		this.mPaintColour = mPaintColour;
		// eventDesc = "COL";
	}

	/**
	 * @return the brushType
	 */
	public int getBrushType() {
		return brushType;
	}

	/**
	 * @param brushType
	 *            the brushType to set
	 */
	public void setBrushType(int brushType) {
		this.brushType = brushType;
		System.out.println("Brush Type: " + brushType);
	}

	/**
	 * @return the brushThickness
	 */
	public int getBrushThickness() {
		return brushThickness;
	}

	/**
	 * @param brushThickness
	 *            the brushThickness to set
	 */
	public void setBrushThickness(int brushThickness) {
		this.brushThickness = brushThickness;
		
	}

	/**
	 * @return the brushThickness
	 */
	public int getDirection() {
		return brushThickness;
	}

	/**
	 * @param brushThickness
	 *            the brushThickness to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
		eventDesc = "POS";
		repaint();
	}

	public void setCentre() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		x = centreX = getWidth()/2;
		y = centreY = getHeight()/2;
		eventDesc = "CENT";
		repaint();
	}

	public void undo() {
		if (actions.size() > 0) {
			if(!actions.get(actions.size()-1).getType().equals("IMG")){
				actions.remove(actions.size()-1);
			}
		}
		eventDesc = "UNDO";
		repaint();
	}

	public void loadImage(BufferedImage image) {
		clearImage();
		Action a = new Action("IMG");
		BufferedImage resizedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		m_image = resizedImage;
		a.setImage(resizedImage);
		actions.add(a);
		repaint();
		
	}
}
