package com.cscie97.ledger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Processes transactions, maintains account balances, and manages the blockchain. Provides validation
 * to verify that transactions and accounts in the blockchain have not been altered.
 */
public class Ledger {
    // Candidate blocks are committed to the chain when they reach this number of transactions
    public static final int TRANSACTIONS_PER_BLOCK = 10;
    public static final int MIN_TRANSACTION_FEE = 10; // Transaction fee is paid by transaction payer to MASTER
    public static final String MASTER = "master"; // Master account associated with ledger
    private final String name;
    private final String seed;
    private final String description;
    private final Block genesisBlock;
    private final Map<Integer, Block> blockMap = new HashMap<>();
    Block candidateBlock;
    public Ledger(String name, String description, String seed) {
        this.name = name;
        this.description = description;
        this.seed = seed;
        genesisBlock = new Block(1,"", "", new ArrayList<>(), new HashMap<>(),null);
        candidateBlock = genesisBlock;
        Account master = this.createAccount(MASTER);
        genesisBlock.accountBalanceMap.put(MASTER, master);
        master.setBalance(Integer.MAX_VALUE);
    }

    public String getName() {
        return name;
    }

    public String getSeed() {
        return seed;
    }

    public String getDescription() {
        return description;
    }

    public Block getBlock(int blockNumber) {
        return blockMap.get(blockNumber);
    }

    /**
     * Creates a new Account object using the given account ID.
     * The initial account balance is 0. The account ID must be unique.
     * @param accountId string value representing unique account ID.
     * @return          the newly created Account instance.
     */
    public Account createAccount(String accountId) {
        // if account id is already in use throw an exception
        if (candidateBlock.accountBalanceMap.containsKey(accountId)) {
            throw new LedgerException("create account", "Could not create account " +
                    accountId + "; account already exists.");
        }

        Account account = new Account(accountId, 0);
        candidateBlock.accountBalanceMap.put(accountId, account);
        return account;
    }

    /**
     * Processes a transaction. Verifies that the transaction is valid and throws a LedgerException if it is not.
     * Valid transactions have valid payer and receiver IDs, an amount greater than zero, and a fee that is
     * greater than or equal to the minimum fee value. They may optionally include a note string.
     * @param transaction   The requested transaction.
     * @return              The transaction ID of the transaction, if successful.
     */
    public String processTransaction(Transaction transaction) {
        // Validate transaction amount > 0 and fee > minimum fee
        if (transaction.getFee() < MIN_TRANSACTION_FEE) {
            throw new LedgerException("process transaction", "Fee too low; minimum transaction fee = " + MIN_TRANSACTION_FEE);
        }
        if (transaction.getAmount() <= 0) {
            throw new LedgerException("process transaction", "Transaction amount must be greater than zero.");
        }

        Account payer = transaction.getPayer(), receiver = transaction.getReceiver(),
                master = this.candidateBlock.accountBalanceMap.get(MASTER);
        // Validate payer and receiver accounts exist
        if (payer == null || receiver == null) {
            throw new LedgerException("process transaction", "Invalid payer or receiver");
        }
        // Verify payer has high enough balance to cover transaction
        if (payer.getBalance() < transaction.getAmount() + transaction.getFee()) {
            throw new LedgerException("process transaction", "Payer balance too low.");
        }

        int amount = transaction.getAmount(), fee = transaction.getFee();
        // Update the account balances
        payer.setBalance(payer.getBalance() - amount - fee);
        receiver.setBalance(receiver.getBalance() + amount);
        master.setBalance(master.getBalance() + fee);

        // Add the transaction to the candidate block.
        // If the candidate block has reached the correct number of transactions, add it to the blockchain
        candidateBlock.add(transaction);
        if (candidateBlock.transactionList.size() == TRANSACTIONS_PER_BLOCK) {
            // Compute and set the candidate block's hash
            String hash = candidateBlock.hashBlock(this.getSeed());
            candidateBlock.setHash(hash);

            // Add candidate block to the block map
            blockMap.put(candidateBlock.getBlockNumber(), candidateBlock);

            // Create a new candidate block
            candidateBlock = new Block(candidateBlock.getBlockNumber() + 1, candidateBlock.getHash(),
                             "", new ArrayList<>(), candidateBlock.accountBalanceMap, candidateBlock);
        }

        return transaction.getTransactionId();
    }

    /**
     * Gets the account balance of the specified account. Raises a LedgerException if the account does not exist.
     * The account may exist, but the block whose accountBalanceMap it was added to may not have been added to the
     * blockchain yet. In such cases, a LedgerException will be thrown as if the account does not exist.
     * @param address   The unique account ID.
     * @return          The account's balance.
     */
    public int getAccountBalance(String address) {
        // No accounts have been added to the ledger yet if the genesis block has not been filled with transactions.
        if (candidateBlock.getBlockNumber() == 1) {
            throw new LedgerException("get account balance", "Account " + address +
                    " has not yet been added to the ledger.");
        }

        Account account = candidateBlock.previousBlock.accountBalanceMap.get(address);
        if (account == null) {
            // Account was not found
            throw new LedgerException("get account balance", "Account " + address +
                    " does not exist or has not been added to the ledger yet.");
        } else {
            return account.getBalance();
        }
    }

    /**
     * Retrieves the account balance map of the most recently completed block.
     * @return  The map of account IDs and associated accounts.
     */
    public Map<String, Account> getAccountBalances() {
        // Return the account balance map of the most recently completed block.
        if (candidateBlock == genesisBlock) {
            // Accounts are only added to the ledger when blocks are added to the chain.
            throw new LedgerException("get account balances",
                    "Cannot return account balance map; no blocks have been committed to the blockchain.");
        } else {
            return candidateBlock.previousBlock.accountBalanceMap;
        }
    }

    /**
     * Retrieves the specified transaction. Returns it to be displayed.
     * @param transactionId The unique ID of the queried transaction.
     * @return              The specified Transaction object, or null if it wasn't found.
     */
    public Transaction getTransaction(String transactionId) {
        // Iterate through the block map - there is no block 0.
        for (int i = blockMap.size(); i > 0; i--) {
            Block block = blockMap.get(i);
            // Iterate through the block's transactionList to find the transaction.
            for (Transaction transaction : block.transactionList) {
                // Return the transaction if found.
                if (Objects.equals(transaction.getTransactionId(), transactionId)) {
                    return  transaction;
                }
            }
        }
        // The transaction was not found.
        return null;
    }

    /**
     * Retrieve the number of blocks in the blockchain.
     * @return  the number of blocks.
     */
    public int getBlocks() {
        return blockMap.size();
    }

    /**
     * Validates the state of the blockchain. Verifies that the account balances and transactions
     * of the committed block have not been altered by re-computing the hash of each block's previousBlock
     * and comparing the hash to the previousHash field. Verifies that all account balances are greater
     * than or equal to zero, and they sum to the master account's starting balance.
     * Throws a LedgerException if the blockchain is invalid.
     */
    public void validate() {
        // Validate the blockchain block by block
        for (int i = 1; i <= blockMap.size(); i++) {
            Block block = blockMap.get(i);

            // Check for correct number of transactions per block
            if (block.transactionList.size() != TRANSACTIONS_PER_BLOCK) {
                throw new LedgerException("Validation error", "Block " + block.getBlockNumber() +
                        " has an invalid number of transactions.");
            }

            // Check that all account balances (including master's) sum to Integer.MAX_VALUE (master's starting balance)
            int sum = 0;
            for (Map.Entry<String, Account> entry : block.accountBalanceMap.entrySet()) {
                if (entry.getValue().getBalance() < 0) {
                    throw new LedgerException("validation error", entry.getKey() + " has negative balance.");
                }
                sum += entry.getValue().getBalance();
            }
            if (sum != Integer.MAX_VALUE) {
                throw new LedgerException("Validation error", "Block " + block.getBlockNumber() +
                        " has invalid account balances.");
            }

            if (block.getBlockNumber() != 1) {  // Genesis block has block number 1, there is no 0th block
                // Check previous hash is correct
                String prevHash = block.previousBlock.hashBlock(this.getSeed());
                if (!prevHash.equals(block.getPreviousHash())) {
                    // A hash mismatch indicates that the block was altered after it was added to the chain.
                    throw new LedgerException("Validation error", "Block " + block.getBlockNumber() +
                            " has an incorrect previous hash.");
                }
            }
        }
    }

    /**
     * Throwable exception returned in response to errors in Ledger API.
     * Thrown by Ledger.
     */
    public static class LedgerException extends RuntimeException {
        private final String action;
        private final String reason;
        public LedgerException(String action, String reason) {
            super("Ledger Exception: " + action + " - " + reason);
            this.action = action;
            this.reason = reason;
        }

        public String getAction() {
            return action;
        }

        public String getReason() {
            return reason;
        }
    }
}
