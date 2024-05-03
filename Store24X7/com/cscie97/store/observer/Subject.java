package com.cscie97.store.observer;

import com.cscie97.store.model.Event;

/**
 * Subjects broadcast event notices to observers. They also provide methods for registering and removing observers
 * to/from a data structure.
 */
public interface Subject {
    public void registerObserver (Observer observer);
    public void removeObserver (Observer observer);
    public void notifyObservers (Event event);
}
