package com.cscie97.store.controller;

import com.cscie97.store.model.Event;

/**
 * Exception class thrown when a command could not be created. Provides the action the caused the exception
 * and the event object that sparked the command creation.
 */
public class CommandCreationException extends Throwable {
    private final String action;
    private final Event event;
    public CommandCreationException(String action, Event event) {
        this.action = action;
        this.event = event;
    }

    public String getAction() {
        return action;
    }

    public Event getEvent() {
        return event;
    }

}
