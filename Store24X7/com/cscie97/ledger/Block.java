package com.cscie97.ledger;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Fundamental unit of the blockchain. Blocks have a unique block number, contain a list of Transaction
 * objects of size <transactions per block>, the hash of the previous block in the blockchain, a pointer
 * to the previous block, and a map of account balances that represents the balances and accounts as they
 * were at the time the block reached its transaction limit and was added to the blockchain. Each block
 * is hashed when it is added to the chain and its hash field is updated, making the transactions and account
 * balances stored in the block immutable.
 */
public class Block implements Serializable {
    private final int blockNumber;
    private final String previousHash;
    private String hash;
    List<Transaction> transactionList;
    public Map<String, Account> accountBalanceMap;
    public final Block previousBlock;
    public Block (int blockNumber, String previousHash, String hash, List<Transaction> transactionList,
                  Map<String, Account> accountBalanceMap, Block previousBlock) {

        this.blockNumber = blockNumber;
        this.previousHash = previousHash;
        this.hash = hash;
        this.transactionList = transactionList;
        this.accountBalanceMap = new HashMap<>();
        // Create deep copy of accountBalanceMap so the previous block's map is not altered in future transactions
        for (Map.Entry<String, Account> entry : accountBalanceMap.entrySet()) {
            String address = entry.getKey();
            Account originalAccount = entry.getValue();
            Account copiedAccount = new Account(originalAccount.getAddress(), originalAccount.getBalance());
            this.accountBalanceMap.put(address, copiedAccount);
        }
        this.previousBlock = previousBlock;
    }

    void add (Transaction transaction) {
        transactionList.add(transaction);
    }
    void delete(Transaction transaction) {
        transactionList.remove(transaction);
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Hashes the block that calls it. The hash is formatted into a hex string.
     * The hash is computed using all the fields and associations of the block, and the seed
     * of the ledger. Because the previous block's hash is one of the block's fields, changes
     * made to any previous block will cascade along the chain.
     * @param seed  The seed used when the ledger was created.
     * @return      The hash string in hex format.
     */
    public String hashBlock(String seed) {
        List<String> hashList = new ArrayList<>();
        // Iterate through the transaction list and hash all the Transaction objects.
        for (Transaction transaction : this.transactionList) {
            hashList.add(computeHash(transaction));
        }

        // Get the Merkle hash of the transactions and the hashes for the other block fields.
        String transactionHash = merkleTree(hashList).toString();
        String accountHash = computeHash(this.accountBalanceMap);
        String prevBlockHash = computeHash(this.previousBlock);
        String seedHash = computeHash(seed);
        // Concatenate the hash strings and hash them all together.
        String combinedString = this.getBlockNumber() + this.getPreviousHash()
                + transactionHash + accountHash + prevBlockHash + seedHash;
        return computeHash(combinedString);
    }

    /**
     * Hashes the given object by serializing it. Uses the SHA-256 algorithm.
     * Helper function to hashBlock().
     * Referenced: https://www.tutorialspoint.com/java/java_serialization.htm,
     *             https://www.tutorialspoint.com/java_cryptography/java_cryptography_message_digest.htm
     * @param object    A serializable object.
     * @return          The hash of the object formatted as a hex string.
     */
    public <T> String computeHash(T object) {
        // Try to create the message digest object instance
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Serialize the object into a byte array using the helper function
        md.update(getByteArray(object));
        byte[] hash = md.digest();
        // format the hash as a hex string and return it
        HexFormat format = HexFormat.of();
        return format.formatHex(hash);
    }

    /**
     * Serializes the input object into a byte array.
     * Helper function to hashBlock().
     * Referenced: https://www.tutorialspoint.com/java/java_serialization.htm,
     *             https://www.tutorialspoint.com/java_cryptography/java_cryptography_message_digest.htm
     * @param object    Any serializable object.
     * @return          The object in byte array form.
     */
    public <T> byte[] getByteArray(T object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
            byte[] byteArray = bos.toByteArray();
            bos.close();
            return byteArray;
        } catch (java.io.IOException e) {
            System.out.println("Serialization error");
            System.exit(1);
            return null; // Should never reach here
        }
    }

    /**
     * Hashes transactions (that were already hashed and put into the hashesList) together in pairs where pairs
     * are adjacent list members. For example, hashesList[0] + hashesList[1] are concatenated and the resulting string
     * is then hashed again. This is repeated until all the list members have been hashed together into one root hash,
     * forming a Merkle tree.
     * Helper function to hashBlock().
     * Adapted from Vinay Prabhu's article on Merkel trees, Medium, 2019
     *   https://medium.com/@vinayprabhu19/merkel-tree-in-java-b45093c8c6bd#:~:text=Merkel%20Tree%20is%20built%20by,the%20transaction%20has%20been%20modified.
     * @param hashesList    A list of strings representing hashed Transaction objects.
     * @return              The root node hash of the Merkle tree.
     */

    public List<String> merkleTree(List<String> hashesList) {
        // Base case; the tree has been constructed so return the root node
        if (hashesList.size() == 1) {
            return hashesList;
        }

        // Concatenate pairs of adjacent hashes and hash the resulting string
        // After the loop executes the tree will have a new layer closer to the root
        List<String> combinedHashes = new ArrayList<>();
        for (int i = 0; i < hashesList.size(); i += 2) {
            try {
                String combinedHash = computeHash(hashesList.get(i) + hashesList.get(i + 1));
                combinedHashes.add(combinedHash);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        // When there are an odd number of hashes in the list we carry the odd hash out
        // on to the next layer in the tree to be concatenated and re-hashed later on
        if (hashesList.size() % 2 == 1) {
            combinedHashes.add(hashesList.get(hashesList.size() - 1));
        }

        // Recursively construct the Merkle tree
        return merkleTree(combinedHashes);
    }
}
