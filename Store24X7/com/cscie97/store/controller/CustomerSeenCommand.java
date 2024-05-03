package com.cscie97.store.controller;

import com.cscie97.ledger.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Updates customer location as they move throughout the store.
 */
public class CustomerSeenCommand extends Command {
    public CustomerSeenCommand(Event event, com.cscie97.store.model.CommandProcessor storeModel,
                               CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    public void execute() {
        // Update customer location at the aisle level
        storeModel.processCommand(String.format("update-customer %s location %s",
                event.getCustomerId(), event.getAisleId()), controllerTokenId);
    }
}
