package com.cscie97.store.controller;

import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * A command for handling a broken glass event in the store.
 */
public class BrokenGlassCommand extends Command {
    /**
     * Constructs a BrokenGlassCommand.
     *
     * @param event      The broken glass event to be processed.
     * @param storeModel The store model's command processor.
     * @param ledger     The ledger's command processor.
     */
    public BrokenGlassCommand(Event event, CommandProcessor storeModel,
                              com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    /**
     * Executes the command by instructing a robot to clean up broken glass in the specified aisle.
     */
    @Override
    public void execute() {
        // Get an available robot
        String robotId = storeModel.processCommand("get-available-robot " + event.getStoreId(), controllerTokenId);

        // Create a command to instruct the robot to clean broken glass in the specified aisle
        storeModel.processCommand(String.format("create-command %s message clean broken glass in aisle %s",
                robotId, event.getAisleId()), controllerTokenId);
    }
}
