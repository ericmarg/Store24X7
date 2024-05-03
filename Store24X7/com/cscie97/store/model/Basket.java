package com.cscie97.store.model;

import java.util.HashMap;
import java.util.Map;

/**product
 * Models shopping baskets that customers carry items in while shopping. Provides an interface for adding or removing
 * items, displaying contents, and for clearing the basket.
 */
public class Basket implements Showable {

	private String id;

	private Map<Product, Integer> productMap;

    public Basket(String id) {
        this.id = id;
        productMap = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addItem(Product product, int count) {
        if (productMap.get(product) == null && count > 0) {
            productMap.put(product, count);
        } else {
            int currentCount = productMap.get(product);
            currentCount += count;
            productMap.put(product, currentCount);
        }
        assert(productMap.get(product) >= 0);
    }

    public void removeItem(Product product, int count) {
        int currentCount = productMap.get(product);
        currentCount -= count;
        assert(currentCount >= 0);
        productMap.put(product, currentCount);
    }

    public void clear() {
        productMap.clear();
    }

    @Override
    public void show() {
        if (productMap.isEmpty()) {
            System.out.println("This basket is empty.");
        } else {
            for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                System.out.println("Product ID: " + product.getId() + ", Quantity: " + quantity);
            }
        }
    }

    public int getCost() {
        int cost = 0;
        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            cost += product.getPrice() * quantity;
        }
        return cost;
    }
}
