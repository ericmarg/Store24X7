package com.cscie97.store.model;

/**
 * Sensors are placed throughout the store to monitor for events. Events may be customer movements, spilled products,
 * emergencies, or any number of other occurrences. Sensors are observable so that the Store Controller Service can
 * be notified of events when they occur and manage the store accordingly.
 */
public abstract class Sensor implements Showable {
	private String id;
	private String name;
	private String location;
	private String type;

	public Sensor(String id, String name, String location, String type) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.type = type;
	}

	public void triggerEventNotice(String event) {
		// Print event to stdout for testing clarity
		System.out.println("Event detected: " + event);
	}

	@Override
	public void show() {
		System.out.println("Appliance Details:");
		System.out.println("  Device ID: " + id);
		System.out.println("  Name: " + name);
		System.out.println("  Type: " + type);
		System.out.println("  Location: " + location);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getType() {
		return type;
	}
}
