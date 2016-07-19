package paintApplication;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;

public class paintApplication extends JFrame implements ActionListener{
	private static SettingsBar settingsPanel;
	private static PaintPanel paintPanel;
	private static Color mPaintColour = new Color(0,0,0,255);
	private int mBrushSize;


	public static Color getmPaintColour() {
		return mPaintColour;
	}

	public void setmPaintColour(Color mPaintColour) {
		this.mPaintColour = mPaintColour;
	}

	public int getmBrushSize() {
		return mBrushSize;
	}

	public void setmBrushSize(int mBrushSize) {
		this.mBrushSize = mBrushSize;
	}

	public paintApplication() // set up graphics window
	{
		super();
		paintPanel = new PaintPanel();
		settingsPanel = new SettingsBar();
		//setBackground(Color.WHITE);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();     
		setBounds(env.getMaximumWindowBounds());

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		// set frame to exit when it is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setTitle("Paint Application");

		// Create the menu bar.
		menuBar = new JMenuBar();
		// Build the first menu.
		menu = new JMenu("File");
		menu.getAccessibleContext().setAccessibleDescription("Main Menu");
		menu.setMnemonic(KeyEvent.VK_F);
		menuItem = new JMenuItem("New Drawing",KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Create a new drawing canvas");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearImage();
			}
		});
		menu.add(menuItem);

		menu.addSeparator();
		menuItem = new JMenuItem("Open Drawing",KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Open an Existing Drawing");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				openDiag();
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Save Drawing",KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save Drawing");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				saveDiag();
			}
		});
		menu.add(menuItem);

		menu.addSeparator();
		menuItem = new JMenuItem("Quit",KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Quit the application");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				System.exit(0); //Quit has been clicked, exit application
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);


		setJMenuBar(menuBar);


		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		setVisible(true);
		JPanel container = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//container.setLayout(new GridBagLayout(container,BoxLayout.Y_AXIS));



		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 0;
		
		container.add(settingsPanel,c);


		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0.9;
		c.gridwidth = 10;
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.SOUTHWEST;

		
		container.add(paintPanel,c);

		add(container);
		settingsPanel.setColour(getmPaintColour());
		paintPanel.setmPaintColour(mPaintColour);

		validate();
		socketServer server = new socketServer(settingsPanel,paintPanel);

	}

	public static void main(String[] args) {


		paintApplication panel = new paintApplication(); // window for drawing
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void saveDiag(){
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				ImageIO.write(paintPanel.getImage(), "png", file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void openDiag(){
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				System.out.println("paintApplication:: OpenDialog()");
				paintPanel.loadImage(ImageIO.read(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void clearImage(){
		paintPanel.clearImage();
	}
//Implemented android functions:
		/**
		 * Shaking: Undo --> Saving everything to a stack
		 * Proximity Sensor: Brush Type --> 0-5
		 * Light Sensor: ANYTHING
		 * Change of Colour: RGB Value
		 * Draw Position: (x,y)
		 * Center: Crosshairs flashes up 
		 * 
		 */
	}
