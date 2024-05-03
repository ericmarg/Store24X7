package com.cscie97.store.model;

/**
 * Simple factory method that creates a concrete class of type Event. Events are sent to the StoreControllerService.
 */
public class SimpleEventFactory {
    public Event createEvent(String deviceId, String storeId, String[] eventArgs) {
        Event event = null;

        switch (eventArgs[0]) {
            case "emergency":
                    event = new EmergencyEvent(deviceId, storeId, eventArgs);
                break;
            case "basket-event":
                    event = new BasketEvent(deviceId, storeId, eventArgs);
                break;
            case "fetch-product":
                    event = new FetchProductEvent(deviceId, storeId, eventArgs);
                break;
            case "broken-glass":
                    event = new BrokenGlassEvent(deviceId, storeId, eventArgs);
                break;
            case "product-spill":
                    event = new SpilledProductEvent(deviceId, storeId, eventArgs);
                break;
            case "customer-seen":
                    event = new CustomerSeenEvent(deviceId, storeId, eventArgs);
                break;
            case "enter-store":
                    event = new EnterStoreEvent(deviceId, storeId, eventArgs);
                break;
            case "missing-person":
                    event = new MissingPersonEvent(deviceId, storeId, eventArgs);
                break;
            case "check-acc-bal":
                    event = new CheckAccBalEvent(deviceId, storeId, eventArgs);
                break;
            case "assist-customer":
                    event = new AssistCustomerToCarEvent(deviceId, storeId, eventArgs);
                break;
            case "checkout":
                    event = new CheckoutEvent(deviceId, storeId, eventArgs);
                break;
            default:
                System.out.println("Event not recognized: " + eventArgs[0]);
        }
        if (event != null) System.out.println("Created event " + eventArgs[0]);
        return event;
    }
}
