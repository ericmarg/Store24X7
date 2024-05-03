package com.cscie97.store.model;

/**
 * Concrete Event created when a customer takes something from a shelf or puts something back on a shelf.
 */
public class BasketEvent extends Event {
    String customerId;
    String productId;
    String inventoryId;
    String shelfId;
    int count;

    public BasketEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[1];
        this.productId = eventArgs[2];
        this.inventoryId = eventArgs[3];
        this.shelfId = eventArgs[4];
        this.count = Integer.parseInt(eventArgs[5]);
    }
    @Override
    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public String getInventoryId() {
        return inventoryId;
    }

    @Override
    public String getShelfId() {
        return shelfId;
    }

    @Override
    public int getCount() {
        return count;
    }
}

