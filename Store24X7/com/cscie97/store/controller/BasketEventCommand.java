package com.cscie97.store.controller;

import com.cscie97.store.model.*;

/**
 * When a customer takes a product from a shelf or puts it back this command is issued to adjust the customer's
 * shopping basket contents, adjust the shelf inventory, and restock the shelf using a robot shop assistant.
 */
public class BasketEventCommand extends Command {
    String customerId;
    String productId;
    String inventoryId;
    String shelfId;
    int itemCount;

    public BasketEventCommand(Event event, CommandProcessor storeModel,
                              com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
        this.customerId = event.getCustomerId();
        this.productId = event.getProductId();
        this.inventoryId = event.getInventoryId();
        this.shelfId = event.getShelfId();
        this.itemCount = event.getCount();
    }

    @Override
    public void execute() {
        String basketId = storeModel.processCommand("get-customer-basket " + customerId, controllerTokenId);

        // Add/remove the product to/from customer basket
        if (itemCount >= 0) {
            System.out.printf("Adding %d of product %s to basket %s%n", itemCount, productId, basketId);
            storeModel.processCommand(String.format("add-basket-item %s product %s item_count %d",
                    basketId, productId, itemCount), controllerTokenId);
        } else {
            System.out.printf("Removing %d of product %s from basket %s%n", itemCount, productId, basketId);
            storeModel.processCommand(String.format("remove-basket-item %s product %s item_count %d",
                    basketId, productId, itemCount), controllerTokenId);
        }

        // Remove/add product from/to shelf
        System.out.printf("update-inventory %s update_count %d%n", inventoryId, itemCount * -1);
        storeModel.processCommand(String.format("update-inventory %s update_count %d",
                inventoryId, itemCount * -1), controllerTokenId);

        String robotId = storeModel.processCommand("get-available-robot " + event.getStoreId(), controllerTokenId);

        // Restock shelf with product
        storeModel.processCommand(String.format("create-command %s message restock(%s, %s)",
                robotId, shelfId, productId),controllerTokenId);
    }
}
