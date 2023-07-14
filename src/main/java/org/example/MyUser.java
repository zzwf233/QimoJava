package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
public class MyUser {
    private Customer loggedInCustomer;
    private final ShoppingCart shoppingCart;
    private final String shoppingCartFile = "shoppingCart.txt";
    public MyUser() {
        loggedInCustomer = null;
        shoppingCart = new ShoppingCart();
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
    private void registerCustomer(Scanner scanner) {
        System.out.print("请输入用户名：");
        String username = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + ":" + password);
            writer.newLine();
            System.out.println("用户注册成功！");
        } catch (IOException e) {
            System.out.println("注册失败：" + e.getMessage());
        }
    }
    private Customer UserLogin(Scanner scanner) {
        System.out.print("请输入用户名：");
        String username = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(":");
                if (userInfo.length == 2 && userInfo[0].equals(username) && userInfo[1].equals(password)) {
                    System.out.println("登录成功！");
                    return new Customer(username, password);
                }
            }
            System.out.println("用户名或密码错误！");
        } catch (IOException e) {
            System.out.println("登录失败：" + e.getMessage());
        }
        return null;
    }
    private Product getProductByName(String name) {
        try {
            File file = new File("products.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String productName = parts[0];
                double productPrice = Double.parseDouble(parts[1]);
                Product product = new Product(productName, productPrice);
                if (product.getName().equals(name)) {
                    scanner.close();
                    return product;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                        shoppingCart.saveCartToTextFile(shoppingCartFile);
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
                        shoppingCart.saveCartToTextFile(shoppingCartFile);
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
                        shoppingCart.saveCartToTextFile(shoppingCartFile);
                        System.out.println("商品数量已修改！");
                    } else {
                        System.out.println("找不到该商品！");
                    }
                    break;
                case 4:
                    double total = shoppingCart.calculateTotal();
                    if (total > 0) {
                        shoppingCart.displayCart();
                        System.out.println("结账成功！总金额为 $" + total);
                    } else {
                        System.out.println("购物车为空，无法结账！");
                    }
                    break;
                case 5:
                    shoppingCart.saveShoppingHistory();
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