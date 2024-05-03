package com.cscie97.store.controller;

import com.cscie97.store.authentication.*;
import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Command issued in response to a customer entering a store
 */
public class EnterStoreCommand extends Command {
    String customerId;
    String customerName;
    String custAccountAddress;
    String aisleId;

    public EnterStoreCommand(Event event, CommandProcessor storeModel,
                             com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
        this.customerId = event.getCustomerId();
        this.customerName = event.getFirstName();
        this.custAccountAddress = event.getAccountAddress();
        this.aisleId = event.getAisleId();
    }

    /**
     * Checks that customer has a positive account balance. If they do, the customer is assigned a basket,
     * the turnstile is opened, and a welcome message is sent. Denies entry if balance is negative.
     */
    public void execute() {
        // authenticate customer
        AuthenticationService authService = AuthenticationService.getInstance();
        AuthToken userAuthToken = null;
        try {
            userAuthToken = authService.authenticateUser(event.getUserFacePrint(), CredentialType.FACE_PRINT);
            authService.checkAccess(userAuthToken.getId(), "enter_store", event.getStoreId());
        } catch (AuthenticationException | AccessDeniedException e) {
            System.out.println(e.getMessage());
            String alert = "Entry denied.";
            storeModel.processCommand(String.format("create-command %s message announce(%s)", event.getDeviceId(), alert), controllerTokenId);
            return;
        }
        int balance = ledger.processCommand(String.format("get-account-balance %s", custAccountAddress), 1);
        // Check for positive account balance
        if (balance < 0) {
            String alert = "Entry denied; account balance negative.";
            storeModel.processCommand(String.format("create-command %s message announce(%s)", event.getDeviceId(), alert), controllerTokenId);
        } else {
            // Open the turnstile
            storeModel.processCommand(String.format("create-command %s message open", event.getDeviceId()), controllerTokenId);
            // Send welcome message
            String welcome = String.format("Hello %s, welcome to %s!", customerName, event.getStoreId());
            storeModel.processCommand(String.format("create-command %s message announce(%s)", event.getDeviceId(), welcome), controllerTokenId);
            // Update customer location at the aisle level
            storeModel.processCommand(String.format("update-customer %s location %s",
                    event.getCustomerId(), event.getAisleId()), controllerTokenId);
            // Assign the customer a basket
            storeModel.processCommand(String.format("assign-basket %s customer %s", customerId + "-basket", customerId), controllerTokenId);
        }
    }
}
