package org.example;

import java.util.HashMap;
import java.util.Map;

// 购物车类
class ShoppingCart {
    private Map<Product, Integer> items;

    public ShoppingCart() {
        items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (items.containsKey(product)) {
            int currentQuantity = items.get(product);
            items.put(product, currentQuantity + quantity);
        } else {
            items.put(product, quantity);
        }
    }

    public void removeItem(Product product, int quantity) {
        if (items.containsKey(product)) {
            int currentQuantity = items.get(product);
            if (currentQuantity <= quantity) {
                items.remove(product);
            } else {
                items.put(product, currentQuantity - quantity);
            }
        }
    }

    public void updateQuantity(Product product, int newQuantity) {
        if (items.containsKey(product)) {
            items.put(product, newQuantity);
        }
    }
    public double calculateTotal() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.getPrice() * quantity;
        }
        return total;
    }

    public void clear() {
        items.clear();
    }

    public void displayCart() {
        System.out.println("Shopping Cart:");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("- " + product.getName() + " x" + quantity);
        }
        System.out.println("Total: $" + calculateTotal());
    }
}

