package org.example;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MyAdmin {
    private final MyAdminManager myAdminManager = new MyAdminManager();
    private Admin admin;
    public MyAdmin() {
        admin = null;
    }
    protected void adminSystem(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 管理员系统 ====");
            System.out.println("1. 注册管理员账户");
            System.out.println("2. 登录管理员账户");
            System.out.println("3. 返回上级菜单");

            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerAdmin(scanner);
                    break;
                case 2:
                    if (adminLogin(scanner)!=null) {
                        adminMenu(scanner);
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
    private void registerAdmin(Scanner scanner) {
        try {
            if (admin != null) {
                System.out.println("管理员账户已存在，无法注册新的管理员账户！");
                return;
            }
            System.out.print("请输入管理员用户名：");
            String username = scanner.next();
            if (!isValidUsername(username)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            System.out.print("请输入管理员密码：");
            String password = scanner.next();
            if (!isValidUserpassword(username)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            admin = new Admin(username, password);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("admin.txt", true))) {
                writer.write(username + ":" + password);
                writer.newLine();
                System.out.println("管理员注册成功！");
            } catch (IOException e) {
                System.out.println("注册失败：" + e.getMessage());
            }
        }catch (IllegalArgumentException e) {
            System.out.println("错误： " + e.getMessage());
        }
    }

    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        return username.matches(regex);
    }
    private boolean isValidUserpassword(String userpassword) {
        String regex = "^[a-zA-Z0-9]+$";
        return userpassword.matches(regex);
    }


    private Admin adminLogin(Scanner scanner) {
        System.out.print("请输入管理员用户名：");
        String username = scanner.next();
        System.out.print("请输入管理员密码：");
        String password = scanner.next();
        try (BufferedReader reader = new BufferedReader(new FileReader("admin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(":");
                if (userInfo.length == 2 && userInfo[0].equals(username) && userInfo[1].equals(password)) {
                    System.out.println("登录成功！");
                    return new Admin(username, password);
                }
            }
            System.out.println("用户名或密码错误！");
        } catch (IOException e) {
            System.out.println("登录失败：" + e.getMessage());
        }
        return null;
    }
    private void adminMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 管理员系统 ====");
            System.out.println("1. 密码管理");
            System.out.println("2. 客户管理");
            System.out.println("3. 商品管理");
            System.out.println("4. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    myAdminManager.passwordManagement(scanner);
                    break;
                case 2:
                    myAdminManager.customerManagement(scanner);
                    break;
                case 3:
                    myAdminManager.productManagement(scanner);
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
}