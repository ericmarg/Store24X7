package com.cscie97.store.observer;

import com.cscie97.store.model.Event;

/**
 * Observers listen for event notices from Observables.
 */
public interface Observer {
    public void update (Event event);
}
