package com.cscie97.store.model;

/**
 * Concrete Event created when a product is spilled within a store.
 */
public class SpilledProductEvent extends Event {
    String aisleId;
    String productId;

    public SpilledProductEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.aisleId = eventArgs[1];
        this.productId = eventArgs[2];
    }

    @Override
    public String getAisleId() {
        return aisleId;
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
