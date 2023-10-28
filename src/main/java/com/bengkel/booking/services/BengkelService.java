package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class BengkelService {
	
	//Silahkan tambahkan fitur-fitur utama aplikasi disini
	
	//Login
	public static Customer loginSection(List<Customer> listAllCustomer, Customer customers, Scanner input, String custID, String password, String status, int count){

		boolean isThere = false;

		
			System.out.println("Login\n");

			System.out.println("Masukan Customer Id :");
			do {
				custID = input.nextLine();
				for (Customer cust : listAllCustomer) {
					if (cust.getCustomerId().equals(custID)) {
						isThere = true;
						customers = getCustomerByIdV2(listAllCustomer, custID);
					}
				}
				if (!isThere) {
					System.out.println("Customer Id Tidak Ditemukan atau Salah!");
					count++;
				}
				if (count > 3) {
					isThere = true;
					break;
				}
				if (isThere) {
					isThere = false;
					System.out.println("Masukan Password :");
					do {
						password = input.nextLine();
						for (Customer cust : listAllCustomer) {
							if (cust.getPassword().equals(password) && customers.getPassword().equals(password)) {
								isThere = true;
							}
						}
						if (!isThere) {
							System.out.println("Passwword yang anda Masukan Salah!");
							count++;
						}
						if (count > 3) {
							isThere = true;
						}
					} while (!isThere);
				}
			} while (!isThere);
			customers = getCustomerById(listAllCustomer, custID, password);
			if (customers != null) {
				if (customers instanceof MemberCustomer) {
					status = "Member";
					customers.setMemberStatus(status);
				} else {
					status = "Non Member";
					customers.setMemberStatus(status);
				}
			}
			return customers;
	}

	public static Customer getCustomerById(List<Customer> listCustomers, String custID, String password){
		return listCustomers.stream()
			.filter(data -> data.getCustomerId().equals(custID) && data.getPassword().equals(password))
			.findFirst().orElse(null);
	}

	public static Customer getCustomerByIdV2(List<Customer> listCustomers, String custID){
		return listCustomers.stream()
				.filter(data -> data.getCustomerId().equals(custID))
				.findFirst().orElse(null);
	}

	//Info Customer ada di prin service
	
	//Booking atau Reservation
	public static BookingOrder bookingBengkelSection(List<Vehicle> listCustomerVehicle, List<ItemService> listServices,
	Scanner input, Customer customer, Vehicle vehicle, ItemService itemService, String vehcID, String servID, String bookID, String pembayaran){
		BookingOrder bookingOrder = null;
		List<ItemService> listCustomerseItemServices = new ArrayList<>();
		int count = 0;
		int limit = customer.getMaxNumberOfService();

		double servicePrice = 0;
		double totalPembayaran = 0;
		double sisa = 0;

		String again = "";

		boolean isDone = false;


		System.out.println("Booking Bengkel\n");
		System.out.println("Masukan Vehicle Id :");
		vehcID = Validation.validasiVehicleId(listCustomerVehicle, input);
		vehicle = getVehiclesById(listCustomerVehicle, vehcID);
		PrintService.printService(listServices, vehicle);
		do {
			System.out.println("\nSilahkan masukan Service Id :");
			servID = Validation.validasiServiceId(listServices, input);
			count++;
			if (servID.equalsIgnoreCase("0")) {
				isDone = true;
			} else {
				if (count >= limit) {
					isDone = true;
				}
				itemService = getItemServiceById(listServices, servID);
				listCustomerseItemServices.add(itemService);
				System.out.println("Apakah anda ingin menambahkan Service yang lainnya(Y/T)?");
				again = Validation.validasiYesOrNot(input);
				if (again.equalsIgnoreCase("Y")) {
					isDone = false;
				} else if (again.equalsIgnoreCase("T")) {
					isDone = true;
				}
			}
			
		} while (!isDone);
		
		if (!servID.equalsIgnoreCase("0")) {
			for (ItemService itemService2 : listCustomerseItemServices) {
			servicePrice += itemService2.getPrice();
			}

			System.out.println("Silahkan Pilih Metode Pembayaran (Saldo Coin atau Cash)");
			pembayaran = Validation.validasiMetodePembayaran(customer, input);
			bookingOrder = new BookingOrder();
			bookingOrder.setPaymentMethod(pembayaran);
			bookingOrder.setTotalServicePrice(servicePrice);
			bookingOrder.calculatePayment();

			totalPembayaran = bookingOrder.getTotalPayment();

			if (pembayaran.equalsIgnoreCase("Saldo Coin")) {
				sisa = ((MemberCustomer)customer).getSaldoCoin() - totalPembayaran;
				if (sisa < 0) {
					System.out.println("Saldo Coin Anda tidak cukup!");
					System.out.println("Metode Pembayaran akan dialihkan ke 'Cash'");
					pembayaran = "Cash";
					bookingOrder.setPaymentMethod(pembayaran);
					bookingOrder.setTotalServicePrice(servicePrice);
					bookingOrder.calculatePayment();

					totalPembayaran = bookingOrder.getTotalPayment();
				}
			}
			System.out.println();
			
			

			System.out.println("Total Harga Service\t: " + PrintService.formatCurency(servicePrice));
			System.out.println("Total Pembayaran\t: " + PrintService.formatCurency(totalPembayaran));

			if (customer instanceof MemberCustomer) {
				((MemberCustomer)customer).setSaldoCoin(((MemberCustomer)customer).getSaldoCoin() - totalPembayaran);
			}
			bookingOrder = new BookingOrder(bookID, customer, listCustomerseItemServices, pembayaran, 0, 0);
			bookingOrder.setTotalServicePrice(servicePrice);
			bookingOrder.setTotalPayment(totalPembayaran);
		}
		return bookingOrder;
	}

	public static Vehicle getVehiclesById(List<Vehicle> listCustomerVehicles, String vehcID){
		return listCustomerVehicles.stream()
			.filter(data -> data.getVehiclesId().equals(vehcID))
			.findFirst().get();
	}

	public static ItemService getItemServiceById(List<ItemService> lItemServices, String servID){
		return lItemServices.stream()
			.filter(data -> data.getServiceId().equals(servID))
			.findFirst().get();
	}

	//Top Up Saldo Coin Untuk Member Customer
	public static void topUpSaldo(Customer customer, Scanner input){
		double topUp = 0;

		boolean isMember = validasiCustomerByMember(customer);

		System.out.println("Top Up Saldo Coin");
		System.out.println();
		
		if (isMember) {
			System.out.println("Masukan besaran Top Up : ");
			topUp = Double.valueOf(input.nextLine());
			((MemberCustomer)customer).setSaldoCoin(((MemberCustomer)customer).getSaldoCoin() + topUp);
		} else {
			System.out.println("Maaf fitur ini hanya untuk Member saja");
		}
	}

	public static boolean validasiCustomerByMember(Customer customer){
		if (!(customer instanceof MemberCustomer)) {
			return false;
		}
		return true;
	}
	//Logout
	public static void logOut(List<BookingOrder> listBookingOrders){
		listBookingOrders.clear();
	}
}
