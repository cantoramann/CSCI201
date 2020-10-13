package clientModels;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

import javax.rmi.CORBA.Util;

import communicationModel.NetworkOrderMessage;
import comparators.NetworkedOrderMessageComparator;
import util.DistanceCalculator;

public class Driver extends Thread {

	public static Scanner scanner;

	Vector<NetworkOrderMessage> deliveryList = new Vector<NetworkOrderMessage>();
	private Double currentLatitude;
	private Double currentLongitude;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private Double hqLatitude;
	private Double hqLongitude;

	public Driver(String hostname, int port) {
		try {
			Socket socket = new Socket(hostname, port);
			objectIn = new ObjectInputStream(socket.getInputStream());
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in driver constructor: " + ioe.getMessage());
		}
	}

	public void readStartingBroadcast() {
		try {
			String startingBroadcastMessage = (String) objectIn.readObject();
			if (startingBroadcastMessage.equals("Broadcast Start")) {
				System.out.println();
				System.out.println("All drivers have arrived!");
				System.out.println("Starting service.");
			}
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
	}
	public synchronized void run() {
	
		while (true) {
			try {
				Integer waitingSignalInteger = (Integer) objectIn.readObject(); //blocking
				if (waitingSignalInteger == 0) break;
				else {
					if (waitingSignalInteger == 1) {
						System.out.println();
						System.out.println(waitingSignalInteger + " more driver is needed before the service can begin. Waiting...");
					}
					else {
						System.out.println();
						System.out.println(waitingSignalInteger + " more drivers are needed before the service can begin. Waiting...");
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		readStartingBroadcast();
		while (true) {
			try {
				Integer situationReceived = (Integer)objectIn.readObject();
				if (situationReceived == 1) {
					deliveryList = (Vector<NetworkOrderMessage>)objectIn.readObject();
					this.hqLatitude = (Double) objectIn.readObject();
					this.hqLongitude = (Double) objectIn.readObject();
					initializeRelativeDistancesAndStart();				
					sortDeliveryList();
					runDeliveries();
					objectOut.writeObject("complete");
					objectOut.flush();
				}
				else if (situationReceived == 0) {
					System.out.println();
					System.out.println(util.GetCurrentTime.getCurrentTime() + " All orders completed!");
					break;
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void initializeRelativeDistancesAndStart() {
		
		System.out.println();
		for (int i = 0; i < this.deliveryList.size(); i++) {
		System.out.println(util.GetCurrentTime.getCurrentTime() + " Starting delivery of " + this.deliveryList.get(i).getOrderName() + " to "
				+ this.deliveryList.get(i).getRestaurantName());
		this.deliveryList.get(i)
				.setRelativeDistance(util.DistanceCalculator.distanceCalculator(
						this.deliveryList.get(i).getHqLatitude(), this.deliveryList.get(i).getHqLongitude(),
						this.deliveryList.get(i).getLatitude(), this.deliveryList.get(i).getLongitude()));
		}
	}
	public void modifyRelativeDistances() {
		for (int i = 0; i < this.deliveryList.size(); i++) {
			this.deliveryList.get(i)
			.setRelativeDistance(util.DistanceCalculator.distanceCalculator(
					this.currentLatitude, this.currentLongitude,
					this.deliveryList.get(i).getLatitude(), this.deliveryList.get(i).getLongitude()));
		}
	}
	public void sortDeliveryList() {
		this.deliveryList.sort(new comparators.NetworkedOrderMessageComparator());
	}
	public void runDeliveries() {
		
		int initialSize = this.deliveryList.size();
		System.out.println();
		
		try {
			while (!deliveryList.isEmpty()) {
				Thread.sleep((long) (1000*deliveryList.get(0).getRelativeDistance()));
				System.out.println(util.GetCurrentTime.getCurrentTime() + " Finished delivery of " + deliveryList.get(0).getOrderName() + " to " + 
						deliveryList.get(0).getRestaurantName() + ".");
				
				currentLatitude = deliveryList.get(0).getLatitude();
				currentLongitude = deliveryList.get(0).getLongitude();
				deliveryList.remove(0); //good so far
				
				while (!deliveryList.isEmpty() && deliveryList.get(0).getLatitude() == currentLatitude 
						&& deliveryList.get(0).getLongitude() == currentLongitude) {
					System.out.println(util.GetCurrentTime.getCurrentTime() + " Finished delivery of " + deliveryList.get(0).getOrderName() + 
							" to " + deliveryList.get(0).getRestaurantName() + ".");
					deliveryList.remove(0);
				}
				
				if (!deliveryList.isEmpty()) {
					modifyRelativeDistances();
					sortDeliveryList();
					System.out.println(util.GetCurrentTime.getCurrentTime() + " Continuing delivery to " + deliveryList.get(0).getRestaurantName() + ".");
					System.out.println();
				}				
			}
			System.out.println(util.GetCurrentTime.getCurrentTime() + " Finished all deliveries, returning back to HQ.");
			sleep((long) (1000*util.DistanceCalculator.distanceCalculator(hqLatitude, hqLongitude, currentLatitude, currentLongitude)));
			System.out.println(util.GetCurrentTime.getCurrentTime() + " Returned to HQ.");
		}
		
		catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		String hostname = getHostName();
		int port = getPort();
		new Driver(hostname, port);
	}

	public static String getHostName() {
		scanner = new Scanner(System.in);
		System.out.print("Welcome to SalEats v2.0! Enter the server hostname: ");
		String hostname = scanner.nextLine();
		hostname = hostname.trim();
		return hostname;
	}

	public static int getPort() {
		while (true) {
			System.out.print("Enter the server port: ");
			String port = scanner.nextLine();
			try {
				Integer portInteger = Integer.parseInt(port);
				return portInteger;
			} catch (NumberFormatException nfe) {
				System.out.println();
				System.out.print("Please enter an integer. ");
			}
		}
	}
}
