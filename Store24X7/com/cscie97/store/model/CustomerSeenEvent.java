package com.cscie97.store.model;

public class CustomerSeenEvent extends Event {
    String customerId;
    String aisleId;

    public CustomerSeenEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[1];
        this.aisleId = eventArgs[2];
    }

    // Getter for the customerId
    public String getCustomerId() {
        return customerId;
    }

    // Getter for the aisleId
    public String getAisleId() {
        return aisleId;
    }
}
