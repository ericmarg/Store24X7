package com.cscie97.ledger.test;

import com.cscie97.ledger.CommandProcessor;

import java.io.File;
import java.util.Arrays;

/**
 * Test driver class to test implementation.
 * Takes a single parameter, a command string or a path to a command file
 * and passes it to a CommandProcessor instance.
 */
public class TestDriver {
    public static void main(String[] args) {
        String input = args[0];
        CommandProcessor commandProcessor = new CommandProcessor();
        // Create a new file object to separate file path inputs from command inputs.
        File file = new File(input);
        if (file.isFile()) {
            commandProcessor.processCommandFile(file);
        } else {
            commandProcessor.processCommand(Arrays.toString(args), 0);
        }
    }
}
