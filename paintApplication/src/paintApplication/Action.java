package paintApplication;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Action {
	private int m_x;
	private int m_y;
	private int m_xDist;
	private int m_yDist;
	private int m_xPrev;
	private int m_yPrev;
	private int m_brushType;
	private String m_type;
	private Color m_color;
	private int m_brushThick;
	private BufferedImage m_image;

	Action(String actionType) {
		m_type = actionType;
	}
	
	void setLine(int x, int y, int xDist, int yDist, int brushType, Color col, int brushThickness) {
		m_x = x;
		m_y = y;
		m_xDist = xDist;
		m_yDist = yDist;
		m_brushType = brushType;
		m_color = col;
		m_brushThick = brushThickness;
	}
	
	void setCentre(int xPrev, int yPrev) {
		m_xPrev = xPrev;
		m_yPrev = yPrev;
	}
	
	String getType() {
		return m_type;
	}
	
	int getLineX() {
		return m_x;
	}
	
	int getLineY() {
		return m_y;
	}
	
	int getLineXDist() {
		return m_xDist;
	}
	
	int getLineYDist() {
		return m_yDist;
	}
	
	int getBrushType() {
		return m_brushType;
	}
	
	public Color getColor() {
		return m_color;
	}
	
	public int getThickness() {
		return m_brushThick;
	}
	int getPrevX() {
		return m_xPrev;
	}
	
	int getPrevY() {
		return m_yPrev;
	}

	public void setImage(BufferedImage resizedImage) {
		m_image = resizedImage;
		
	}
	
	public BufferedImage getImage(){
		return m_image;
	}
}
