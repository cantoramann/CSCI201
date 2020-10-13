package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import com.google.gson.Gson;

import serverModels.Restaurant;
import serverModels.RestaurantList;


public class PrepareAPILinks {
	
	public static Vector<String> urls = new Vector<String>();
	public static Vector<String> jsons = new Vector<String>();

	public static void prepareLinks() {
		
		for (int index = 0; index < SingelizedRestaurantsFromOrdersFile.singelizedRestaurants.size(); index++) {
			String urlUnmodified = "https://api.yelp.com/v3/businesses/search?term=" + 
					SingelizedRestaurantsFromOrdersFile.singelizedRestaurants.get(index) + 
								"&latitude=" + Double.toString(serverModels.Headquarters.getLatitude()) 
								+ "&longitude=" + Double.toString(serverModels.Headquarters.getLongitude());

			urlUnmodified = urlUnmodified.replaceAll("\\s+", "-");
			String url = urlUnmodified.replaceAll("’", "'");

			urls.add(url);
		}
	}
	
	public static void parseAPI() {
		for (int i = 0; i < urls.size(); i++) {
			try {
				URL url = new URL(urls.get(i));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Authorization",
						"Bearer JGXy9G-nbMp8B5WnonEpd6jvA5ESicd-eL47ujEf9T1PvjV2G-Ls4p_88ou0F7v0XDmpo46ndG-Oa5qjk_f1hTJOJHrC7HOhFjfFmYghm1ZjiQA32AW0UBeJgMtaXnYx");
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String json = bufferedReader.readLine();
				jsons.add(json); //successfull
			} catch (MalformedURLException e) {
				System.out.println();
				System.out.println("Data could not be found from Yelp - URL is malformed. Restaurant name strings in the API are not supported in YELP.");
				System.out.println("Terminating the program");
				System.exit(7);
			} catch (IOException ioe) {
				System.out.println();
				System.out.println("Data could not be extracted Yelp - IO Exception. Some restaurants from the schedule file could not be found.");
				System.out.println("Terminating the prgram");
				System.exit(7);
			}
		}
	}

	public static boolean extractDesiredRestaurants() {
		
		for (int i = 0; i < jsons.size(); i++) {
			Gson gson = new Gson();
			RestaurantList rl = gson.fromJson(jsons.get(i), RestaurantList.class);
			
			
			ArrayList<Restaurant> onlyDesiredNamedRestaurants = new ArrayList<Restaurant>();
			for (int j = 0; j < rl.getBusinesses().size(); j++) {
				if (rl.getBusinesses().get(j).getName().contentEquals(SingelizedRestaurantsFromOrdersFile.singelizedRestaurants.get(i))) {
					onlyDesiredNamedRestaurants.add(rl.getBusinesses().get(j));
				}
			}
			for (int j = 0; j < onlyDesiredNamedRestaurants.size(); j++) {
				onlyDesiredNamedRestaurants.get(j).initializeRelativeDistance();
			}
			
			if (onlyDesiredNamedRestaurants.size() == 0) {
				System.out.println();
				System.out.print("Not all restaurants could be found from Yelp. ");
				return true;
			}
			onlyDesiredNamedRestaurants.sort(new comparators.RestaurantComparatorbyDistance());

			serverModels.ProgramRestaurants.getProgramRestaurants().add(onlyDesiredNamedRestaurants.get(0));
		}
		return false;
	}
}
