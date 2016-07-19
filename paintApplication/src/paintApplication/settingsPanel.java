package paintApplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class settingsPanel extends JPanel{
	private Color brushColour;
	private BufferedImage img, imgAlpha,imgType,imgThick;
	private Graphics2D g;
	private JLabel icon;
	private int alpha;
	private int brushType = 1;
	private int brushThickness = 5;
	
	
	
	//Look up JToolBar
	public settingsPanel(){
		super();
		//JPanel panel = new JPanel();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();     
		setSize(env.getMaximumWindowBounds().width, 35);
		
		//Paint palette colour
		img = new BufferedImage( 25, 25, BufferedImage.TYPE_INT_RGB );
		//Image for transparency indicator
		imgAlpha = new BufferedImage( 25, 25, BufferedImage.TYPE_INT_ARGB );
		//Image for brush type
		imgType = new BufferedImage( 30, 30, BufferedImage.TYPE_INT_RGB );
		//Image for brush thickness
		imgThick = new BufferedImage( 25, 25, BufferedImage.TYPE_INT_RGB );
		icon = new JLabel();
		//add(icon);
		
				
		setVisible(true);
		paint();
		
	}

	public void setColour(Color paintColour) {
		this.brushColour = paintColour;
		System.out.println(paintColour);
		setAlpha(paintColour);
		paint();
	}
	
	public void setAlpha(Color alpha) {
		this.alpha = (alpha.getAlpha()/255*100);
		System.out.println("Alpha Value: " + this.alpha);
		paint();
	}
	
	public void paint(){
		this.removeAll();
		FlowLayout flowy = new FlowLayout();
		setLayout(flowy);
		Font font = new Font("Arial", Font.ITALIC, 15);
		// Create Icon to represent the current colour
		g = (Graphics2D) img.createGraphics();
		g.setColor(brushColour);
		System.out.println("Out brush" + brushColour);
		g.fillRect( 0, 0, 25, 25 );
	
		icon = new JLabel("Colour: ");
		icon.setFont(font);
		add(icon);
		icon = new JLabel(new ImageIcon(img) );
		icon.repaint();
		add(icon, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(40));
		
        JLabel icon2 = new JLabel("<html><center>Opacity<br>" + alpha + "%</center></html>");
        icon2.setFont(font);
        icon2.setForeground(Color.BLACK);
		add(icon2);	
		add(Box.createHorizontalStrut(40));
		//g = (Graphics2D) imgType.createGraphics();
		System.out.println("Type: " + brushType);
		getBrushDraw(brushType);
		icon = new JLabel("<html><center>Brush<br> Type: </center></html>");
		icon.setFont(font);
		add(icon);
		JLabel icon3 = new JLabel(new ImageIcon(imgType) );
		add(icon3);
		add(Box.createHorizontalStrut(40));
		JLabel icon4 = new JLabel("<html><center>Thickness " + brushThickness + "</center></html>");
        icon4.setFont(font);
        icon4.setForeground(Color.BLACK);
		g.dispose();
		add(icon4);	
		add(Box.createHorizontalStrut(40));
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
		paint();
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
		paint();
	}
	
	public Graphics2D getBrushDraw(int brushType){
		
		int thickness = 5;
		int x = 15;
		int y = 15;
		int diff = 10;
		g = (Graphics2D) imgType.createGraphics();
		g.fillRect( 0, 0, 35, 35 );
		
		if (brushType == 1) {
			Stroke s = new BasicStroke(thickness*3, // Width
					BasicStroke.CAP_SQUARE, // End cap
					BasicStroke.JOIN_MITER, // Join style
					1.0f, // Miter limit
					new float[] { 10.0f, 10.0f }, // Dash pattern
					0.0f);
			g.setStroke(s);// Dash phase
			g.setColor(brushColour);
			g.drawLine(x, y, diff, diff);
			
		} else if (brushType == 2) {

			g.setColor(brushColour);
			
				g.drawOval(x, y, thickness*5, thickness*5);
				
		} else if (brushType == 3) {
			//System.out.println("BRUSH TYPE == 2:: X: " + x + " Y: " + y);
			g.setColor(brushColour);
			
				g.fillOval(x, y, thickness*5, thickness*5);
				
		} else if (brushType == 4) {
			g.setColor(brushColour);
			
				g.drawPolygon(new int[]{(int)(x-(2.5*thickness)), x, (int)(x+(2.5*thickness))}, new int[]{(int)(y+(2.5*thickness)), (int)(y-(2.5*thickness)), (int)(y+(2.5*thickness))}, 3);
				
		} else if (brushType == 5) {
			g.setColor(brushColour);
			
				g.fillPolygon(new int[]{(int)(x-(2.5*thickness)), x, (int)(x+(2.5*thickness))}, new int[]{(int)(y+(2.5*thickness)), (int)(y-(2.5*thickness)), (int)(y+(2.5*thickness))}, 3);
			
		}else if (brushType == 6) {
			g.setColor(brushColour);
			
				double c1 = Math.cos((2*Math.PI/5)) * (2.5*thickness);
				double c2 =  Math.cos((Math.PI/5)) * (2.5*thickness);
				double s1 =  Math.sin((2*Math.PI)/5) * (2.5*thickness);
				double s2 =  Math.sin((4*Math.PI)/5) * (2.5*thickness);
				System.out.println(c1 + " " + c2+ " " + s1+ " " + s2+ " ");
				g.drawPolygon(new int[]{(int)(x),(int)(x+s1), (int)(x + s2),(int)( x - s2), (int)(x - s1 )}, 
								new int[]{y+(int)(2.5*thickness),(int)(y+c1), (int)(y - c2), (int)(y - c2), (int)(y + c1)}, 5);
				//g2.drawLine(x, y, act.getLineXDist(), diff);
		
		}else if (brushType == 7) {
			g.setColor(brushColour);
			
				double c1 = Math.cos((2*Math.PI/5)) * (int)(2.5*thickness);
				double c2 =  Math.cos((Math.PI/5)) * (int)(2.5*thickness);
				double s1 =  Math.sin((2*Math.PI)/5) * (int)(2.5*thickness);
				double s2 =  Math.sin((4*Math.PI)/5) * (int)(2.5*thickness);
				System.out.println(c1 + " " + c2+ " " + s1+ " " + s2+ " ");
				g.fillPolygon(new int[]{(int)(x),(int)(x+s1), (int)(x + s2),(int)( x - s2), (int)(x - s1 )}, 
								new int[]{y+(int)(2.5*thickness),(int)(y+c1), (int)(y - c2), (int)(y - c2), (int)(y + c1)}, 5);
			
		}else if (brushType == 8) {
			g.setColor(brushColour);
			
				int nX = (int)(Math.cos(108) * 2.5*thickness);
				int nY = (int)(2.5*thickness);
				
				g.fillPolygon(new int[]{x + nX, x + nY, x + nX, x - nX, x - nY, x - nX}, 
								new int[]{y - nY, y, y + nY, y + nY, y, y - nY}, 6);
				//g2.drawLine(x, y, act.getLineXDist(), diff);
			
		}else if (brushType == 9) {
			g.setColor(brushColour);
			
				int nX = (int)(Math.cos(30) * 2.5*thickness);
				int nY = (int)(2.5*thickness);
				
				g.fillPolygon(new int[]{x + nX, x + nY, x + nX, x - nX, x - nY, x - nX}, 
								new int[]{y - nY, y, y + nY, y + nY, y, y - nY}, 6);
			
		}else if (brushType == 10) {
			g.setColor(brushColour);
			
				int nX = (int)(Math.cos(72) * 2.5*thickness);
				int nY = (int)(2.5*thickness);
				
				g.fillPolygon(new int[]{x + nX, x + nY, x + nX, x - nX, x - nY, x - nX}, 
								new int[]{y - nY, y, y + nY, y + nY, y, y - nY}, 6);
			
	}
		return g;
	}
	
}
