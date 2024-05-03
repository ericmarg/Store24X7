package com.cscie97.store.model;

/**
 * Concrete Event created when a customer enters a store through a turnstile.
 */
public class EnterStoreEvent extends Event {
    private final String userFacePrint;
    String customerId;
    String customerName;
    String custAccountAddress;
    String aisleId;

    public EnterStoreEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[1];
        this.customerName = eventArgs[2];
        this.custAccountAddress = eventArgs[3];
        this.aisleId = eventArgs[4];
        this.userFacePrint = eventArgs[5];
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAisleId() {
        return aisleId;
    }

    public String getFirstName() {
        return customerName;
    }

    public String getAccountAddress() {
        return custAccountAddress;
    }

    @Override
    public String getUserFacePrint() {
        return userFacePrint;
    }
}
