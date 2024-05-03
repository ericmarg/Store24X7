package com.cscie97.store.model;

/**
 * Appliances are objects that can be managed by the Store Controller Service. They can perform
 * tasks and have Sensor capabilities. Types in this implementation include Robot, Speaker, and Turnstile.
 */
public abstract class Appliance implements Showable {
	private String id;
	private String name;
	private String type;
	private String location;
	private String currentTask;

	public Appliance(String id, String name, String type, String location) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.location = location;
    }

	public String getDeviceId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public String getCurrentTask() {
		return currentTask;
	}

	@Override
	public void show() {
		System.out.println("Appliance Details:");
		System.out.println("  Device ID: " + id);
		System.out.println("  Name: " + name);
		System.out.println("  Type: " + type);
		System.out.println("  Location: " + location);
	}

	public void setCurrentTask(String command) {
		// Print to stdout for clarity
		System.out.println("--Device " + this.getDeviceId() + " received command " + command);
		this.currentTask = command;
	}

	public void triggerEventNotice(String event) {
		// Print event to stdout for testing clarity
		System.out.println("Event detected: " + event);
	}

	public class ApplianceException extends Throwable {
		private String action;
		private String reason;
		private int applianceId;
		public ApplianceException(String action, String reason) {
			super("StoreModelServiceException: " + action + " - " + reason);
			this.action = action;
			this.reason = reason;
		}
		public String getAction() {
			return action;
		}
		public String getReason() {
			return reason;
		}
	}
}
