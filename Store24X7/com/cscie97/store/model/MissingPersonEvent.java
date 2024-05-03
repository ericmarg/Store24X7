package com.cscie97.store.model;

/**
 * Concrete Event created when a customer asks a microphone to locate another person within the store.
 */
public class MissingPersonEvent extends Event {
    String customerVoicePrint;
    String customerId;

    public MissingPersonEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[2];
        this.customerVoicePrint = eventArgs[3];
    }

    // Getter for the customerId
    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getUserVoicePrint() {
        return this.customerVoicePrint;
    }
}
