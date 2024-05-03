package com.cscie97.store.model;

/**
 * Inventory represents the amount, quantity, and identity of items on shelves throughout the store. Each shelf
 * may have up to one inventory associated with it.
 */
public class Inventory implements Showable {

	private final String id;

	private final String location;

	private final int capacity;

	private int count;

	private final String productId;

	public Inventory(String inventoryId, String location, int capacity, int count, String productId) {
		this.id = inventoryId;
		this.location = location;
		this.capacity = capacity;
		this.count = count;
		this.productId = productId;
	}
	public String getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getCount() {
		return count;
	}

	public void updateCount(int count) {
		this.count += count;
	}

	public String getProductId() {
		return productId;
	}

	// Show method to display inventory details
	@Override
	public void show() {
		System.out.println("Inventory Details:");
		System.out.println("  ID: " + id);
		System.out.println("  Location [store, aisle, shelf]: " + location);
		System.out.println("  Capacity: " + capacity);
		System.out.println("  Count: " + count);
		System.out.println("  Product ID: " + productId);
	}
}
