package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyUser {
    private static final String DB_URL1 = "jdbc:sqlite:demo.db";
    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE IF NOT EXISTS customers(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,password TEXT)";
    
    private final Connection connection;
    private final List<Product> products;
    private Customer loggedInCustomer;
    private final ShoppingCart shoppingCart;

    public MyUser() {
        connection=createConnection();
        products = new ArrayList<>();
        loggedInCustomer = null;
        shoppingCart = new ShoppingCart();
        
        createTables();
    }
    protected void userSystem(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 用户系统 ====");
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 返回上级菜单");

            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerCustomer(scanner);
                    break;
                case 2:
                    loggedInCustomer = UserLogin(scanner);
                    if (loggedInCustomer != null) {
                        shopping(scanner);
                    }
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }
    }

    private Connection createConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL1);
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_CUSTOMERS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerCustomer(Scanner scanner) {
        System.out.print("请输入用户名：");
        String name = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO customers (name, password) VALUES (?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("注册成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Customer UserLogin(Scanner scanner) {
        System.out.print("请输入用户名：");
        String name = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM customers WHERE name = ? AND password = ?")) {
            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int customerId = resultSet.getInt("id");
                String customerName = resultSet.getString("name");
                System.out.println("用户账号登陆成功！");
                return new Customer(customerId,customerName, password);
            } else {
                System.out.println("用户名或密码错误！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Product getProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
    // 用户系统功能
    private void shopping(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 购物系统 ====");
            System.out.println("1. 将商品加入购物车");
            System.out.println("2. 从购物车移除商品");
            System.out.println("3. 修改购物车中的商品数量");
            System.out.println("4. 模拟结账");
            System.out.println("5. 查看购物历史");
            System.out.println("6. 退出登录");

            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("请输入要加入购物车的商品名称：");
                    String productName = scanner.next();
                    Product product = getProductByName(productName);
                    if (product != null) {
                        System.out.print("请输入要加入购物车的数量：");
                        int quantity = scanner.nextInt();
                        shoppingCart.addItem(product, quantity);
                        System.out.println("商品已添加至购物车！");
                    } else {
                        System.out.println("找不到该商品！");
                    }
                    break;
                case 2:
                    System.out.print("请输入要移除的商品名称：");
                    String pName = scanner.next();
                    Product p = getProductByName(pName);
                    if (p != null) {
                        System.out.print("请输入要移除的数量：");
                        int qty = scanner.nextInt();
                        shoppingCart.removeItem(p, qty);
                        System.out.println("商品已从购物车移除！");
                    } else {
                        System.out.println("找不到该商品！");
                    }
                    break;
                case 3:
                    System.out.print("请输入要修改数量的商品名称：");
                    String prodName = scanner.next();
                    Product pr = getProductByName(prodName);
                    if (pr != null) {
                        System.out.print("请输入新的商品数量：");
                        int newQty = scanner.nextInt();
                        shoppingCart.updateQuantity(pr, newQty);
                        System.out.println("商品数量已修改！");
                    } else {
                        System.out.println("找不到该商品！");
                    }
                    break;
                case 4:
                    double total = shoppingCart.calculateTotal();
                    if (total > 0) {
                        System.out.println("结账成功！总金额为 $" + total);
                        shoppingCart.clear();
                    } else {
                        System.out.println("购物车为空，无法结账！");
                    }
                    break;
                case 5:
                    shoppingCart.displayCart();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }
    }
}

