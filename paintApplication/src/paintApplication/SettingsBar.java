package paintApplication;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JPanel;

public class SettingsBar extends JPanel {
	private Color mPaintColour = new Color(200, 80, 100);
	private int brushType = 1, brushThickness = 5, mAlpha, x, y;
	private Graphics2D g2;





	public SettingsBar(){
		super();

		setPreferredSize(new Dimension(getWidth(),45));
		setBackground(new Color(223,234,237));
		repaint();

	}
	@Override
	public void paintComponent(Graphics g) // draw graphics in the panel
	{
		super.paintComponent(g);
		//Font font = new Font("Arial", Font.ITALIC, 15);
		// Create Icon to represent the current colour
		g2 = (Graphics2D) g;
		g2.setColor(mPaintColour);
		//System.out.println("Out brush" + mPaintColour);
		g2.fillRect( 475, 0, 40, 40 );
		
		
		//add(Box.createHorizontalStrut(40));
		
		String text = "Colour:";
		g2.setColor(Color.BLACK);
		g2.drawRect(427, 0, 87, 40);
		g2.drawString(text, 430, 25);
		
		//Opacity icon
		text = "Opacity: " + mAlpha + "%";
		g2.drawRect(535, 0, 90, 40);
		g2.drawString(text, 540, 25);
		
		//Brush type icon
		text = "Brush Type: ";
		g2.drawRect(646, 0, 100, 40);
		g2.drawString(text, 651, 25);
		int x = 730;
		int y = 20;
		int diff = 10;
		
		if (brushType == 1) {
			Stroke s = new BasicStroke(brushThickness*3, // Width
					BasicStroke.CAP_SQUARE, // End cap
					BasicStroke.JOIN_MITER, // Join style
					1.0f, // Miter limit
					new float[] { 10.0f, 10.0f }, // Dash pattern
					0.0f);
			g2.setStroke(s);// Dash phase
			g2.setColor(mPaintColour);
			g2.drawLine(x, y, x+diff, y+diff);
			
			s = new BasicStroke(1, // Width
					BasicStroke.CAP_SQUARE, // End cap
					BasicStroke.JOIN_MITER, // Join style
					1.0f, // Miter limit
					new float[] { 1.0f}, // Dash pattern
					0.0f);
			g2.setStroke(s);
			
		} else if (brushType == 2) {

			g2.setColor(mPaintColour);

			g2.drawOval(x-brushThickness*2, y-brushThickness*2, brushThickness*5, brushThickness*5);

		} else if (brushType == 3) {
			//System.out.println("BRUSH TYPE == 2:: X: " + x + " Y: " + y);
			g2.setColor(mPaintColour);

			g2.fillOval(x-brushThickness*2, y-brushThickness*2, brushThickness*5, brushThickness*5);

		} else if (brushType == 4) {
			g2.setColor(mPaintColour);

			g2.drawPolygon(new int[]{(int)(x-(2.5*brushThickness)), x, (int)(x+(2.5*brushThickness))}, new int[]{(int)(y+(2.5*brushThickness)), (int)(y-(2.5*brushThickness)), (int)(y+(2.5*brushThickness))}, 3);

		} else if (brushType == 5) {
			g2.setColor(mPaintColour);

			g2.fillPolygon(new int[]{(int)(x-(2.5*brushThickness)), x, (int)(x+(2.5*brushThickness))}, new int[]{(int)(y+(2.5*brushThickness)), (int)(y-(2.5*brushThickness)), (int)(y+(2.5*brushThickness))}, 3);

		}else if (brushType == 6) {
			g2.setColor(mPaintColour);

			double c1 = Math.cos((2*Math.PI/5)) * (2.5*brushThickness);
			double c2 =  Math.cos((Math.PI/5)) * (2.5*brushThickness);
			double s1 =  Math.sin((2*Math.PI)/5) * (2.5*brushThickness);
			double s2 =  Math.sin((4*Math.PI)/5) * (2.5*brushThickness);
			//System.out.println(c1 + " " + c2+ " " + s1+ " " + s2+ " ");
			g2.drawPolygon(new int[]{(int)(x),(int)(x+s1), (int)(x + s2),(int)( x - s2), (int)(x - s1 )}, 
					new int[]{y+(int)(2.5*brushThickness),(int)(y+c1), (int)(y - c2), (int)(y - c2), (int)(y + c1)}, 5);
			//g2.drawLine(x, y, act.getLineXDist(), diff);

		}else if (brushType == 7) {
			g2.setColor(mPaintColour);

			double c1 = Math.cos((2*Math.PI/5)) * (int)(2.5*brushThickness);
			double c2 =  Math.cos((Math.PI/5)) * (int)(2.5*brushThickness);
			double s1 =  Math.sin((2*Math.PI)/5) * (int)(2.5*brushThickness);
			double s2 =  Math.sin((4*Math.PI)/5) * (int)(2.5*brushThickness);
			//System.out.println(c1 + " " + c2+ " " + s1+ " " + s2+ " ");
			g2.fillPolygon(new int[]{(int)(x),(int)(x+s1), (int)(x + s2),(int)( x - s2), (int)(x - s1 )}, 
					new int[]{y+(int)(2.5*brushThickness),(int)(y+c1), (int)(y - c2), (int)(y - c2), (int)(y + c1)}, 5);

		}else if (brushType == 8) {
			g2.setColor(mPaintColour);

			int nX = (int)(Math.cos(108) * 2.5*brushThickness);
			int nY = (int)(2.5*brushThickness);

			g2.fillPolygon(new int[]{x + nX, x + nY, x + nX, x - nX, x - nY, x - nX}, 
					new int[]{y - nY, y, y + nY, y + nY, y, y - nY}, 6);
			//g2.drawLine(x, y, act.getLineXDist(), diff);

		}else if (brushType == 9) {
			g2.setColor(mPaintColour);

			int nX = (int)(Math.cos(30) * 2.5*brushThickness);
			int nY = (int)(2.5*brushThickness);

			g2.fillPolygon(new int[]{x + nX, x + nY, x + nX, x - nX, x - nY, x - nX}, 
					new int[]{y - nY, y, y + nY, y + nY, y, y - nY}, 6);

		}else if (brushType == 10) {
			g2.setColor(mPaintColour);

			int nX = (int)(Math.cos(72) * 2.5*brushThickness);
			int nY = (int)(2.5*brushThickness);

			g2.fillPolygon(new int[]{x + nX, x + nY, x + nX, x - nX, x - nY, x - nX}, 
					new int[]{y - nY, y, y + nY, y + nY, y, y - nY}, 6);

		}
		
		//thickness icon
		g2.setColor(Color.BLACK);
		text = "Thickness: " + Integer.toString(brushThickness);
		g2.drawRect(767, 0, 80, 40);
		g2.drawString(text,770, 25);
		
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 41, getWidth(), 3);
		
		
	}

	public void setColour(Color paintColour) {
		mPaintColour = paintColour;
		//System.out.println(paintColour);
		setAlpha(paintColour);

		repaint();
	}

	public void setAlpha(Color alpha) {
		mAlpha = (alpha.getAlpha()*100/255);
		//System.out.println("Alpha Value: " + mAlpha);

		repaint();
	}


	/**
	 * @return the brushThickness
	 */
	public int getBrushThickness() {
		return brushThickness;
	}

	/**
	 * @param brushThickness the brushThickness to set
	 */
	public void setBrushThickness(int brushThickness) {
		this.brushThickness = brushThickness;

		repaint();
	}

	/**
	 * @return the brushType
	 */
	public int getBrushType() {
		return brushType;
	}

	/**
	 * @param brushType the brushType to set
	 */
	public void setBrushType(int brushType) {
		this.brushType = brushType;

		repaint();
	}


}
