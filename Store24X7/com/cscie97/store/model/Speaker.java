package com.cscie97.store.model;

/**
 * Speakers are paired with Microphones to provide an interface that Customers can interact with. Speakers can be
 * issued commands via the Store Model Service API.
 */
public class Speaker extends Appliance {

	private String id;

	private String location;

	public Speaker(String id, String name, String location) {
		super(id, name, "speaker", location);
		this.id = id;
		this.location = location;
	}

	public void speak(int String) {

	}
}
