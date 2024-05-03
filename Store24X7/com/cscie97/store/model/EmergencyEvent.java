package com.cscie97.store.model;

/**
 * Concrete Event created in response to an emergency situation such as fire or flood within the store.
 */
public class EmergencyEvent extends Event {
    String emergency;
    String aisleId;

    public EmergencyEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.emergency = eventArgs[1];
        this.aisleId = eventArgs[2];
    }

    @Override
    public String getEmergency() {
        return emergency;
    }

    @Override
    public String getAisleId() {
        return aisleId;
    }
}
