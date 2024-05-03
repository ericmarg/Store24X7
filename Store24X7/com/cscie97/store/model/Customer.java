package com.cscie97.store.model;

import java.time.LocalDateTime;

/**
 * The Customer class represents customers within the store. Customers must be of type registered in order to
 * remove items from the store. The accountAddress field stores the account address of their blockchain account.
 */
public class Customer implements Showable {

	private final String id;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private Boolean isRegistered;
	private Boolean isAdult;
	private String accountAddress;
	private String currentLocation = null;
	private LocalDateTime timeLastSeen = null;
	private Basket basket = null;

	public Customer(String id, String firstName, String lastName, Boolean isRegistered, Boolean isAdult, String emailAddress, String accountAddress) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isRegistered = isRegistered;
		this.isAdult = isAdult;
		this.emailAddress = emailAddress;
		this.accountAddress = accountAddress;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public Boolean getRegistered() {
		return isRegistered;
	}

	public Boolean getAdult() {
		return isAdult;
	}

	public String getAccountAddress() {
		return accountAddress;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public LocalDateTime getTimeLastSeen() {
		return timeLastSeen;
	}

	public void setTimeLastSeen(LocalDateTime timeLastSeen) {
		this.timeLastSeen = timeLastSeen;
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}

	@Override
	public void show() {
		System.out.println("Customer Details:");
		System.out.println("  ID: " + id);
		System.out.println("  First Name: " + firstName);
		System.out.println("  Last Name: " + lastName);
		System.out.println("  Email Address: " + emailAddress);
		System.out.println("  Registered: " + isRegistered);
		System.out.println("  Adult: " + isAdult);
		System.out.println("  Account Address: " + accountAddress);
		System.out.println("  Current Location: " + currentLocation);
		System.out.println("  Time Last Seen: " + timeLastSeen);

		if (basket != null) {
			System.out.println("Basket Details:");
			System.out.println("  Basket ID: " + basket.getId());
		}
	}

}
