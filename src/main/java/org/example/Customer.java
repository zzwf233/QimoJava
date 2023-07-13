package org.example;

// 客户类
class Customer {
    private String name;
    private String password;
    private int id;
    public Customer(int id,String name, String password) {
        this.id=id;
        this.name = name;
        this.password = password;
    }
    public int getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}