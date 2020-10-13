package serverModels;

import java.util.ArrayList;

import javax.swing.plaf.synth.Region;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantList {

	
	@SerializedName("businesses")
	@Expose
	private ArrayList<Restaurant> restaurants = null;
	@SerializedName("total")
	@Expose
	private Integer total;
	@SerializedName("region")
	@Expose
	private Region region;

	public ArrayList<Restaurant> getBusinesses() {
	return restaurants;
	}

	public void setBusinesses(ArrayList<Restaurant> businesses) {
	this.restaurants = businesses;
	}

	public Integer getTotal() {
	return total;
	}

	public void setTotal(Integer total) {
	this.total = total;
	}

	public Region getRegion() {
	return region;
	}

	public void setRegion(Region region) {
	this.region = region;
	}
}
