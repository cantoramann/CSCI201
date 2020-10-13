package userInputs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import serverModels.FileOrders;
import serverModels.Order;

public class FileName {

	public static Scanner scanner = null;
	
	public static void getFile() {
		
		scanner = new Scanner(System.in);
		
		while (true) {
			System.out.println("What is the name of the schedule file?");
			
			Scanner scheduleScanner = null;
			try {
				String filenameString = scanner.nextLine();
				FileReader fileReader = new FileReader(filenameString);
				scheduleScanner = new Scanner(fileReader);
				
				if (!scheduleScanner.hasNext()) {
					System.out.println();
					System.out.print("That file is empty. ");
					continue;
				}
				while (scheduleScanner.hasNextLine()) {
					String currentLine = scheduleScanner.nextLine();
					String [] arr = currentLine.split(",", 3);
					
					String restaurantName = arr[1].trim();
					restaurantName = restaurantName.replaceAll("’", "'");

					String orderName = arr[2].trim();

					FileOrders.getAllOrders().add(new Order(restaurantName, orderName, Integer.parseInt(arr[0])));
				}
			}
			catch (FileNotFoundException fnf) {
				System.out.println();
				System.out.print("That file does not exist. ");
				continue;
			}
			catch (Exception e) {
				System.out.println();
				System.out.print("That file is not properly formatted. ");
				continue;
			}
			scheduleScanner.close();
			break;
		}
	}
}
