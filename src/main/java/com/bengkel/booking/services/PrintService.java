package com.bengkel.booking.services;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class PrintService {
	
	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s %-25s %n";
		
		System.out.println();
		System.out.printf("%-25s %n", title);
		System.out.println(line);
		
		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number + ".", data);
			}else {
				System.out.printf(formatTable, 0 + ".", data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}
	
	public static void printVechicle(List<Vehicle> listVehicle) {
		String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-5s |%n";
		String line = "+----+-----------------+------------+-----------------+-------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Vechicle Id", "Warna", "Tipe Kendaraan", "Tahun");
	    System.out.format(line);
	    int number = 1;
	    for (Vehicle vehicle : listVehicle) {
	    	System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getVehicleType(), vehicle.getYearRelease());
	    	number++;
	    }
	    System.out.printf(line);
	}
	
	//Silahkan Tambahkan function print sesuai dengan kebutuhan.

	public static void printCustomerProfile(Customer customer, MemberCustomer memberCustomer, List<Vehicle> listCustomerVehicles){
		String[] data = {"Customer Id  ", "Nama  ", "Customer Status  ", "Alamat  ", "Saldo Koin "};
		String formatNonTable = "%-25s %-25s %n";

		List<String> dataCustomer = Arrays.asList(customer.getCustomerId(), customer.getName(), customer.getMemberStatus(), customer.getAddress());
		
		
		int num = 0;

		for (String string : dataCustomer) {
			if (customer.getMemberStatus().equals("Non Member")) {
				System.out.printf(formatNonTable, data[num], ": " + string);
				if (num <= data.length - 2) {
					num++;
				}
			} else if (customer.getMemberStatus().equals("Member")) {
				memberCustomer = (MemberCustomer)customer;
				System.out.printf(formatNonTable, data[num], ": " + string);
				num++;
				if(num == data.length - 1){
					System.out.printf(formatNonTable, data[num], ": " + formatCurency(memberCustomer.getSaldoCoin()));
				}
			}
		}
		System.out.println("List Kendaraan");
		printVechicle(listCustomerVehicles);
	}
	
	public static void printService(List<ItemService> listCustomerServices, Vehicle vehicle){
		String formatTable = "| %-2s | %-15s | %-15s | %-15s | %-8s |%n";
		String line = "+----+-----------------+-----------------+-----------------+----------+%n";
		System.out.format(line);
		System.out.format(formatTable, "No", "Service Id", "Nama Service", "Tipe Kendaraan", "Harga");
		System.out.format(line);
		int number = 1;
		for (ItemService service : listCustomerServices) {
			if (service.getVehicleType().equals(vehicle.getVehicleType())) {
				System.out.printf(formatTable, number, service.getServiceId(), service.getServiceName(), service.getVehicleType(), formatCurency(service.getPrice()));
			}
		}
		System.out.format(line);
		System.out.printf("| %-2s | %-62s |%n", 0, "Kembali ke Home Menu");
		System.out.println("+----+----------------------------------------------------------------+");
	}

	public static String printServices(List<ItemService> serviceList){
        String result = "";
        // Bisa disesuaikan kembali
        for (ItemService service : serviceList) {
            result += service.getServiceName() + ", ";
        }
        return result;
    }

	public static void printBookOrder(List<BookingOrder> listBookingOrders, Customer customer, String bookID){
		String formatTable = "| %-2s | %-17s | %-13s | %-14s | %-13s | %-13s | %-25s| \n";
		String line = "+----+-------------------+---------------+----------------+---------------+---------------+--------------------------+\n";
		int num = 1;

		System.out.print(line);
		System.out.printf(formatTable, "No", "Booking Id", "Nama Customer", "Payment Method", "Total Service", "Total Payment", "List Service");
		System.out.print(line);
		int a = 0;
		for (BookingOrder bookingOrder : listBookingOrders) {
			String custId = "";
				for (int i = 4; i <= customer.getCustomerId().length() - 1; i++) {
					custId += customer.getCustomerId().charAt(i);
				}
				
				if (a < 10) {
					bookID = "Book-Cust-00" + (a + 1) + custId;
				} else if (a >= 10 && a < 100) {
					bookID = "Book-Cust-0" + (a + 1) + custId;
				} else if (a >= 100){
					bookID = "Book-Cust-" + (a + 1) + custId;						
				}
				bookingOrder.setBookingId(bookID);
				System.out.printf(formatTable, num, bookingOrder.getBookingId(), bookingOrder.getCustomer().getName(), bookingOrder.getPaymentMethod(), formatCurency(bookingOrder.getTotalServicePrice()), formatCurency(bookingOrder.getTotalPayment()), printServices(bookingOrder.getServices()));
				num++;
				a++;

			
		}
		System.out.print(line);
	}

	public static String formatCurency(double nominal){
				String result = "";
        String pattern = "#,###";

        DecimalFormat decimalFormat = new DecimalFormat(pattern);

				result = decimalFormat.format(nominal);
        return result;
	}
}
