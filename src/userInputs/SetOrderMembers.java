package userInputs;


public class SetOrderMembers {

	public static void setMembers() {
		for (int i = 0; i < serverModels.FileOrders.getAllOrders().size(); i++) {
			for (int j = 0; j < serverModels.ProgramRestaurants.getProgramRestaurants().size(); j++) {
				if (serverModels.ProgramRestaurants.getProgramRestaurants().get(j).getName().
						equals(serverModels.FileOrders.getAllOrders().get(i).getRestaurantName())) {
					serverModels.FileOrders.getAllOrders().get(i).setLatitude(
							serverModels.ProgramRestaurants.getProgramRestaurants().get(j).getCoordinates().getLatitude());
					serverModels.FileOrders.getAllOrders().get(i).setLongitude(
							serverModels.ProgramRestaurants.getProgramRestaurants().get(j).getCoordinates().getLongitude());
				}
			}
		}
	}
}
