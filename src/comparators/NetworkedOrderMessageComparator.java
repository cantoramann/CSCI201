package comparators;

import java.util.Comparator;

public class NetworkedOrderMessageComparator implements Comparator<communicationModel.NetworkOrderMessage> {

	public int compare(communicationModel.NetworkOrderMessage first, communicationModel.NetworkOrderMessage second) {
		
		if (first.getRelativeDistance() < second.getRelativeDistance()) {
			return -1;
		}
		else if (first.getRelativeDistance() > second.getRelativeDistance()) {
			return 1;
		}
		return 0;
	}
}
