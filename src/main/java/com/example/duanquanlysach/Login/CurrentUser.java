package com.example.duanquanlysach.Login;

public class CurrentUser {
    private int maNguoiDung;

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    private static CurrentUser instance;
    private String username;
    private String password;

    public CurrentUser(){}
    public CurrentUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static void login(String username, String password, int maNguoiDung) {
        if (instance == null) {
            instance = new CurrentUser(username, password);
        } else {
            instance.username = username;
            instance.password = password;
            instance.maNguoiDung = maNguoiDung;
        }
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static void logout() {
        instance = null;
    }

}
