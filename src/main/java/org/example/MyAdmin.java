package org.example;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class MyAdmin {
    private static final String DB_URL2 = "jdbc:sqlite:Admin.db";
    private static final String CREATE_TABLE_ADMINS = "CREATE TABLE IF NOT EXISTS admins(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,password TEXT)";

    private final Connection connection;
    private MyAdminManager myAdminManager = new MyAdminManager();

    private Admin admin;
    public MyAdmin() {
        connection = createConnection();
        admin = null;
        createTables();
    }
    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_ADMINS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Connection createConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL2);
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                    if (adminLogin(scanner) != null) {
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
            String adminname = scanner.next();
            if (!isValidUsername(adminname)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            System.out.print("请输入管理员密码：");
            String password = scanner.next();
            if (!isValidUserpassword(password)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO admins (name, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, adminname);
                statement.setString(2, password);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int adminId = generatedKeys.getInt(1);
                        admin = new Admin(adminId, adminname, password);
                        System.out.println("管理员账号注册成功！");
                    }
                } else {
                    System.out.println("注册管理员账号失败！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }catch (IllegalArgumentException e) {
            System.out.println("错误： " + e.getMessage());
        }
    }
    private boolean isValidUsername(String adminname) {
        String regex = "^[a-zA-Z0-9]+$";
        return adminname.matches(regex);
    }
    private boolean isValidUserpassword(String userpassword) {
        String regex = "^[a-zA-Z0-9]+$";
        return userpassword.matches(regex);
    }


    private Admin adminLogin(Scanner scanner) {
        if (admin == null) {
            System.out.println("管理员账户不存在，请先注册管理员账户！");
            return null;
        }
        System.out.print("请输入管理员用户名：");
        String adminname = scanner.next();
        System.out.print("请输入管理员密码：");
        String password = scanner.next();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM admins WHERE name = ? AND password = ?")) {
            statement.setString(1, adminname);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int adminId = resultSet.getInt("id");
                String adminName = resultSet.getString("name");
                admin= new Admin(adminId, adminName, password);
                System.out.println("管理员账号登陆成功！");
                return admin;
            } else {
                System.out.println("用户名或密码错误！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
