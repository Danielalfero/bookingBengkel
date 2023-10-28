package com.bengkel.booking.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	private String customerId;
	private String name;
	private String address;
	private String password;
	private String memberStatus;
	private List<Vehicle> vehicles;
	private int maxNumberOfService;

	public Customer(String customerId, String name, String address, String password, String memberStatus, List<Vehicle> vehicles) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.password = password;
		this.memberStatus = memberStatus;
		this.vehicles = vehicles;
		this.maxNumberOfService = 1;
	}

}
