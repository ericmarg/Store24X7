package com.cscie97.store.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Models aisles in a store. Aisles contain shelves. Shelves are stored in a map structure.
 */
public class Aisle implements Showable {
	private String id;
	private String name;
	private String description;
	private String location;
	private Map<String, Shelf> shelfMap = new HashMap<String, Shelf>();

	public Aisle(String id, String name, String description, String location) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
    }

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getLocation() {
		return location;
	}

	public Shelf getShelf(String shelfId) {
		return shelfMap.get(shelfId);
	}

	public void addShelf(String shelfId, String name, String level, String description, String temperature) {
		Shelf shelf = new Shelf(shelfId, name, level, description, temperature);
		shelfMap.put(shelfId, shelf);
	}

	@Override
	public void show() {
		// Display aisle details
		System.out.println("Aisle Details:");
		System.out.println("  ID: " + id);
		System.out.println("  Name: " + name);
		System.out.println("  Description: " + description);
		// Print details of shelves, if any
		if (shelfMap.isEmpty()) {
			System.out.println("No shelves in this aisle.");
		} else {
			System.out.println("Shelves in this aisle:");
			for (Shelf shelf : shelfMap.values()) {
				System.out.println(" - Shelf " + shelf.getId() + ", " + shelf.getName());
			}
		}
	}
}