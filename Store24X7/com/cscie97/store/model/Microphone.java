package com.cscie97.store.model;

/**
 * Microphone is a type of Sensor that listens for voice commands from customers. Microphones are paired with speakers
 * to provide an interface for customers to interact with.
 */
public class Microphone extends Sensor {

	public Microphone(String id, String name, String location) {
		super(id, name, location, "microphone");
	}

	public void processCommand() {

	}
}
