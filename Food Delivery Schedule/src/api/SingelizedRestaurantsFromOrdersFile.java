package api;

import java.util.Vector;

import serverModels.FileOrders;

public class SingelizedRestaurantsFromOrdersFile {
	
	public static Vector<String> singelizedRestaurants = new Vector<String>();
	
	public static void singelizeRestaurants() {
		singelizedRestaurants.clear();
		for (int i = 0; i < FileOrders.getAllOrders().size(); i++) {
			if (i == 0) {
				singelizedRestaurants.add(FileOrders.getAllOrders().get(i).getRestaurantName());
			}
			else {
				boolean found = false;
				for (int j = 0; j < singelizedRestaurants.size(); j++) {
					if (singelizedRestaurants.get(j).contentEquals(FileOrders.getAllOrders().get(i).getRestaurantName()))
						found = true;
				}
				if (!found) {
					singelizedRestaurants.add(FileOrders.getAllOrders().get(i).getRestaurantName());
				}
			}
		}
	}

}
