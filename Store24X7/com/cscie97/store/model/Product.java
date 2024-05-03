package com.cscie97.store.model;

/**
 * Products represent the items that the store sells. Products have a number of qualities associated with them.
 * Products have a one-to-one relationship with Inventories.
 */
public class Product implements Showable {

	private String id;
	private String name;
	private String description;
	private String category;
	private String size;
	private int price;
	private String temperature;

	public Product(String productId, String name, String description, String size, String category, int unitPrice, String temperature) {
		this.id = productId;
		this.name = name;
		this.description = description;
		this.size = size;
		this.category = category;
		this.price = unitPrice;
		this.temperature = temperature;
	}

	// Getter methods for the Product class fields

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public String getSize() {
		return size;
	}

	public int getPrice() {
		return price;
	}

	public String getTemperature() {
		return temperature;
	}

	@Override
	public void show() {
		System.out.println("Product Details:");
		System.out.println("  ID: " + id);
		System.out.println("  Name: " + name);
		System.out.println("  Description: " + description);
		System.out.println("  Size: " + size);
		System.out.println("  Category: " + category);
		System.out.println("  Unit Price: " + price);
		System.out.println("  Temperature: " + temperature);
	}
}
