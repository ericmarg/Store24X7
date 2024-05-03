package com.cscie97.store.model;

/**
 * Concrete Event created when a customer asks a microphone to check their account balance.
 */
public class CheckAccBalEvent extends Event {
    String customerId;
    String custAccountAddress;
    String speakerLocation;

    public CheckAccBalEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[1];
        this.custAccountAddress = eventArgs[2];
        this.speakerLocation = eventArgs[3];
    }

    @Override
    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getAccountAddress() {
        return custAccountAddress;
    }

    @Override
    public String getSpeakerLocation() {
        return speakerLocation;
    }
}
