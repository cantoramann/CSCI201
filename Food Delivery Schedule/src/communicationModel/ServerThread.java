package communicationModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import serverModels.Headquarters;

public class ServerThread extends Thread {


	public Vector<NetworkOrderMessage> currentList = new Vector<NetworkOrderMessage>();
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private int id;
	public boolean onAction;
	public ReentrantLock lock = new ReentrantLock();
	public Integer situation = 1;
	public boolean initialSignal;
	public int remaining;
	public boolean sendingWaitSignals;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void broadcastStarting() {
		try {
			objectOut.writeObject("Broadcast Start");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerThread(Socket socket, int id) {

		try {
			this.sendingWaitSignals = false;
			this.initialSignal = false;
			this.onAction = false;
			this.id = id;
			this.objectOut = new ObjectOutputStream(socket.getOutputStream());
			this.objectIn = new ObjectInputStream(socket.getInputStream());
			this.start();
		}
		catch (IOException ioe) {
			System.out.println("ioe in serverThread constructor: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}
	public synchronized void run() {		
		
		while (true) {
			while (!sendingWaitSignals) {
				Thread.yield();
			}
			try {
				objectOut.writeObject(remaining);
				objectOut.flush();
				sendingWaitSignals = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (remaining == 0) break;
		}
		broadcastStarting();
		while (true) {
			while (!onAction) {
				yield();
			}
			try {
				objectOut.writeObject(this.situation);
				objectOut.flush();
				if (situation == 1) {
//					System.out.println("Server thread list size before delivery is : " + currentList.size());
					Vector<NetworkOrderMessage> tempMessages = new Vector<NetworkOrderMessage>();
					for (int i = 0; i < currentList.size(); i++) {
						tempMessages.add(currentList.get(i));
					}
					objectOut.writeObject(tempMessages);
					objectOut.flush();
					objectOut.writeObject(Headquarters.getLatitude());
					objectOut.flush();
					objectOut.writeObject(Headquarters.getLongitude());
					objectOut.flush();
					
					String message = (String) objectIn.readObject();
					if (!message.equals("complete"))
						System.err.println("-- There is a problem: a driver thread could not complete deliveries.");
					
					this.onAction = false;
					currentList.clear();
					Headquarters.getDriversInHQVector().add(this);
					Headquarters.driversRemaining++;
				}
				else if (situation == 0) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException cnf) {
				cnf.printStackTrace();
			}
		}
	}
}
