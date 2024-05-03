package com.cscie97.store.controller;

import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Checks the customer's blockchain account balance in response to a voice query and issues the response message
 * to a speaker in the same aisle.
 */
public class CheckBalanceCommand extends Command {
    public CheckBalanceCommand(Event event, CommandProcessor storeModel,
                               com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    @Override
    public void execute() {
        String customerId = event.getCustomerId();
        String basketId = storeModel.processCommand("get-customer-basket " + customerId, controllerTokenId);
        int basketCost = Integer.parseInt(storeModel.processCommand("get-basket-cost " + basketId,controllerTokenId));

        // Query customer balance
        int balance = ledger.processCommand(String.format("get-account-balance %s",
                event.getAccountAddress()), 1);

        // Construct response message
        String moreOrLess = (basketCost > balance) ? "more" : "less";
        String response = String.format("Total value of basket items is %d which is %s than your account balance of %d",
                basketCost, moreOrLess, balance);

        // Respond using nearest speaker
        String speakerId = storeModel.processCommand("get-nearest-speaker " + event.getSpeakerLocation(), controllerTokenId);
        storeModel.processCommand(String.format("create-command %s message announce(%s)", speakerId, response), controllerTokenId);
    }
}
