package com.cscie97.store.model;

/**
 * Throwable Exception class for StoreModelService method issues.
 */
public class StoreModelServiceException extends RuntimeException {
    private final String action;
    private final String reason;
    public StoreModelServiceException(String action, String reason) {
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