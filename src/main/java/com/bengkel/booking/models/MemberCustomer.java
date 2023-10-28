package com.bengkel.booking.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCustomer extends Customer {
	private double saldoCoin;
	
	public MemberCustomer(String customerId, String name, String address, String password, String memberStatus, List<Vehicle> vehicles,
			double saldoCoin) {
		super(customerId, name, address, password, memberStatus, vehicles);
		this.saldoCoin = saldoCoin;
		setMaxNumberOfService(2);
	}

}
