package org.example;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.*;

public class MyAdminManager {
    private String adminFilePath = "admin.txt";
    private String userFilePath = "users.txt";
    public MyAdminManager() {
        
    }
    protected void passwordManagement(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 密码管理 ====");
            System.out.println("1. 修改管理员密码");
            System.out.println("2. 重置用户密码");
            System.out.println("3. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("请输入新密码：");
                    String newPassword = scanner.next();
                    writeAdminPassword(newPassword);
                    System.out.println("密码修改成功！");
                    break;
                case 2:
                    System.out.print("请输入要重置密码的用户名：");
                    String username = scanner.next();
                    Customer customer = getCustomerByUsername(username);
                    if (customer != null) {
                        System.out.print("请输入新密码：");
                        String newPwd = scanner.next();
                        resetUserPassword(username, newPwd);
                        System.out.println("用户密码重置成功！");
                    } else {
                        System.out.println("找不到该用户！");
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

    private void writeAdminPassword(String newPassword) {
        try {
            FileWriter fileWriter = new FileWriter(adminFilePath);
            fileWriter.write("admin:" + newPassword);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void resetUserPassword(String username, String newPassword) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(userFilePath));
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(username)) {
                    lines.set(i, username + ":" + newPassword);
                    break;
                }
            }
            Files.write(Paths.get(userFilePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Customer getCustomerByUsername(String username) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(userFilePath));
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    return new Customer(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void customerManagement(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 客户管理 ====");
            System.out.println("1. 列出所有客户信息");
            System.out.println("2. 删除客户信息");
            System.out.println("3. 查询客户信息");
            System.out.println("4. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    listAllCustomers();
                    break;
                case 2:
                    System.out.print("请输入要删除的用户名：");
                    String username = scanner.next();
                    deleteCustomer(username);
                    break;
                case 3:
                    System.out.print("请输入要查询的用户名：");
                    String name = scanner.next();
                    Customer customer = getCustomerByUsername(name);
                    if (customer != null) {
                        System.out.println("用户名：" + customer.getName() + " 密码：" + customer.getPassword());
                    } else {
                        System.out.println("找不到该用户！");
                    }
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }
    }
    private void listAllCustomers() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(userFilePath));
            System.out.println("所有客户信息：");
            for (String line : lines) {
                String[] parts = line.split(":");
                System.out.println("用户名：" + parts[0] + " 密码：" + parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomer(String username) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(userFilePath));
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(username)) {
                    lines.remove(i);
                    found = true;
                    break;
                }
            }
            if (found) {
                Files.write(Paths.get(userFilePath), lines);
                System.out.println("用户删除成功！");
            } else {
                System.out.println("找不到该用户！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    protected void productManagement(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 商品管理 ====");
            System.out.println("1. 列出所有商品信息");
            System.out.println("2. 添加商品信息");
            System.out.println("3. 修改商品信息");
            System.out.println("4. 删除商品信息");
            System.out.println("5. 查询商品信息");
            System.out.println("6. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    listAllProducts();
                    break;
                case 2:
                    addProduct(scanner);
                    break;
                case 3:
                    System.out.print("请输入要修改的商品名称：");
                    String name = scanner.next();
                    updateProduct(name, scanner);
                    break;
                case 4:
                    System.out.print("请输入要删除的商品名称：");
                    String productName = scanner.next();
                    deleteProduct(productName);
                    break;
                case 5:
                    System.out.print("请输入要查询的商品名称：");
                    String pname = scanner.next();
                    Product product = getProductByName(pname);
                    if (product != null) {
                        System.out.println("商品名称：" + product.getName() + " 价格：" + product.getPrice());
                    } else {
                        System.out.println("找不到该商品！");
                    }
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

    private void listAllProducts() {
        System.out.println("所有商品信息：");
        try (FileReader fileReader = new FileReader("products.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                System.out.println("商品名称：" + name + " 价格：" + price);
            }
        } catch (IOException e) {
            System.out.println("读取商品信息出错！");
        }
    }

    private void addProduct(Scanner scanner) {
        System.out.print("请输入商品名称：");
        String name = scanner.next();
        System.out.print("请输入商品价格：");
        double price = scanner.nextDouble();
        try (FileWriter fileWriter = new FileWriter("products.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String line = name + "," + price;
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            System.out.println("商品添加成功！");
        } catch (IOException e) {
            System.out.println("添加商品信息出错！");
        }
    }
    private void updateProduct(String name, Scanner scanner) {
        try (FileReader fileReader = new FileReader("products.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter fileWriter = new FileWriter("temp.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String line;
            boolean found = false;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String productName = parts[0];
                if (productName.equals(name)) {
                    System.out.print("请输入新的商品价格：");
                    double newPrice = scanner.nextDouble();
                    line = productName + "," + newPrice;
                    found = true;
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            if (found) {
                System.out.println("商品信息修改成功！");
            } else {
                System.out.println("找不到该商品！");
            }
        } catch (IOException e) {
            System.out.println("修改商品信息出错！");
        }
        File productsFile = new File("products.txt");
        File tempFile = new File("temp.txt");
        productsFile.delete();
        tempFile.renameTo(productsFile);
    }

    private void deleteProduct(String productName) {
        try (FileReader fileReader = new FileReader("products.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter fileWriter = new FileWriter("temp.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String line;
            boolean found = false;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                if (!name.equals(productName)) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                } else {
                    found = true;
                }
            }
            if (found) {
                System.out.println("商品删除成功！");
            } else {
                System.out.println("找不到该商品！");
            }
        } catch (IOException e) {
            System.out.println("删除商品信息出错！");
        }
        File productsFile = new File("products.txt");
        File tempFile = new File("temp.txt");
        productsFile.delete();
        tempFile.renameTo(productsFile);
    }

    private Product getProductByName(String name) {
        try (FileReader fileReader = new FileReader("products.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String productName = parts[0];
                double productPrice = Double.parseDouble(parts[1]);
                if (productName.equals(name)) {
                    return new Product(productName, productPrice);
                }
            }
        } catch (IOException e) {
            System.out.println("读取商品信息出错！");
        }
        return null;
    }
}
