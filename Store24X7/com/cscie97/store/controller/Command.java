package com.cscie97.store.controller;

import com.cscie97.store.model.Event;

/**
 * Abstract class used to represent commands issued to store appliances through the StoreModelService API.
 */
public abstract class Command {
    String controllerTokenId;
    Event event;
    com.cscie97.store.model.CommandProcessor storeModel;
    com.cscie97.ledger.CommandProcessor ledger;

    public Command(Event event,
                   com.cscie97.store.model.CommandProcessor storeModel,
                   com.cscie97.ledger.CommandProcessor ledger,
                   String controllerTokenId) {
        this.event = event;
        this.storeModel = storeModel;
        this.ledger = ledger;
        this.controllerTokenId = controllerTokenId;
    }

    public void execute() {

    }
}
