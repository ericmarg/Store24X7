package com.cscie97.store.model;

/**
 * Concrete Event created when a microphone detects the sound of glass breaking in an aisle.
 */
public class BrokenGlassEvent extends Event {
    String aisleId;

    public BrokenGlassEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.aisleId = eventArgs[1];
    }

    @Override
    public String getAisleId() {
        return aisleId;
    }
}
