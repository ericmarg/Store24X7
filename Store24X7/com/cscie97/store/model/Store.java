package com.cscie97.store.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores represent physical locations that Customers can shop for Products in. Stores have Aisles, Sensors, and
 * Appliances.
 */
public class Store implements Showable {

	// Fields
	private final String id;
	private final String name;
	private final String address;
	private int currentCustomerCount;
	private Map<String, Aisle> aisleMap;

	// Constructor
	public Store(String id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
		aisleMap = new HashMap<String, Aisle>();
	}

	// Getter methods
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public int getCurrentCustomerCount() {
		return currentCustomerCount;
	}

	public void addAisle(String aisleID, String name, String description, String location) {
		Aisle aisle = new Aisle(aisleID, name, description, location);
		aisleMap.put(aisleID, aisle);
	}

	public Aisle getAisle(String aisleNumber) {
		return aisleMap.get(aisleNumber);
	}

	@Override
	public void show() {
		System.out.println("Store Details:");
		System.out.println("  ID: " + id);
		System.out.println("  Name: " + name);
		System.out.println("  Address: " + address);
		System.out.println("  Current Customer Count: " + currentCustomerCount);

		// Display Aisle details
		System.out.println("Aisles:");
		for (Map.Entry<String, Aisle> entry : aisleMap.entrySet()) {
			String aisleNumber = entry.getKey();
			Aisle aisle = entry.getValue();
			System.out.println("  Aisle Number: " + aisleNumber);
			System.out.println("    Name: " + aisle.getName());
			System.out.println("    Description: " + aisle.getDescription());
			System.out.println("    Location: " + aisle.getLocation());
		}
	}
}
