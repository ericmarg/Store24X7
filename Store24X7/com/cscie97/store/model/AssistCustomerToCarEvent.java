package com.cscie97.store.model;

/**
 * Concrete Event created when a customer asks for assistance in bringing their groceries to their car.
 */
public class AssistCustomerToCarEvent extends Event {
    String customerId;
    String turnstileLocation;

    public AssistCustomerToCarEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[1];
        this.turnstileLocation = eventArgs[2];
    }

    @Override
    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getTurnstileLocation() {
        return turnstileLocation;
    }
}
