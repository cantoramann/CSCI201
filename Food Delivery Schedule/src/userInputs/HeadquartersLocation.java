package userInputs;

import java.util.Scanner;

public class HeadquartersLocation {
	
	public static Scanner scanner;
	public static void getHeadquartersLocation() {
	
		scanner = new Scanner(System.in);
		System.out.println();

		Double latitude = null;
		while (true) {
			System.out.println("What is your latitude?");
			try {
				String latitudeString = scanner.nextLine();
				latitude = Double.parseDouble(latitudeString);
			}
			catch (NumberFormatException nfe) {
				System.out.println();
				System.out.print("Invalid latitude, please enter again. ");
				continue;
			}
			if (latitude < -90 || latitude > 90) {
				System.out.println();
				System.out.print("Latitude should be between the values -90 and 90. ");
				continue;
			}
			serverModels.Headquarters.setLatitude(latitude);
			break;
		}
		System.out.println();
		Double longitude = null;
		while (true) {
			System.out.println("What is your longitude?");
			try {
				String longitudeString = scanner.nextLine();
				longitude = Double.parseDouble(longitudeString);
			}
			catch (NumberFormatException nfe) {
				System.out.println();
				System.out.print("Invalid longitude, please enter again. ");
				continue;
			}
			if (longitude < -180 || longitude > 80) {
				System.out.println();
				System.out.print("Longitude should be between the values -180 and 80. ");
				continue;
			}
			serverModels.Headquarters.setLongitude(longitude);
			break;
		}
	}
}
