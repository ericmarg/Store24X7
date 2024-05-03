package com.cscie97.store.controller;

import com.cscie97.store.authentication.*;
import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Checks a customer out of the store. Automatically bills their blockchain account, opens the turnstile, and issues
 * a goodbye message.
 */
public class CheckoutCommand extends Command {
    public CheckoutCommand(Event event, CommandProcessor storeModel,
                           com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    @Override
    public void execute() {
        // Authenticate user and verify checkout privilege
        AuthenticationService authService = AuthenticationService.getInstance();
        try {
            AuthToken userAuthToken = authService.authenticateUser(event.getUserFacePrint(), CredentialType.FACE_PRINT);
            authService.checkAccess(userAuthToken.getId(), "checkout", event.getStoreId());
        } catch (AuthenticationException | AccessDeniedException e) {
            System.out.println(e.getMessage());
            String alert = "Checkout denied. Only registered users may checkout.";
            storeModel.processCommand(String.format("create-command %s message announce(%s)", event.getDeviceId(),
                    alert), controllerTokenId);
            return;
        }

        String customerId = event.getCustomerId();
        String basketId = storeModel.processCommand("get-customer-basket " + customerId, controllerTokenId);
        String basketCost = storeModel.processCommand("get-basket-cost " + basketId, controllerTokenId);
        // This method of generating a transaction ID is bad because it doesn't guarantee uniqueness
        String transactionId = event.getAccountAddress() + basketId + basketCost;
        String note = String.format("Thank you for shopping at %s!", event.getStoreId());

        // Create transaction
        String transaction = String.format("process-transaction %s amount %s fee %s note %s payer %s receiver %s",
                transactionId, basketCost, "10", note, event.getAccountAddress(), "Store24X7");

        // Submit transaction to blockchain
        ledger.processCommand(transaction, 1);

        // Open turnstile
        storeModel.processCommand(String.format("create-command %s message open", event.getDeviceId()), controllerTokenId);

        // Send goodbye message
        String goodbye = String.format("Goodbye %s, thanks for shopping at %s!",
                event.getFirstName(), event.getStoreId());
        storeModel.processCommand(String.format("create-command %s message speak(%s)",
                event.getDeviceId(), goodbye), controllerTokenId);

        // Log the user out
        authService.logout(event.getUserFacePrint(), CredentialType.FACE_PRINT);
    }
}
