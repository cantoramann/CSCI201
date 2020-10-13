package serverModels;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import javax.rmi.CORBA.Util;

import api.PrepareAPILinks;
import api.SingelizedRestaurantsFromOrdersFile;
import communicationModel.NetworkOrderMessage;
import communicationModel.ServerThread;
import util.GetCurrentTime;


public class Headquarters {
	
	public static Integer ordersSent = new Integer(0);
	public static Integer driversRemaining = new Integer(0);
	
 	public static ReentrantLock driverVectorLock = new ReentrantLock();
	private static Vector<ServerThread> driverIndexedVector = new Vector<ServerThread>();
	public static Vector<ServerThread> getDriverIndexedVector() {
		return driverIndexedVector;
	}
	
	private static ReentrantLock driversInHQLock = new ReentrantLock();
	private static Vector<ServerThread> driversInHQVector = new Vector<ServerThread>();
	public static Vector<ServerThread> getDriversInHQVector() {
		return driversInHQVector;
	}

	public static Vector<Integer> currentFood = new Vector<Integer>();
	
	private static int driverNumber;
	private static Double latitude;
	private static Double longitude;

	public static Double getLatitude() {
		return Headquarters.latitude;
	}

	public static void setLatitude(Double latitude) {
		Headquarters.latitude = latitude;
	}

	public static Double getLongitude() {
		return Headquarters.longitude;
	}

	public static void setLongitude(Double longitude) {
		Headquarters.longitude = longitude;
	}

	public static void setServer(int driverNumber) {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(3456);
			System.out.println();
			System.out.println("Listening on port 3456. Waiting for drivers...");
			
			for (int i = 0; i < driverNumber; i++) {
				
				Socket socket = serverSocket.accept(); // blocking line of code

				int remaining = driverNumber - i - 1;
				if (remaining != 0) {
					System.out.println();
					System.out.println("Connection from " + socket.getInetAddress() + ". Waiting for " + remaining
							+ " more driver(s)...");		

				}
				else {
					System.out.println();
					System.out.println("Connection from " + socket.getInetAddress() + ". Starting service.");

				}
				ServerThread st = new ServerThread(socket, i);
				
				//initially set the vector with drivers / serverThreads
				driverVectorLock.lock();
				driverIndexedVector.add(st);
				
				driverVectorLock.unlock();
				
				driversInHQLock.lock();
				driversInHQVector.add(st);
				if (remaining != 0) {
					for (int j = 0; j < driversInHQVector.size(); j++) {
						driversInHQVector.get(i).remaining = remaining;
						driversInHQVector.get(i).sendingWaitSignals = true;
					}
				}
				driversInHQLock.lock();
			}
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	public static void main(String[] args) {

		getAndVerifyFile(); // probably done TODO: test more

		driverNumber = userInputs.getDriverNumber.getDrivers();
		setServer(driverNumber);
		for (int i = 0; i < driversInHQVector.size(); i++) {
			driversInHQVector.get(i).remaining = 0;
			driversInHQVector.get(i).sendingWaitSignals = true;
		}
		
		while (getDriverIndexedVector().size() != driverNumber) {
			Thread.yield();
		}
		
		//let's gooooo!!
		for (int i = 0; i < driverNumber; i++) {
			driverIndexedVector.get(i).initialSignal = true;
		}

		long startTime = System.currentTimeMillis();
		driversRemaining = driverNumber;	
		int totalOrderNumber = FileOrders.getAllOrders().size();
		int cookedOrders = 0;
			
		while (ordersSent != totalOrderNumber) {	
			while (driversInHQVector.isEmpty()) {
				Thread.yield();
			} 
			while (CookedOrders.getCookedOrders().isEmpty()) {		
				long currentTime = System.currentTimeMillis();
					
				for (int j = cookedOrders; j < FileOrders.getAllOrders().size(); j++) {
					Order iteratedOrder = FileOrders.getAllOrders().get(j);
					
					if (((currentTime - startTime)) >= (long) iteratedOrder.getCookTime() * 1000) {
						CookedOrders.getCookedOrders().add(iteratedOrder);
						cookedOrders++;
					}
				}
			}
			
			CookedOrders.modifyingCookedOrders.lock();			
			driversInHQLock.lock();
			
			//create new serverOrders
			int size = CookedOrders.getCookedOrders().size();
			for (int i = 0; i < size; i++) {
				driversInHQVector.get(0).currentList.add(new NetworkOrderMessage(CookedOrders.getCookedOrders().get(i).getRestaurantName(), 
						CookedOrders.getCookedOrders().get(i).getOrderName(), CookedOrders.getCookedOrders().get(i).getLatitude(), 
						CookedOrders.getCookedOrders().get(i).getLongitude(), latitude, longitude));
			}
//			System.out.println("Current list size is " + driversInHQVector.get(0).currentList.size() + ". Indexed:");
//			for (int i = 0; i < size; i++) {
//				System.out.println("Current list size is " + driversInHQVector.get(0).currentList.get(i).getOrderName());
//
//			}
			CookedOrders.getCookedOrders().clear();
			driversInHQVector.get(0).onAction = true;
			ordersSent += size;
			driversRemaining--;
			driversInHQVector.remove(0);
			CookedOrders.getCookedOrders().clear();
			driversInHQLock.unlock();
			CookedOrders.modifyingCookedOrders.unlock();
			
		}
		while (driversRemaining != driverNumber) {
			Thread.yield();
		}
		for (int i = 0; i < driverNumber; i++) {
			driverIndexedVector.get(i).situation = 0;
			driverIndexedVector.get(i).onAction = true;
		}
		System.out.println();
		System.out.println("All orders completed!");
	}
	

	public static void getAndVerifyFile() {
		while (true) {
			userInputs.FileName.getFile(); //done
			userInputs.HeadquartersLocation.getHeadquartersLocation(); //done
			//TODO : check what happens if the txt file is empty. DONE
			api.SingelizedRestaurantsFromOrdersFile.singelizeRestaurants(); //done
			api.PrepareAPILinks.prepareLinks(); //done
			api.PrepareAPILinks.parseAPI(); //done
			boolean coronaVirus = api.PrepareAPILinks.extractDesiredRestaurants(); //done
			if (coronaVirus) {
				ProgramRestaurants.getProgramRestaurants().clear();
				FileOrders.getAllOrders().clear();
				PrepareAPILinks.jsons.clear();
				PrepareAPILinks.urls.clear();
				SingelizedRestaurantsFromOrdersFile.singelizedRestaurants.clear();
				continue;
			}
			userInputs.SetOrderMembers.setMembers();
			break;
		}
	
	}
}
