package com.cscie97.store.model;

/**
 * An abstract class representing an event within a store.
 */
public abstract class Event {
    String storeId;
    String deviceId;
    String commandClass;

    /**
     * Constructs an Event object with the provided device ID, store ID, and event arguments.
     *
     * @param deviceId   The unique identifier of the device generating the event.
     * @param storeId    The unique identifier of the store where the event occurs.
     * @param eventArgs  An array of event-specific arguments.
     */
    public Event(String deviceId, String storeId, String... eventArgs) {
        this.storeId = storeId;
        this.deviceId = deviceId;
        this.commandClass = (eventArgs.length > 0) ? eventArgs[0] : null;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getCommandClass() {
        return commandClass;
    }

    public String getCustomerId() {
        return null;
    }

    public String getAccountAddress() {
        return null;
    }

    public String getFirstName() {
        return null;
    }

    public String getTurnstileLocation() {
        return null;
    }

    public String getEmergency() {
        return null;
    }

    public String getAisleId() {
        return null;
    }

    public String getSpeakerLocation() {
        return null;
    }

    public String getProductId() {
        return null;
    }

    public String getInventoryId() {
        return null;
    }

    public String getShelfId() {
        return null;
    }

    public int getCount() {
        return 0;
    }

    public String getCustomerLocation() {
        return null;
    }

    public String getUserVoicePrint() {
        return null;
    }

    public String getUserFacePrint() {
        return null;
    }
}
