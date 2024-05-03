package com.cscie97.store.model;

/**
 * This class represents the turnstiles through which customers exit the store. Each turnstile has a
 * unique identifier and a state, open or closed, and automatically checks the customer out based
 * on the items in their basket as they leave.
 */
public class Turnstile extends Appliance  {

	private String id;

	private boolean state;

    public Turnstile(String id, String name, String location) {
		super(id, name, "turnstile", location);
		this.id = id;
    }

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void open() {
		this.setState(true);
	}

	public void close() {
		this.setState(false);
	}

	public Boolean checkoutCustomer(int custId) {
		return null;
	}
}
