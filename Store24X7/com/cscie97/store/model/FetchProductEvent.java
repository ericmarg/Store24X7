package com.cscie97.store.model;

/**
 * Concrete Event created when a customer commands a robot to fetch them a product.
 */
public class FetchProductEvent extends Event {
    String customerId;
    String customerLocation;
    String productId;
    String inventoryId;
    String shelfId;
    String userVoicePrint;
    int count;

    public FetchProductEvent(String deviceId, String storeId, String... eventArgs) {
        super(deviceId, storeId, eventArgs);
        this.customerId = eventArgs[1];
        this.customerLocation = eventArgs[2];
        this.productId = eventArgs[3];
        this.inventoryId = eventArgs[4];
        this.shelfId = eventArgs[5];
        this.count = Integer.parseInt(eventArgs[6]);
        this.userVoicePrint = eventArgs[7];
    }

    public String getUserVoicePrint() {
        return userVoicePrint;
    }

    // Getters for the fields (if needed)
    public String getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public String getShelfId() {
        return shelfId;
    }

    public int getCount() {
        return count;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }
}
