package org.example;

import java.util.Scanner;

// 购物管理系统类
public class ShoppingManagementSystem {
    private final MyUser myUser;
    private final MyAdmin myAdmin;

    public ShoppingManagementSystem() {
        myUser =new MyUser();
        myAdmin = new MyAdmin();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("==== 购物管理系统 ====");
            System.out.println("1. 用户系统");
            System.out.println("2. 管理员系统");
            System.out.println("3. 退出");

            System.out.print("请选择进入的系统：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    myUser.userSystem(scanner);
                    break;
                case 2:
                    myAdmin.adminSystem(scanner);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }

        System.out.println("感谢使用购物管理系统！");
    }

}