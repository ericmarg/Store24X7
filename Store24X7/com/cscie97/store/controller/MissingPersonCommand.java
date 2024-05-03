package com.cscie97.store.controller;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.CredentialType;
import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Command issued in response to a voice command from a customer indicated that they want help locating someone.
 */
public class MissingPersonCommand extends Command {
    public MissingPersonCommand(Event event, CommandProcessor storeModel, com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    /**
     * Gets the location of the missing person and announces it over a speaker.
     */
    @Override
    public void execute() {
        String customerId = event.getCustomerId();
        String missingPersonLocation = storeModel.processCommand("get-customer-location " + customerId, controllerTokenId);
        String deviceLocation = storeModel.processCommand("get-device-location " + event.getDeviceId(), controllerTokenId);
        String speakerId = storeModel.processCommand("get-nearest-speaker " + deviceLocation, controllerTokenId);
        String response = String.format("Individual is in aisle %s", missingPersonLocation);
        storeModel.processCommand(String.format("create-command %s message announce(%s)", speakerId, response), controllerTokenId);
    }
}
