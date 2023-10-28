package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;


public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<Vehicle> listCustomerVehicles = new ArrayList<>();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static List<BookingOrder> listBookingOrders = new ArrayList<>();
	private static Scanner input = new Scanner(System.in);

	private static Customer customer = null;
	private static Vehicle vehicle = null;
	private static MemberCustomer memberCustomer = null;
	private static ItemService itemService = null;
	private static BookingOrder bookingOrder = null;

	private static String custID = "";
	private static String vehcID = "";
	private static String servID = "";
	private static String password = "";
	private static String status = "";
	private static String metodePembayaran = "";


	private static int count = 0; //Menghitung kesalahan user dalam memasukan cust id/password


	public static void run() {
		boolean isLooping = true;
		do {
			clearScreen();
			isLooping = login();
			if (!isLooping) {
				isLooping = false;
			}
		} while (isLooping);
		
	}
	
	public static boolean login() {
		String [] listMenu = {"Login", "Exit"};
		int menuChoice = 0;
		
		boolean isLooping = true;

		do {
			PrintService.printMenu(listMenu, "Aplikasi Booking Bengkel");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu : ", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0, input);
			System.out.print(menuChoice);
			clearScreen();
			switch (menuChoice) {
				case 1:
					customer = BengkelService.loginSection(listAllCustomers, customer, input, custID, password, status, count);
					if (customer != null) {
						listCustomerVehicles = customer.getVehicles();
						clearScreen();
						mainMenu();
					} else if (customer == null) {
						isLooping = false;
					}
					break;
				case 0:
					isLooping = false;
				default:
					break;
			}
		} while (isLooping);
		return isLooping;
	}
	
	public static void mainMenu() {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		String bookID = "";
		int menuChoice = 0;
		boolean isLooping = true;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu : ", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0, input);
			
			switch (menuChoice) {
			case 1:
				//panggil fitur Informasi Customer
				clearScreen();
				System.out.println();
				System.out.println("Customer Profile");
				System.out.println();
				PrintService.printCustomerProfile(customer, memberCustomer, listCustomerVehicles);
				break;
			case 2:
				//panggil fitur Booking Bengkel
				clearScreen();
				System.out.println();
				bookingOrder = BengkelService.bookingBengkelSection(listCustomerVehicles, listAllItemService, input, customer, vehicle, itemService, vehcID, servID, bookID, metodePembayaran);
				if (bookingOrder != null) {
					listBookingOrders.add(bookingOrder);
				}
				break;
			case 3:
				//panggil fitur Top Up Saldo Coin
				clearScreen();
				BengkelService.topUpSaldo(customer, input);
				break;
			case 4:
				//panggil fitur Informasi Booking Order
				clearScreen();
				System.out.println();
				System.out.println("Booking Order Menu");
				System.out.println();
				PrintService.printBookOrder(listBookingOrders, customer, bookID);
				break;
			case 0:
				System.out.println("\nLogout\n");
				BengkelService.logOut(listBookingOrders);
				isLooping = false;
				clearScreen();
				break;
			}
		} while (isLooping);
	}

	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
	public static void clearScreen(){
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}
}
