package org.example;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
// 购物车类
class ShoppingCart {
    private Map<Product, Integer> items;
    private final String productFilePath = "shoppingCart.txt";
    public ShoppingCart() {
        items = new HashMap<>();
        loadCartFromTextFile();
    }

    public void addItem(Product product, int quantity) {
        if (items.containsKey(product)) {
            int currentQuantity = items.get(product);
            items.put(product, currentQuantity + quantity);
        } else {
            items.put(product, quantity);
        }
        saveCartToTextFile(productFilePath);
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
        saveCartToTextFile(productFilePath);
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

    public void displayCart() {
        System.out.println("=== 购物车中的商品 ===");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("商品名称：" + product.getName());
            System.out.println("商品价格：" + product.getPrice());
            System.out.println("商品数量：" + quantity);
            System.out.println("-----------------");
        }
    }
    public void saveShoppingHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productFilePath, true))) {
            writer.write("=== 购物历史 ===");
            writer.newLine();
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                writer.write("商品名称：" + product.getName());
                writer.newLine();
                writer.write("商品价格：" + product.getPrice());
                writer.newLine();
                writer.write("商品数量：" + quantity);
                writer.newLine();
                writer.write("-----------------");
                writer.newLine();
            }
            writer.write("总价：" + calculateTotal());
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Failed to save shopping history: " + e.getMessage());
        }
    
    }
    public void updateQuantity(Product product, int newQuantity) {
        if (items.containsKey(product)) {
            items.put(product, newQuantity);
        }
        saveCartToTextFile(productFilePath);
    }
    public void clear() {
        items.clear();
        saveCartToTextFile(productFilePath);
    }


    private void loadCartFromTextFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(productFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length>=2){
                    String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                int quantity = Integer.parseInt(parts[2]);
                items.put(new Product(name, price), quantity);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load cart from text file: " + e.getMessage());
        }
    }
    protected void saveCartToTextFile(String productFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productFilePath))) {
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                String line = product.getName() + "," + product.getPrice() + "," + quantity;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save cart to text file: " + e.getMessage());
        }
    }
}