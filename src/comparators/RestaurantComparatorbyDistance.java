package comparators;

import java.util.Comparator;

public class RestaurantComparatorbyDistance implements Comparator<serverModels.Restaurant> {

	public int compare(serverModels.Restaurant first, serverModels.Restaurant second) {

		if (first.getRelativeDistance() < second.getRelativeDistance())
			return -1;
		else if (first.getRelativeDistance() > second.getRelativeDistance())
			return 1;
		return 0;
	}
}
