package com.cscie97.store.authentication;

/**
 * Interface to provide visitor Pattern functionality
 * Allows a Visitor to call objects implementing this interface
 */
public interface Visitable {
    public abstract void acceptVisitor(Visitor v);
}
