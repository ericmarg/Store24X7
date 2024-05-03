package com.cscie97.store.model;

/**
 * Concrete Event created when a customer leaves the store through a turnstile.
 */
public class CheckoutEvent extends Event {
    String userFacePrint;
    String customerId;
    String accountAddress;
    String firstName;

    public CheckoutEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[1];
        this.accountAddress = eventArgs[2];
        this.firstName = eventArgs[3];
        this.userFacePrint = eventArgs[4];
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getAccountAddress() {
        return accountAddress;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getUserFacePrint() {
        return this.userFacePrint;
    }
}
