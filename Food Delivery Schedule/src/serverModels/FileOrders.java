package serverModels;

import java.util.Vector;

public class FileOrders {
	
	public static Vector<Order> getAllOrders() {
		return allOrders;
	}

	private static Vector<Order> allOrders = new Vector<Order>();
}
