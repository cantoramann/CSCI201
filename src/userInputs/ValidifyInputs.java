package userInputs;

import serverModels.FileOrders;
import serverModels.ProgramRestaurants;

public class ValidifyInputs {

	public static boolean validifyAllNamesMatch() {
		
		boolean corona = false;
		
		for (int i = 0; i < FileOrders.getAllOrders().size(); i++) {
			boolean found = false;
			for (int j = 0; j < ProgramRestaurants.getProgramRestaurants().size(); j++) {
				if (FileOrders.getAllOrders().get(i).getRestaurantName().contentEquals
						(ProgramRestaurants.getProgramRestaurants().get(j).getName())) {
					found = true; 
				}
			}
			if (!found) {
				corona = true;
				break;
			}
		}
		return corona;
	}
}
