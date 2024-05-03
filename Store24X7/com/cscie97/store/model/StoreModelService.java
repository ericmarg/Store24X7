package com.cscie97.store.model;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.observer.Observer;
import com.cscie97.store.observer.Subject;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The StoreModelService class is a Singleton that represents the top-level interface for managing
 * stores. The StoreModelService acts on commands from the Store Controller Service by way of
 * the CommandProcessor class. Acts as a Subject in the Observer pattern to notify the Store Controller
 * Service of events that occur within the store.
 */
public class StoreModelService implements Subject {
    private static final StoreModelService storeModelService = new StoreModelService();
    private final AuthenticationService authService = AuthenticationService.getInstance();
    private Map<String, Store> storeMap = new HashMap<>();
    private Map<String, Inventory> inventoryMap = new HashMap<>();
    private Map<String, Product> productMap = new HashMap<>();
    private Map<String, Customer> customerMap = new HashMap<>();
    private Map<String, Basket> basketMap = new HashMap<>();
    private Map<String, Sensor> sensorMap = new HashMap<>();
    private Map<String, Appliance> applianceMap = new HashMap<>();
    private SimpleEventFactory factory = new SimpleEventFactory();
    private List<Observer> observers = new ArrayList<>();
    private com.cscie97.ledger.CommandProcessor ledgerCP;


    // private constructor to avoid client applications using the constructor
    private StoreModelService() {
    }

    public static StoreModelService getInstance() {
        return storeModelService;
    }

    /**
     * Creates a new Store object. Throws an exception if there is already a store with that ID.
     *
     * @param storeId Unique store ID
     * @param name    Store name
     * @param address Store address
     */
    public void defineStore(String storeId, String name, String address, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Store store = storeMap.get(storeId);
        if (store == null) {
            store = new Store(storeId, name, address);
            storeMap.put(storeId, store);
            authService.createResource(storeId, address);
        } else {
            throw new StoreModelServiceException("define store", "Store already exists.");
        }

    }

    /**
     * Tries to return the requested Store. Throws an exception if the store was not found.
     *
     * @param storeId Unique ID
     * @return Store object
     */
    public Store getStore(String storeId) {
        Store store = storeMap.get(storeId);
        if (store == null) {
            throw new StoreModelServiceException("get store", "No such store");
        }
        return store;
    }

    /**
     * Display store details
     *
     * @param storeId Unique store ID
     */
    public void showStore(String storeId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        this.getStore(storeId).show();
    }

    /**
     * Creates a new Aisle object. Aisles are composed by Stores.
     *
     * @param id          Unique identifier
     * @param name        Aisle name
     * @param description Aisle description
     * @param location    Aisle location (floor or storeroom)
     */
    public void defineAisle(String id, String name, String description, String location, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        String storeId = id.split(":")[0];

        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        this.getStore(storeId).addAisle(id, name, description, location);
    }

    /**
     * Display aisle details.
     *
     * @param id Unique ID store:aisle
     */
    public void showAisle(String id, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        String storeId = id.split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        this.getStore(storeId).getAisle(id).show();
    }

    /**
     * Creates a new Shelf object.
     *
     * @param id          Unique ID store:aisle:shelf
     * @param name        Shelf name
     * @param level       Shelf level low-medium-high
     * @param description Shelf description
     * @param temperature Shelf temperature frozen-refrigerated-ambient-warm-hot
     */
    public void defineShelf(String id, String name, String level, String description, String temperature, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        String[] strings = id.split(":");
        String storeId = strings[0];
        String aisleId = strings[0] + ":" + strings[1];

        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Aisle aisle = this.getStore(storeId).getAisle(aisleId);
        aisle.addShelf(id, name, level, description, temperature);

    }

    /**
     * Display shelf details
     *
     * @param id Unique ID store:aisle:shelf
     */
    public void showShelf(String id, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        String[] strings = id.split(":");
        String storeId = strings[0];
        String aisleId = strings[0] + ":" + strings[1];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        this.getStore(storeId).getAisle(aisleId).getShelf(id).show();
    }

    /**
     * Creates a new Inventory object
     *
     * @param inventoryId Unique ID
     * @param location    Store:aisle:shelf
     * @param capacity    Maximum number of the product a shelf can hold
     * @param count       Current product count
     * @param productID   The unique ID of the product in this inventory
     */
    public void defineInventory(String inventoryId, String location, int capacity, int count, String productID, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("define inventory", "No auth token provided.");

        String storeId = location.split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (count < 0) {
            throw new StoreModelServiceException("define inventory",
                    "Count must be greater than or equal to zero.");
        }
        Inventory inventory = new Inventory(inventoryId, location, capacity, count, productID);
        inventoryMap.put(inventoryId, inventory);
    }

    /**
     * Show inventory details
     *
     * @param inventoryId Unique ID
     */
    public void showInventory(String inventoryId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("show inventory", "No auth token provided.");

        Inventory inventory = inventoryMap.get(inventoryId);
        if (inventory == null)
            throw new StoreModelServiceException("show inventory", "No inventory with that ID.");

        String storeId = inventory.getLocation().split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }
        inventory.show();

    }

    /**
     * Update the inventory's item count
     *
     * @param inventoryId Unique inventory ID
     * @param count       Number of units that were added or removed
     */
    public void updateInventory(String inventoryId, int count, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        Inventory inventory = inventoryMap.get(inventoryId);
        if (inventory == null) {
            throw new StoreModelServiceException("update inventory", "Inventory does not exist: " + inventoryId);
        }
        String storeId = inventory.getLocation().split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }
        inventory.updateCount(count);


    }

    /**
     * Creates a new Product object
     *
     * @param productId   Unique product ID
     * @param name        product name
     * @param description Product description
     * @param size        Size in unspecified units
     * @param category    Product category
     * @param unitPrice   Unit price of the product
     * @param temperature Temperature the product should be stored at frozen-refrigerated-ambient-warm-hot
     */
    public void defineProduct(String productId, String name, String description, String size, String category,
                              int unitPrice, String temperature, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (unitPrice <= 0) {
            throw new StoreModelServiceException("define product",
                    "Unit price must be greater than zero.");
        }
        Product product = new Product(productId, name, description, size, category, unitPrice, temperature);
        productMap.put(productId, product);
    }

    /**
     * Display product details
     *
     * @param productId Unique ID
     */
    public void showProduct(String productId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        productMap.get(productId).show();
    }

    /**
     * Defines a new Customer object which represents a shopper within a store.
     *
     * @param customerId     Unique ID number
     * @param firstName      Customer's firstname
     * @param lastName       Customer's lastname
     * @param isRegistered   Is the customer registered or not
     * @param isAdult        Is the customer an adult
     * @param emailAddress   Customer email address
     * @param accountAddress Customer blockchain account address
     */
    public void defineCustomer(String customerId, String firstName, String lastName,
                               Boolean isRegistered, Boolean isAdult, String emailAddress, String accountAddress, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Customer customer = new Customer(customerId, firstName, lastName, isRegistered, isAdult, emailAddress, accountAddress);
        customerMap.put(customerId, customer);

        // Create a blockchain account for the customer and give them a starting balance
        ledgerCP.processCommand("create-account " + accountAddress, 0);
        String transactionId = accountAddress + ":T1";
        String transaction = String.format("process-transaction %s amount 1000 fee 10 note \"starting balance\" payer master receiver %s", transactionId, accountAddress);
        ledgerCP.processCommand(transaction, 0);

        // Create an Authentication Service User object for the customer
        authService.createUser(customerId, firstName + " " + lastName);
    }

    /**
     * Updates the customer object's location as the customer moves around the store.
     *
     * @param customerId Unique customer ID
     * @param location   Location in store:aisle
     */
    public void updateCustomer(String customerId, String location, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        String storeId = location.split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Customer customer = customerMap.get(customerId);
        customer.setCurrentLocation(location);
        customer.setTimeLastSeen(LocalDateTime.now().withNano(0));
    }

    /**
     * Display customer details
     *
     * @param customerId Unique customer ID
     */
    public void showCustomer(String customerId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        Customer customer = customerMap.get(customerId);
        if (customer == null) {
            throw new StoreModelServiceException("show customer", "customer not found");
        }
        try {
            String location = customer.getCurrentLocation();
            String storeId = location.split(":")[0];
            try {
                authService.checkAccess(authToken, "user_admin", storeId);
            } catch (AccessDeniedException e) {
                System.out.println(e.getMessage());
                return;
            }
            customer.show();
        } catch (NullPointerException e) {
            System.out.println("** Could not show customer details - customer not found in store **");
        }
    }

    /**
     * Retrieves the ID of the basket with the given Customer. If the customer does not have a basket,
     * this method makes a call to defineBasket with no arguments which results in a new basket being created.
     * The new basket ID is returned.
     *
     * @param customerId Unique customer ID
     * @return The ID of the Basket the customer is using.
     */
    public String getCustomerBasket(String customerId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        Customer customer = customerMap.get(customerId);
        if (customer == null) {
            throw new StoreModelServiceException("get customer basket", "Customer does not exist: " + customerId);
        }

        String storeId = customer.getCurrentLocation().split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        Basket basket = customer.getBasket();
        if (basket == null) {
            basket = this.defineBasket();
        }
        return basket.getId();
    }

    /**
     * Creates a new Basket object.
     *
     * @param basketId Unique ID
     */
    public void defineBasket(String basketId, String storeId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }
        Basket basket = new Basket(basketId);
        basketMap.put(basketId, basket);
    }

    /**
     * This version of defineBasket is called when a get_customer_basket call is made and the customer does not
     * already have a basket. The current implementation results in a new basket being created and assigned to
     * that customer. The new basket needs a unique ID. This method generates a unique ID String that looks like
     * "bx", where x is some positive integer.
     * Returns the new Basket object.
     */
    public Basket defineBasket() {
        Set<String> ids = basketMap.keySet();
        String basketId;
        if (ids.isEmpty()) {
            basketId = "b1";
        } else {
            basketId = (String) ids.toArray()[0];
            // Generate a unique ID for the new basket
            while (ids.contains(basketId)) {
                String str = basketId.replaceAll("[^0-9]", "");
                int num = Integer.parseInt(str);
                num++;
                basketId = "b" + num;
            }
        }

        Basket basket = new Basket(basketId);
        basketMap.putIfAbsent(basketId, basket);
        return basket;
    }

    /**
     * Assigns the given Basket to the given Customer.
     *
     * @param basketId   unique Basket ID
     * @param customerId unique Customer ID
     */
    public void assignBasket(String basketId, String customerId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");
        
        String location = getCustomerLocation(customerId, authToken);
        if (location == null) throw new StoreModelServiceException("assign basket", "Customer location not found.");

        String storeId = location.split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Basket basket = basketMap.get(basketId);
        if (basket == null) {
            defineBasket(basketId, storeId, authToken);
        }
        basket = basketMap.get(basketId);
        customerMap.get(customerId).setBasket(basket);
    }

    /**
     * Adds a Product to the customer's basket.
     *
     * @param basketId  Unique basket ID
     * @param productId ID of the product being added
     * @param count     The number of product that was added
     */
    public void addBasketItem(String basketId, String productId, int count, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Basket basket = basketMap.get(basketId);
        Product product = productMap.get(productId);
        if (basket == null) {
            throw new StoreModelServiceException("add basket item", "Basket does not exist.");
        } else if (product == null) {
            throw new StoreModelServiceException("add basket item", "Product does not exist.");
        }
        basket.addItem(product, count);
    }

    /**
     * Removes a Product from the Customer's basket.
     *
     * @param basketId  Unique basket ID
     * @param productId ID of the product being removed
     * @param count     The number of product being removed
     */
    public void removeBasketItem(String basketId, String productId, int count, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        basketMap.get(basketId).removeItem(productMap.get(productId), count);
    }

    /**
     * Removes all the items from the given basket.
     *
     * @param basketId Unique basket ID
     */
    public void clearBasket(String basketId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        basketMap.get(basketId).clear();
    }

    /**
     * Displays the product IDs and counts of all products in the Basket.
     *
     * @param basketId The basket's unique ID.
     */
    public void showBasket(String basketId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        Basket basket = basketMap.get(basketId);

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (basket == null) {
            System.out.println("No such basket.");
        } else {
            basket.show();
        }
    }

    /**
     * Creates a new Device object. Handles creation for both Sensors and Appliances.
     * Sensor types are [microphone, camera]
     * Appliance types are [speaker, robot, turnstile]
     *
     * @param deviceId The unique ID of the device
     * @param name     The device name
     * @param type     The type of device it is
     * @param location The location of the device - store:aisle
     */
    public void defineDevice(String deviceId, String name, String type, String location, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        String storeId = location.split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Sensor sensor = null;
        Appliance appliance = null;
        switch (type) {
            case "microphone":
                sensor = new Microphone(deviceId, name, location);
                break;
            case "camera":
                sensor = new Camera(deviceId, name, location);
                break;
            case "robot":
                appliance = new Robot(deviceId, name, location);
                break;
            case "turnstile":
                appliance = new Turnstile(deviceId, name, location);
                break;
            case "speaker":
                appliance = new Speaker(deviceId, name, location);
                break;
            default:
                System.out.println("No such sensor type: " + type);
                break;
        }
        if (sensor != null) {
            sensorMap.put(deviceId, sensor);
        } else if (appliance != null) {
            applianceMap.put(deviceId, appliance);
        }
    }

    /**
     * Displays device details
     *
     * @param deviceId The ID of the device
     */
    public void showDevice(String deviceId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        Sensor sensor = sensorMap.get(deviceId);
        Appliance appliance = applianceMap.get(deviceId);

        if (sensor != null) {
            // Device found in the sensor map; display its properties
            String location = sensor.getLocation();
            String storeId = location.split(":")[0];
            try {
                authService.checkAccess(authToken, "user_admin", storeId);
            } catch (AccessDeniedException e) {
                System.out.println(e.getMessage());
                return;
            }

            sensor.show();
        } else if (appliance != null) {
            // Device found in the appliance map; display its properties
            String location = appliance.getLocation();
            String storeId = location.split(":")[0];
            try {
                authService.checkAccess(authToken, "user_admin", storeId);
            } catch (AccessDeniedException e) {
                System.out.println(e.getMessage());
                return;
            }

            appliance.show();
        } else {
            // Device not found in either sensors or appliances
            throw new StoreModelServiceException("show device", "Device not found.");
        }
    }

    /**
     * Uses the event factory class to create a concrete Event class instance in response to sensors detecting
     * occurrences within stores.
     *
     * @param deviceId  The ID of the detecting sensor or appliance
     * @param storeId   The ID of the store the event occurred in
     * @param eventArgs The event-specific information used to determine Event type and relay relevant
     *                  information to the StoreControllerService
     */
    public void createEvent(String deviceId, String storeId, String[] eventArgs, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        Event event = factory.createEvent(deviceId, storeId, eventArgs);
        notifyObservers(event);
    }

    /**
     * Sends a command to an Appliance. The appliance will process and carry out the command.
     *
     * @param deviceId The ID of the device being commanded
     * @param command  The command being issued.
     */
    public void createCommand(String deviceId, String command, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        Appliance appliance = applianceMap.get(deviceId);
        if (appliance == null) {
            throw new StoreModelServiceException("create command", "Appliance does not exist: " + deviceId);
        }

        String storeId = appliance.getLocation().split(":")[0];
        String perm = "control_" + appliance.getType();
        try {
            authService.checkAccess(authToken, perm, storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return;
        }

        appliance.setCurrentTask(command);
    }

    /**
     * Uses the basket getCost() method to return the total value of items in the basket
     *
     * @param basketId The basket ID
     * @return The total value of items in the basket
     */
    public String getBasketCost(String basketId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("get basket cost", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        int cost = basketMap.get(basketId).getCost();
        return String.valueOf(cost);
    }

    /**
     * Returns the customer's current location or throws an exception if the customer does not exist
     *
     * @param customerId The customer ID
     * @return The customer's current location
     */
    public String getCustomerLocation(String customerId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        Customer customer = customerMap.get(customerId);
        if (customer == null) {
            throw new StoreModelServiceException("get customer location", "Customer does not exist: " + customerId);
        }
        String location = customer.getCurrentLocation();
        if (location == null)
            throw new StoreModelServiceException("get customer location", "Customer location is null");

        String storeId = location.split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
            return location;
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Subject method that registers an object as an Observer of the StoreModelService
     *
     * @param observer A pointer to the object that is being registered
     */
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
        System.out.println("Adding observer " + observer);
    }

    /**
     * Subject method that removes an object from the list of observers of the StoreModelService
     *
     * @param observer A pointer to the object being removed
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        System.out.println("Removing observer " + observer);
    }

    /**
     * Subject method that notifies all observers when an event occurs
     *
     * @param event The concrete Event object that informs the observers of what's going on
     */
    @Override
    public void notifyObservers(Event event) {
        for (Observer observer : observers) {
            System.out.println("Notifying observer " + observer + event);
            observer.update(event);
        }
    }

    /**
     * Stores a pointer to the Ledger Service Command Processor for use in creating customer accounts
     *
     * @param ledgerCP A pointer to the ledger CommandProcessor
     */
    public void setLedgerAPI(com.cscie97.ledger.CommandProcessor ledgerCP) {
        this.ledgerCP = ledgerCP;
    }

    /**
     * Creates a new Ledger instance. This method is called once when the system is started.
     */
    public void createLedger() {
        // Create the blockchain ledger and an account for the store 24 service
        ledgerCP.processCommand("create-ledger new description \"store 24 ledger\" seed 24X7", 0);
        ledgerCP.processCommand("create-account Store24X7", 0);
    }

    /**
     * Returns the ID of the aisle that the device is located in, or throws an exception if the device does not exist
     *
     * @param deviceId The ID of the device
     * @return The ID of the aisle the device is in
     */
    public String getDeviceLocation(String deviceId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        if (applianceMap.get(deviceId) != null) {
            String location = applianceMap.get(deviceId).getLocation();
            String storeId = location.split(":")[0];
            try {
                authService.checkAccess(authToken, "user_admin", storeId);
                return location;
            } catch (AccessDeniedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } else if (sensorMap.get(deviceId) != null) {
            String location = sensorMap.get(deviceId).getLocation();
            String storeId = location.split(":")[0];
            try {
                authService.checkAccess(authToken, "user_admin", storeId);
                return location;
            } catch (AccessDeniedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            throw new StoreModelServiceException("get device location", "Device not found.");
        }
    }

    /**
     * Returns the ID of a speaker in the same aisle as the given location
     *
     * @param location The ID of an aisle
     * @return The ID of a speaker in the given aisle
     */
    public String getNearestSpeaker(String location, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        String storeId = location.split(":")[0];
        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        for (Appliance appliance : applianceMap.values()) {
            if (appliance.getType().equals("speaker") && appliance.getLocation().equals(location)) {
                return appliance.getId();
            }
        }
        // There should be a speaker in the aisle, but return this if not
        return "No speaker in aisle.";
    }

    /**
     * Finds a robot with a current task of null
     *
     * @return The ID of the robot
     */
    public String getAvailableRobot(String storeId, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        for (Appliance appliance : applianceMap.values()) {
            if (appliance.getType().equals("robot") && appliance.getCurrentTask() == null) {
                return appliance.getId();
            }
        }
        // There should be a speaker in the aisle, but return this if not
        return "No robots available.";
    }

    /**
     * Returns a concatenated string of appliances of the requested type. The string contains the ID
     * of all appliances of that type
     *
     * @param type robot, turnstile, speaker
     * @return A string of device IDs separated by a space character
     */
    public String getAppliancesOfType(String storeId, String type, String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new StoreModelServiceException("", "No auth token provided.");

        try {
            authService.checkAccess(authToken, "user_admin", storeId);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        StringBuilder applianceIds = new StringBuilder();
        for (Appliance appliance : applianceMap.values()) {
            if (appliance.getType().equals(type)) {
                applianceIds.append(appliance.getId()).append(" ");
            }
        }
        return applianceIds.toString();
    }
}
