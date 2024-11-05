package com.example.duanquanlysach.Login;

public class CurrentUser {
    private static CurrentUser instance;
    private String username;
    private String password;

    // Private constructor để ngăn tạo thể hiện mới
    private CurrentUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Phương thức để khởi tạo instance khi đăng nhập thành công
    public static void login(String username, String password) {
        if (instance == null) {
            instance = new CurrentUser(username, password);
        }
    }

    // Lấy instance của người dùng hiện tại
    public static CurrentUser getInstance() {
        return instance;
    }

    // Phương thức để lấy thông tin người dùng
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Đăng xuất để xóa instance hiện tại
    public static void logout() {
        instance = null;
    }
}
