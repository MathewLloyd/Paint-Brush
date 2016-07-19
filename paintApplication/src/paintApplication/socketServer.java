package paintApplication;

import java.net.*;
import java.util.*;
import java.awt.Color;
import java.io.*;

class socketServer {
	static final int SOCKET_NUMBER = 8888;
	
	SettingsBar m_settingsPanel;
	PaintPanel m_paintPanel;
	Color m_previousColor;
	
	public socketServer(SettingsBar panSets, PaintPanel panPaint) {
		m_settingsPanel = panSets;
		m_paintPanel = panPaint;
		
		ServerSocket sSoc = null;
				
		IsServerList list = new BasicServerList();
		
		try {
			sSoc = new ServerSocket(SOCKET_NUMBER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				 Socket inSoc = sSoc.accept();

				 ServerThread newServerThread = new ServerThread(inSoc,list,this);
				 new Thread(newServerThread).start();
		    } catch (IOException e) {
		    	    System.out.println("IO Exception, continuing.");
		        e.printStackTrace();
		    }
	    }
	}
	
	public void callMethods(String inputString) {
		
		// Parse inputs String
		// Pass key into switch statement to choose method
		// Pass values from selected method in here
		String key = inputString.split(":")[0];
		String value = new String();
		if (inputString.split(":").length > 1) {
			value = inputString.split(":")[1];
		}
		
		switch (key) {
		case "ALPH":
		case "COL":
			int r = Integer.parseInt(value.split(",")[0]);
			int g = Integer.parseInt(value.split(",")[1]);
			int b = Integer.parseInt(value.split(",")[2]);
			int a = Integer.parseInt(value.split(",")[3]);
			a = (a > 255) ? 0 : 255 - a;
			System.out.println("A: " + a);
			Color c = new Color(r,g,b,a);
			m_settingsPanel.setColour(c);
			m_paintPanel.setmPaintColour(c);
			break;
		case "POS":
			m_paintPanel.setDirection(value);
			break;
		case "BRTY":
			int bt = Integer.parseInt(value);
			m_settingsPanel.setBrushType(bt);
			m_paintPanel.setBrushType(bt);
			break;
		case "CENT":
			m_paintPanel.setCentre();
			break;
		case "UNDO":
			m_paintPanel.undo();
			break;
		case "THICK":
			int thickness = Integer.parseInt(value);
			m_settingsPanel.setBrushThickness(thickness);
			m_paintPanel.setBrushThickness(thickness);
			break;
		}
		
	//	System.out.println("Key: " + key + ", Value: " + value);
	}
}

class BasicServerList implements IsServerList {
	
	private Collection<IsServerThread> threadList;
          	
	public BasicServerList() {
		threadList = new ArrayList<IsServerThread>();
	}

	public void add(IsServerThread item) {
	    threadList.add(item);
	}
	
	// Similarly for removal.
	public void remove(IsServerThread item) {
		synchronized(threadList) {
			threadList.remove(item);
		}
	}
	
    // Don't need these for versions 1 and 2
	public void incCounter() {}
	
	public void decCounter() {}
	
	//This is because it would be too much effort to make this class implement
	//Collection, return it's own Iterator etc. etc...\
	//Note it is *not* a bug that it isn't synchronized
	public Collection getCollection() {
		return threadList;
	}
}

interface IsServerThread extends Runnable {

	void writeMessage(String string);
}

interface IsServerList {

	public void add(IsServerThread item);
	
	public void remove(IsServerThread item);

	public void incCounter();
	
	public void decCounter();
	
	public Collection getCollection();
	
}

class ServerThread implements IsServerThread {

	protected IsServerList threadList;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String name;
	Socket inSocket;
	socketServer m_socketServer;
	
	public ServerThread(Socket inSoc, IsServerList list, socketServer socket) throws IOException {
		m_socketServer = socket;
		inSocket = inSoc;
		threadList = list;
		out = new ObjectOutputStream(inSoc.getOutputStream());
		out.flush();
		in = null;
		
		// Make sure we can only do this if no other thread is in a synchronized section
		synchronized (threadList) {
			threadList.add(this);
		}
	}
	
	//Write out to this thread's client
    public void writeMessage(String message) {
        if (message == null) {
        		System.out.println("Null message");
        } else {
        	try {
        	    out.writeObject(message);
        	}
        	catch (Exception e) {
        		System.out.println("Failure writing message");
        		e.printStackTrace();
        	}
        }		
    }
	
	public void run(){
		boolean notFinished = true;
		try {
			while (notFinished) {
				if (inSocket.getInputStream() != null) {
					if (in == null) {
						in = new ObjectInputStream(inSocket.getInputStream());
					} else {
						String message = in.readUTF();
						m_socketServer.callMethods(message);
					}
				}
				/*for (Iterator i = threadList.getCollection().iterator(); i.hasNext();) {
		            IsServerThread thread = (IsServerThread)i.next();
		            thread.writeMessage("Androids dream of electric sheep");
		        }*/
				/*if (out != null) {
					System.out.println("Writing...");
					out.writeUTF("Androids dream of electric sheep");
					System.out.println("Written...");
				}*/
			}
		}
		
		catch (IOException ie) {
				System.out.println("IO Exception");
				ie.printStackTrace();
		}
		
		finally {
			synchronized (threadList) {
				threadList.remove(this);
			}
		}
	}
}