package com.cscie97.ledger;

import java.io.Serializable;

/**
 * Associates a unique account ID (the address) with its balance.
 */
public class Account implements Serializable {
    private final String address;
    private int balance;
    public Account(String address, int balance) {
        this.address = address;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }
}
