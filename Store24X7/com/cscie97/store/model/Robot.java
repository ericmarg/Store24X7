package com.cscie97.store.model;

/**
 * Robots are the workers in the store. They clean up spills, stock shelves, and answer queries from customers.
 * They can be commanded by the Store Controller Service via the API that the Store Model Service provides.
 */
public class Robot extends Appliance  {

	private final String id;

	private String currentLocation;

	private String currentTask;

	public Robot(String id, String name, String location) {
		super(id, name, "robot", location);
		this.id = id;
		this.currentLocation = location;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getCurrentTask() {
		return currentTask;
	}

	public void cleanFloor(int aisle, int shelf, String location) {
	}

	public void stockShelf(int productId, int aisleNumber, int shelfNumber, int count) {
	}

	/**
	 * Functionality to implement in assignment 3
	 *
	 */
	public int countInventory(Product product, String location) {
        int count = 0;
		return count;
    }

	public void voiceResponse(String question) {
	}

	@Override
	public void show() {
		System.out.println("Robot Details:");
		System.out.println("  ID: " + id);
		System.out.println("  Name: " + getName()); // Use the inherited getName() method
		System.out.println("  Current Location: " + currentLocation);
		System.out.println("  Current Task: " + currentTask);
	}
}
