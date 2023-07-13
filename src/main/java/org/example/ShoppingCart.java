package org.example;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// 购物车类
class ShoppingCart {
    private Map<Product, Integer> items;
    private final String productFilePath = "C:\\Users\\Lenovo\\Desktop\\products.txt";

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

    public double calculateTotal() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.getPrice() * quantity;
        }
        return total;
    }

    public void updateQuantity(Product product, int newQuantity) {
        if (items.containsKey(product)) {
            items.put(product, newQuantity);
        }
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
    public void loadFromFile() {
        try {
            FileReader fileReader = new FileReader(productFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);

                    Product product = new Product(name, price);
                    items.put(product, quantity);
                }
            }

            bufferedReader.close();

            System.out.println("Product information has been loaded from the file.");

        } catch (FileNotFoundException e) {
            System.out.println("Product information file not found. Starting with an empty cart.");
        } catch (IOException e) {
            System.out.println("Error occurred while loading product information from the file: " + e.getMessage());
        }
    }
}