package com.bengkel.booking.services;

import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.Vehicle;

public class Validation {
	
	public static String validasiInput(String question, String errorMessage, String regex, Scanner input) {
	    String result;
	    boolean isLooping = true;
	    do {
	      System.out.print(question);
	      result = input.nextLine();

	      //validasi menggunakan matches
	      if (result.matches(regex)) {
	        isLooping = false;
	      }else {
	        System.out.println(errorMessage);
	      }

	    } while (isLooping);

	    return result;
	  }
	
	public static int validasiNumberWithRange(String question, String errorMessage, String regex, int max, int min, Scanner input) {
	    int result;
	    boolean isLooping = true;
	    do {
	      result = Integer.valueOf(validasiInput(question, errorMessage, regex, input));
	      if (result >= min && result <= max) {
	        isLooping = false;
	      }else {
	        System.out.println("Pilihan angka " + min + " s.d " + max);
	      }
	    } while (isLooping);

	    return result;
	}

	public static String validasiVehicleId(List<Vehicle> listCustomerVehicles, Scanner input){
		String userInput = "";
		boolean isThere = false;

		do {
			userInput = input.nextLine();
			for (Vehicle vehicle : listCustomerVehicles) {
				if (vehicle.getVehiclesId().equals(userInput)) {
					isThere = true;
				}
			}
			if (!isThere) {
				System.out.println("Kendaraan Tidak ditemukan");
			}
		} while (!isThere);
		return userInput;
	}

	public static String validasiServiceId(List<ItemService> lItemServices, Scanner input){
		String userInput = "";
		boolean isThere = false;

		do {
			userInput = input.nextLine();
			if (userInput.equalsIgnoreCase("0")) {
				isThere = true;
			} else {
				for (ItemService itemService : lItemServices) {
					if (itemService.getServiceId().equals(userInput)) {
						isThere = true;
					}
				}
				if (!isThere) {
					System.out.println("Service tidak ditemukan");
				}
			}
		} while (!isThere);
		return userInput;
	}

	public static String validasiYesOrNot(Scanner input){
		String userInput = "";

		boolean isCorrect = false;

		do {
			userInput = input.nextLine();
			if (userInput.equalsIgnoreCase("Y") || userInput.equalsIgnoreCase("T")) {
				isCorrect = true;
			}
			if (!isCorrect) {
				System.out.println("Inputan tidak sesuai, silahkan ulangi!");
			}
		} while (!isCorrect);

		return userInput;
	}

	public static String validasiMetodePembayaran(Customer customer, Scanner input){
		String userInput = "";

		boolean isCorrect = false;

		do {
			userInput = input.nextLine();
			if (customer.getMemberStatus().equalsIgnoreCase("Member")) {
				if (userInput.equalsIgnoreCase("Saldo Coin") || userInput.equalsIgnoreCase("Cash")) {
					isCorrect = true;
					System.out.println("Booking Berhasil!");
				}
			} else if (customer.getMemberStatus().equalsIgnoreCase("Non Member")) {
				if (userInput.equalsIgnoreCase("Cash")) {
					System.out.println("Booking Berhasil!");
					isCorrect = true;
				}
			}
			if (!isCorrect) {
				System.out.println("Metode pembayaran tidak sesuai atau tidak tersedia");
			}
		} while (!isCorrect);
		return userInput;
	}
}
