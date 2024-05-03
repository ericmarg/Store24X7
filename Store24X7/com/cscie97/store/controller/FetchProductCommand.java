package com.cscie97.store.controller;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.CredentialType;
import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Issued in response to a fetch product event.
 */
public class FetchProductCommand extends Command {
    String customerId;
    String customerLocation;
    String productId;
    String inventoryId;
    String shelfId;
    String userVoicePrint;
    int count;
    public FetchProductCommand(Event event, CommandProcessor storeModel, com.cscie97.ledger.CommandProcessor ledger,
                               String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
        this.customerId = event.getCustomerId();
        this.customerLocation = event.getCustomerLocation();
        this.productId = event.getProductId();
        this.inventoryId = event.getInventoryId();
        this.shelfId = event.getShelfId();
        this.count = event.getCount();
        this.userVoicePrint = event.getUserVoicePrint();
    }

    /**
     * Sends a robot to fetch a product in response to a voice command from a customer.
     */
    @Override
    public void execute() {
        // Implement authService actions to verify the user has robot control permissions
        AuthenticationService authService = AuthenticationService.getInstance();
        AuthToken userTokenId = null;
        try {
            userTokenId = authService.authenticateUser(userVoicePrint, CredentialType.VOICE_PRINT);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return;
        }

        String robotId = storeModel.processCommand("get-available-robot " + event.getStoreId(), controllerTokenId);
        String fetch = String.format("fetch %d of %s from %s and bring to customer %s in aisle %s",
                count, productId, shelfId, customerId, customerLocation);
        storeModel.processCommand(String.format("create-command %s message fetch(%s)",
                robotId, fetch), userTokenId.getId());
    }
}
