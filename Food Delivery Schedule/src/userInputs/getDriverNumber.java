package userInputs;

import java.util.Scanner;

public class getDriverNumber {

	public static Integer driversInteger;
	public static Scanner scanner;
	
	public static Integer getDrivers() {
		scanner = new Scanner(System.in);
		System.out.println();
		
		while (true) {
			System.out.println("How many drivers will be in service today?");
			
			try {
				String numberString = scanner.nextLine();
				driversInteger = Integer.parseInt(numberString);
				if (driversInteger <= 0) {
					System.out.println();
					System.out.print("Please enter a positive number.");
					continue;
				}
			}
			catch (NumberFormatException nfe) {
				System.out.println();
				System.out.print("Please enter a number. ");
				continue;
			}
			catch (Exception e) {
				System.out.println();
				System.out.print("Please enter a valid input. ");
				continue;
			}
			return driversInteger;
		}
	}
}
