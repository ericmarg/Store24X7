package com.cscie97.store.model;

/**
 * Shelves exist in Aisles and hold Products. The Product that a Shelf may hold is determined by its Inventory.
 * Shelves have a capacity and temperature, so not all inventories are appropriate for all shelves.
 */
public class Shelf implements Showable {
	private String id;
	private int capacity;
	private String level;
	private String name;
	private String description;
	private String temperature;
	private Inventory inventory;

	public Shelf(String id, String name, String level, String description, String temperature) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.description = description;
		this.temperature = temperature;
	}

	@Override
	public void show() {
		System.out.println("Shelf Details:");
		System.out.println("  ID: " + id);
		System.out.println("  Name: " + name);
		System.out.println("  Level: " + level);
		System.out.println("  Description: " + description);
		System.out.println("  Temperature: " + temperature);
	}


	// Getter methods for accessing private fields
	public String getId() {
		return id;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getTemperature() {
		return temperature;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}
