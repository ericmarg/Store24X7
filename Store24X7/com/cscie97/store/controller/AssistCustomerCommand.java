package com.cscie97.store.controller;

import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Sends a robot to assist a customer in carrying their goods from the store to their car.
 */
public class AssistCustomerCommand extends Command {
    public AssistCustomerCommand(Event event, CommandProcessor storeModel, com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    @Override
    public void execute() {
        String robotId = storeModel.processCommand("get-available-robot " + event.getStoreId(), controllerTokenId);
        storeModel.processCommand(String.format("create-command %s message assist %s at %s to car",
                        robotId, event.getCustomerId(), event.getTurnstileLocation()), controllerTokenId);
    }
}
