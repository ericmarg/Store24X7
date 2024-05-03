package com.cscie97.ledger;

import com.cscie97.ledger.Ledger.LedgerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CommandProcessor processes input commands.
 * Commands are strings read from the command line or from a file.
 * Responsible for creating a ledger and calling ledger methods based on command input.
 */
public class CommandProcessor {
    private Ledger ledger = null;

    public CommandProcessor() {
    }

    /**
     * Processes commands and calls appropriate ledger methods to carry out command function.
     * Displays successful command returns (if any) to std out.
     * Catches LedgerExceptions and throws CommandProcessorExceptions on invalid inputs.
     *
     * @param command    the single-line command to be processed
     * @param lineNumber the line number of the command if it is being read from a file. 0 if read from the CLI.
     * @return
     */
    public int processCommand(String command, int lineNumber) {
        System.out.println("processing command: " + command);
        String[] commandList = command.split(" ", 2);
        // The first substring must be the command.
        switch (commandList[0]) {
            case "create-ledger":
                // create-ledger <name> description <description> seed <seed>
                String[] ledgerKeyWords = {"create-ledger", "description", "seed"};

                // Parse the string
                HashMap<String, String> map = commandParser(command, ledgerKeyWords);

                String name = map.get("create-ledger");
                String description = map.get("description");
                String seed = map.get("seed");

                if (ledger != null) {
                    // CommandProcessor and Ledger have a 1:1 relationship. Throw exception if a ledger already exists.
                    throw new CommandProcessorException("create-ledger", "A ledger already exists.", lineNumber);
                }

                ledger = new Ledger(name, description, seed);
                break;

            case "create-account":
               // Create a new account. Account names must be unique; catch a LedgerException if the name is already in use.
               try {
                   Account account = ledger.createAccount(commandList[1]);
                   System.out.println("Created account " + account.getAddress());
               } catch (LedgerException e) {
                   System.out.println("***ERROR*** \n" + e.getAction());
                   System.out.println(e.getReason() + "\n***********");
               }
                break;

            case "get-account-balance":
                /*
                 get-account-balance <account-id>
                 Get the account balance. Catch a LedgerException if the account does not exist in the ledger
                 (an account may have been created but does not exist in the ledger until its block has been added to the blockchain).
                */
                try {
                    int balance = ledger.getAccountBalance(commandList[1]);
                    System.out.println(commandList[1] + ": balance = " + balance);
                    return balance;
                } catch (LedgerException e) {
                    System.out.println("***ERROR*** \n" + e.getAction());
                    System.out.println(e.getReason() + "\n***********");
                }
                break;

            case "process-transaction":
                // process-transaction <transaction-id> amount <amount> fee <fee> note <note> payer <account-address> receiver <account-address>
                String[] transactionKeyWords = {"process-transaction", "amount", "fee", "note", "payer", "receiver"};
                HashMap<String, String> transactionMap = commandParser(command, transactionKeyWords);

                int amount = Integer.parseInt(transactionMap.get("amount")), fee = Integer.parseInt(transactionMap.get("fee"));
                String transactionId = transactionMap.get("process-transaction"), note = transactionMap.get("note"),
                       payerAddress = transactionMap.get("payer"), receiverAddress = transactionMap.get("receiver");

                // transactionIDs must be unique. Throw an exception if a transaction exists with the same ID.
                if (ledger.getTransaction(transactionId) != null) {
                    throw new CommandProcessorException("process-transaction", "Transaction " + transactionId + " already exists.", lineNumber);
                }

                // Instantiate accounts using the candidate block's account balance map because the accounts may not have
                // been added to the ledger yet (especially problematic for the first TRANSACTION_PER_BLOCK transactions).
                Account payer = ledger.candidateBlock.accountBalanceMap.get(payerAddress);
                Account receiver = ledger.candidateBlock.accountBalanceMap.get(receiverAddress);
                Transaction transaction = new Transaction(transactionId, amount, fee, note, payer, receiver);
                try {
                    // Try to add the transaction to the candidate block. The transaction is validated by the ledger.
                    String transactionID = ledger.processTransaction(transaction);
                    System.out.println("Transaction " + transactionID + " successful");
                } catch (LedgerException e) {
                    // Transaction was invalid
                    System.out.println("***ERROR*** \n" + e.getAction());
                    System.out.println(e.getReason() + "\n***********");
                }
                break;

            case "get-block":
                // get-block <blockNumber>
                // Attempt to retrieve the specified block and display its fields to std out.
                Block block = ledger.getBlock(Integer.parseInt(commandList[1]));
                if (block == null) {
                    System.out.println("Block " + commandList[1] + " does not exist");
                    break;
                }

                System.out.println("Block Number: " + block.getBlockNumber());
                System.out.println("Previous Hash: " + block.getPreviousHash());
                System.out.println("Hash: " + block.getHash());

                System.out.println("Transactions:");
                for (Transaction t : block.transactionList) {
                    System.out.println("\tTransaction ID: " + t.getTransactionId());
                    System.out.println("\tAmount: " + t.getAmount());
                    System.out.println("\tFee: " + t.getFee());
                    System.out.println("\tNote: " + t.getNote());
                    System.out.println("\tPayer: " + t.getPayer().getAddress());
                    System.out.println("\tReceiver: " + t.getReceiver().getAddress() + "\n");
                }

                System.out.println("Account Balances:");
                for (Map.Entry<String, Account> entry : block.accountBalanceMap.entrySet()) {
                    Account account = entry.getValue();
                    System.out.println("\tAccount Address: " + account.getAddress() + ", Balance: " + account.getBalance());
                }

                System.out.print("Previous Block: ");
                if (block.previousBlock == null) {
                    // The genesis block has no previous block.
                    System.out.println("Genesis Block");
                } else {
                    System.out.println(block.previousBlock.getBlockNumber());
                }
                break;

            case "get-account-balances":
                // Retrieve the most recently completed block's map of account balances, if one exists.
                // Display all account names and their balances to std out.
                try {
                    Map<String, Account> accountBalanceMap = ledger.getAccountBalances();
                    System.out.println("Account Balances:");
                    for (Map.Entry<String, Account> entry : accountBalanceMap.entrySet()) {
                        Account account = entry.getValue();
                        System.out.println("Account Address: " + account.getAddress() + ", Balance: " + account.getBalance());
                    }
                } catch (LedgerException e) {
                    System.out.println(e.getReason());
                }

                break;

            case "validate":
                /*
                 Validate the blockchain.
                 Ensure all account balances add up to master account starting balance, all blocks
                 have exactly <transactions per block> transactions, and check that each block's previousHash
                 field is consistent with a re-computed hash of the block's previousBlock.
                */
                try {
                    ledger.validate();
                } catch (LedgerException e) {
                    System.out.println("***ERROR*** \n" + e.getAction());
                    System.out.println(e.getReason() + "\n***********");
                }
                break;

            case "get-transaction":
                // get-transaction <transactionId>
                // Attempt to retrieve the specified transaction. Display the details to std out.
                Transaction trans = ledger.getTransaction(commandList[1]);
                if (trans == null) {
                    System.out.println("Transaction not found.");
                } else {
                    System.out.println("Transaction ID: " + trans.getTransactionId());
                    System.out.println("\tAmount: " + trans.getAmount());
                    System.out.println("\tFee: " + trans.getFee());
                    System.out.println("\tNote: " + trans.getNote());
                    System.out.println("\tPayer: " + trans.getPayer().getAddress());
                    System.out.println("\tReceiver: " + trans.getReceiver().getAddress());
                }
                break;

            case "get-blocks":
                // Display the number of blocks that have been committed to the blockchain.
                System.out.println(ledger.getBlocks());
                break;

            default:
                System.out.println("Invalid command: " + commandList[0]);
        }
        return 0;
    }

    /**
     * Reads lines from a given file object. Lines starting with a hash symbol are considered comments
     * and will not be sent to the processCommand method.
     * @param file  the script file containing one or many commands separated by newlines.
     */
    public void processCommandFile(File file) {
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int lineNumber = 0;
        // Read through the entire file
        while (reader.hasNextLine()) {
            lineNumber++;
            String data = reader.nextLine();

            // Skip empty lines or comments (as denoted by a hash(#) symbol)
            if (data.isEmpty() || data.charAt(0) == '#') {
                if (data.charAt(0) == '#') {
                    // Print comments for output readability
                    System.out.println(data);
                }
                continue;
            }

            try {
                // Send commands to processCommand method to be interpreted and acted on.
                processCommand(data, lineNumber);
            } catch (CommandProcessorException e) {
                System.out.println("***ERROR*** \n" + e.getCommand());
                System.out.println(e.getReason());
                System.out.println("Line number: " + e.getLineNumber() + "\n***********");
            }
        }
        reader.close();
    }

    /**
     * Parses the given command string. Splits the command into substrings based on the provided command
     * keywords to separate out the keywords from the variable strings.
     * @param command   The single-line command string read from the CLI or script file.
     * @param keyWords  The keywords associated with the command. If the command has the structure
     *                  create-ledger <name> description <description> seed <seed>, the angle brackets are the
     *                  variable strings and the keywords are 'create-ledger', 'description', and 'seed'.
     * @return          A hashmap of paired strings where the key is the keyword and the value is the variable string.
     */
    public HashMap<String, String> commandParser(String command, String[] keyWords) {
        int length = keyWords.length;
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < length; i++) {
            // Split the command string into a list of substrings separated by the keyword
            String[] substrings = command.split(keyWords[i], 2);
            if (i == 0) {
                // The command string starts with the first keyword; after removing it, continue
                command = substrings[1];
                continue;
            }
            // substrings[0] contains the input for the previous keyword; substrings[1] contains the remaining string
            String str = substrings[0].trim().replaceAll("[\"“”]", "");
            command = substrings[1];
            // Add keyword and associated input to map
            map.put(keyWords[i-1], str);
        }
        // Add the last word pair to the list.
        map.put(keyWords[length - 1], command.trim().replaceAll("[\"“”]", ""));
        return map;
    }

    /**
     * Throwable exception for invalid commands.
     * Thrown by CommandProcessor.
     */
    public static class CommandProcessorException extends RuntimeException {
        private final String command;
        private final String reason;
        private final int lineNumber;
        public CommandProcessorException(String command, String reason, int lineNumber) {
            super("Ledger Exception: " + command + " - " + reason);
            this.lineNumber = lineNumber;
            this.command = command;
            this.reason = reason;
        }

        public String getCommand() {
            return command;
        }

        public String getReason() {
            return reason;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }
}
