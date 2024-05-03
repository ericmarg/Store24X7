package com.cscie97.ledger;

import java.io.Serializable;

/**
 * Represents transactions submitted to the blockchain. Transactions must have an amount greater than zero,
 * a fee greater than or equal to the minimum fee, valid payer and receiver accounts, a unique transaction ID,
 * and optionally may include a note.
 */
public class Transaction implements Serializable{
    private final Account payer;
    private final Account receiver;
    private final String transactionId;
    private final int amount;
    private final int fee;
    private final String note;
    public Transaction (String transactionId, int amount, int fee, String note, Account payer, Account receiver) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.fee = fee;
        this.note = note;
        this.payer = payer;
        this.receiver = receiver;
    }

    public Account getPayer() {
        return payer;
    }

    public Account getReceiver() {
        return receiver;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public int getAmount() {
        return amount;
    }

    public int getFee() {
        return fee;
    }

    public String getNote() {
        return note;
    }

}
