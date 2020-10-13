package communicationModel;

import java.io.Serializable;

public class NetworkOrderMessage implements Serializable {

	public NetworkOrderMessage(String restaurantName2, String orderName2, Double latitude2, Double longitude2
			, Double hqlat, Double hqlong) {
		this.restaurantName = restaurantName2;
		this.orderName = orderName2;
		this.latitude = latitude2;
		this.longitude = longitude2;
		this.hqLatitude = hqlat;
		this.hqLongitude = hqlong;
	}
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getRelativeDistance() {
		return relativeDistance;
	}
	public void setRelativeDistance(Double relativeDistance) {
		this.relativeDistance = relativeDistance;
	}
	public Double getHqLatitude() {
		return hqLatitude;
	}
	public void setHqLatitude(Double hqLatitude) {
		this.hqLatitude = hqLatitude;
	}
	public Double getHqLongitude() {
		return hqLongitude;
	}
	public void setHqLongitude(Double hqLongitude) {
		this.hqLongitude = hqLongitude;
	}

	private Double hqLatitude;
	private Double hqLongitude;
	private String restaurantName;
	private String orderName;
	private Double latitude;
	private Double longitude;
	private Double relativeDistance;
}
