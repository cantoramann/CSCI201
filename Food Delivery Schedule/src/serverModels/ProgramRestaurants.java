package serverModels;

import java.util.Vector;

public class ProgramRestaurants {

	public static Vector<Restaurant> getProgramRestaurants() {
		return programRestaurants;
	}
	public static void setProgramRestaurants(Vector<Restaurant> programRestaurants) {
		ProgramRestaurants.programRestaurants = programRestaurants;
	}
	private static Vector<Restaurant> programRestaurants = new Vector<Restaurant>();
}
