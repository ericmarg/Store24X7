package com.cscie97.store.authentication;

/**
 * Throwable exception class. Thrown by AuthenticationService when a user tries to access a resource that they do
 * not have permission to access.
 */
public class AccessDeniedException extends Throwable {
	private String message;
	public AccessDeniedException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
