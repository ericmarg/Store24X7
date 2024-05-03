package com.cscie97.store.model;

/**
 * Cameras placed throughout the store monitor customer locations and detect events.
 * When an event occurs, the camera notifies its observers.
 */
public class Camera extends Sensor {
	private int eventCount;
	public Camera(String id, String name, String location) {
		super(id, name, location, "camera");
	}

	public void triggerEventNotice(String event) {
		eventCount += 1;
	}

	public int getEventCount() {
		return eventCount;
	}
}
