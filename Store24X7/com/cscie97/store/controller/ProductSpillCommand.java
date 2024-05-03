package com.cscie97.store.controller;

import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Command issued when a product is spilled within the store.
 */
public class ProductSpillCommand extends Command {
    public ProductSpillCommand(Event event, CommandProcessor storeModel,
                               com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    /**
     * Commands a robot to clean up the spilled product in the specified aisle.
     */
    @Override
    public void execute() {
        String robotId = storeModel.processCommand("get-available-robot " + event.getStoreId(), controllerTokenId);
        storeModel.processCommand(String.format("create-command %s message clean product %s in aisle %s",
                robotId, event.getProductId(), event.getAisleId()), controllerTokenId);
    }
}
