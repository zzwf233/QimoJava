package org.example;

// 管理员类
class Admin {
    private String username;
    private String password;
    private int adminId;
    public Admin(int adminId,String username, String password) {
        this.adminId=adminId;
        this.username = username;
        this.password = password;
    }
    public int getAdminId(){
        return adminId;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}