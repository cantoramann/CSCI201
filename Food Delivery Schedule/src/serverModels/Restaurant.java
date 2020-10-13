package serverModels;

import java.util.ArrayList;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Restaurant {

	private Double relativeDistance;
	public Double getRelativeDistance() {
		return relativeDistance;
	}
	public void initializeRelativeDistance() {
		this.relativeDistance = util.DistanceCalculator.distanceCalculator(Headquarters.getLatitude(), Headquarters.getLongitude(),
				this.getCoordinates().getLatitude(), this.getCoordinates().getLongitude());
	}

	@SerializedName("name")
	@Expose
	private String restaurantName;

	@SerializedName("coordinates")
	@Expose
	private api.Coordinates coordinates;
	@SerializedName("transactions")
	@Expose
	private ArrayList<String> transactions = null;

	public String getName() {
		return restaurantName;
	}
	public void setName(String name) {
		this.restaurantName = name;
	}

	public api.Coordinates getCoordinates() {
		return coordinates;
	}
}