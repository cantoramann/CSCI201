package serverModels;

public class Order {
	
	
	public String getRestaurantName() {
		return this.restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getOrderName() {
		return this.orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public Integer getCookTime() {
		return cookTime;
	}
	public void setCookTime(Integer cookTime) {
		this.cookTime = cookTime;
	}
	public Restaurant getParentRestaurant() {
		return parentRestaurant;
	}
	public void setParentRestaurant(Restaurant parentRestaurant) {
		this.parentRestaurant = parentRestaurant;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public boolean taken = false;
	
	private String restaurantName;
	private String orderName;
	private Integer cookTime;
	private Restaurant parentRestaurant;
	private Double longitude;
	private Double latitude;

	public Order(String restaurantName, String orderName, int cookTime) {
		this.restaurantName = restaurantName;
		this.orderName = orderName;
		this.cookTime = cookTime;
	}
}
