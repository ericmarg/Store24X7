package com.cscie97.store.controller;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;
import com.cscie97.store.observer.Observer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Listens for Events from the StoreModelService. When the StoreObserver is notified of an event, it uses
 * a rule-based response process to issue a command to the appliances in the store to address the event.
 */
public class StoreObserver implements Observer {
    private static StoreObserver storeObserver = null;
    CommandProcessor SMSCommandProcessor;
    com.cscie97.ledger.CommandProcessor ledgerCommandProcessor;
    private AuthToken authToken;
    // Create a map of event types matched with the name of the command subclass that should respond
    Map<String, String> eventResponse = new HashMap<String, String>() {{
        put("emergency", "com.cscie97.store.controller.EmergencyCommand");
        put("enter-store", "com.cscie97.store.controller.EnterStoreCommand");
        put("basket-event", "com.cscie97.store.controller.BasketEventCommand");
        put("fetch-product", "com.cscie97.store.controller.FetchProductCommand");
        put("customer-seen", "com.cscie97.store.controller.CustomerSeenCommand");
        put("broken-glass", "com.cscie97.store.controller.BrokenGlassCommand");
        put("product-spill", "com.cscie97.store.controller.ProductSpillCommand");
        put("missing-person", "com.cscie97.store.controller.MissingPersonCommand");
        put("check-acc-bal", "com.cscie97.store.controller.CheckBalanceCommand");
        put("assist-customer", "com.cscie97.store.controller.AssistCustomerCommand");
        put("checkout", "com.cscie97.store.controller.CheckoutCommand");
    }};

    // private constructor to avoid client applications using the constructor
    private StoreObserver () {}

    // getInstance() method adapted from wikipedia https://en.wikipedia.org/wiki/Singleton_pattern
    public static StoreObserver getInstance() {
        if (storeObserver == null) {
            synchronized(StoreObserver.class) {
                if (storeObserver == null) {
                    storeObserver = new StoreObserver();
                }
            }
        }
        return storeObserver;
    }

    public void setSMSCommandProcessor(CommandProcessor SMSCommandProcessor) {
        this.SMSCommandProcessor = SMSCommandProcessor;
    }

    public void setLedgerCommandProcessor(com.cscie97.ledger.CommandProcessor ledgerCommandProcessor) {
        this.ledgerCommandProcessor = ledgerCommandProcessor;
    }

    public Command createCommand(Event event) throws CommandCreationException {
        Command command = null;
        String commandTypeString;
        try {
            // Use the java reflection API to create an instance of the required Command subclass
            commandTypeString = eventResponse.get(event.getCommandClass());
            Class<?> commandType = Class.forName(commandTypeString);
            Constructor<?> constructor = commandType.getConstructor(Event.class, CommandProcessor.class,
                    com.cscie97.ledger.CommandProcessor.class, String.class);
            command = (Command) constructor.newInstance(event, SMSCommandProcessor, ledgerCommandProcessor, authToken.getId());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException | NullPointerException e) {
            // Handle any exceptions that might occur during class instantiation
            throw new CommandCreationException("Command creation failed", event);
        }
        System.out.println("--Created command: " + commandTypeString + " in response to event: " + event.getCommandClass());
        return command;
    }

    /**
     * Observer pattern method called when the StoreModelService creates an Event message.
     * Creates and executes a concrete Command in response.
     * @param event     A concrete instance of the Event class representing a store event.
     */
    @Override
    public void update(Event event) {
        Command command = null;
        try {
            command = createCommand(event);
            command.execute();
        } catch (CommandCreationException e) {
            System.out.println("***ERROR*** \n" + e.getAction());
            System.out.println("Event: " + e.getEvent() + "\n***********");
        }
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
