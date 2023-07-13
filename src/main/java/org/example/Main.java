package org.example;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer databaseInitializer = new DatabaseInitializer();
        databaseInitializer.initializeDatabase();
        ShoppingManagementSystem system = new ShoppingManagementSystem();
        system.run();
    }
}

