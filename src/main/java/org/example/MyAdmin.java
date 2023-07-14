package org.example;
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
                    if (adminLogin(scanner)) {
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
            System.out.println("管理员账户注册成功！");
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


    private boolean adminLogin(Scanner scanner) {
        if (admin == null) {
            System.out.println("管理员账户不存在，请先注册管理员账户！");
            return false;
        }
        System.out.print("请输入管理员用户名：");
        String username = scanner.next();
        System.out.print("请输入管理员密码：");
        String password = scanner.next();
        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            System.out.println("管理员登录成功！");
            return true;
        } else {
            System.out.println("管理员用户名或密码错误！");
            return false;
        }
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