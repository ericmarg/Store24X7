package com.cscie97.store.test;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.CredentialType;
import com.cscie97.store.controller.StoreObserver;
import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.StoreModelService;

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
        System.out.println(Arrays.toString(args));

        // Create administrative user for running the command script
        System.out.println("**Creating an administrative user to run test script**");
        AuthenticationService authService = AuthenticationService.getInstance();
        authService.createUser("admin", "Admin");
        authService.createPermission("user_admin", "User Administrator", "Create, Update, Delete Users");
        authService.addUserPermission("admin", "user_admin");
        authService.createRole("admin_role", "Admin Role", "Full administrative privileges");
        authService.addRolePermission("admin_role", "user_admin");
        authService.addUserRole("admin", "admin_role");
        try {
            authService.addUserCredential("admin", CredentialType.PASSWORD, "password");
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
        AuthToken authToken = authService.login("admin", "password");
        assert (authToken != null);

        StoreModelService storeModelService = StoreModelService.getInstance();

        // Create command processor instances
        CommandProcessor SMScommandProcessor = new CommandProcessor();
        com.cscie97.ledger.CommandProcessor ledgerCP = new com.cscie97.ledger.CommandProcessor();

        // Create StoreObserver instance and set pointers
        StoreObserver storeObserver = StoreObserver.getInstance();
        storeObserver.setSMSCommandProcessor(SMScommandProcessor);
        storeObserver.setLedgerCommandProcessor(ledgerCP);
        storeObserver.setAuthToken(authToken);

        // Add storeObserver to storeModelService observers
        storeModelService.registerObserver(storeObserver);

        // Hand SMS the ledger API and create a new ledger
        storeModelService.setLedgerAPI(ledgerCP);
        storeModelService.createLedger();

        // Create a new file object to separate file path inputs from command inputs.
        File file = new File(input);
        if (file.isFile()) {
            SMScommandProcessor.processCommandFile(file, authToken.getId());
        } else {
            SMScommandProcessor.processCommand(Arrays.toString(args), authToken.getId());
        }
    }
}