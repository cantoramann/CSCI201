package serverModels;

import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CookedOrders {
	
	public static int hqSignaltoCookedOrders = 0;
	public static boolean initiallyEmpty = false;
	
	public static Lock modifyingCookedOrders = new ReentrantLock();
	public static Condition cookedOrdersUpdated = modifyingCookedOrders.newCondition();
	
	public static Vector<Order> getCookedOrders() {
		return cookedOrders;
	}
	private static Vector<Order> cookedOrders = new Vector<Order>();

}
