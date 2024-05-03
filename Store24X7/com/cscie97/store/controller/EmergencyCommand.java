package com.cscie97.store.controller;

import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.Event;

/**
 * Emergency command in response to emergency events. Opens all turnstiles, broadcasts a message from all speakers,
 * assigns one robot to handle the emergency, and commands the rest of the robots to assist customers to leave.
 */
public class EmergencyCommand extends Command {
    String[] turnstiles;
    String[] speakers;
    String[] robots;
    public EmergencyCommand(Event event, CommandProcessor storeModel, com.cscie97.ledger.CommandProcessor ledger, String controllerTokenId) {
        super(event, storeModel, ledger, controllerTokenId);
    }

    @Override
    public void execute() {
        String str = String.format("get-appliance-list %s ", event.getStoreId());
        robots = storeModel.processCommand(str + "robot", controllerTokenId).strip().split(" ");
        speakers = storeModel.processCommand(str + "speaker", controllerTokenId).strip().split(" ");
        turnstiles = storeModel.processCommand(str + "turnstile" ,controllerTokenId).strip().split(" ");

        // Open all turnstiles
        for (String turnstileId : turnstiles) {
            storeModel.processCommand(String.format("create-command %s message open", turnstileId), controllerTokenId);
        }

        // Broadcast emergency message
        String message = String.format("There is a %s in %s, please leave %s immediately.",
                event.getEmergency(), event.getAisleId(), event.getStoreId());
        for (String speakerId : speakers) {
            storeModel.processCommand(String.format("create-command %s message announce(%s)",
                    speakerId, message), controllerTokenId);
        }

        // Send robot 1 to address the emergency
        String action = String.format("address %s in %s", event.getEmergency(), event.getAisleId());
        storeModel.processCommand(String.format("create-command %s message %s",
                        robots[0], action), controllerTokenId);

        // Send the rest of the robots to assist customers leaving the store
        action = String.format("assist customers leaving the %s", event.getStoreId());
        for (int i = 1; i < robots.length; ++i) {
            storeModel.processCommand(String.format("create-command %s message %s",
                    robots[i], action), controllerTokenId);
        }
    }
}
