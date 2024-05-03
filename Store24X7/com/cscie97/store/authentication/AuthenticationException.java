package com.cscie97.store.authentication;

/**
 * Throwable exception class. Thrown by AuthenticationService when the service failed to recognize the user.
 */
public class AuthenticationException extends Throwable {

	private String message;

	@Override
	public String getMessage() {
		return message;
	}

	public AuthenticationException(String message) {
		this.message = message;
	}
}
