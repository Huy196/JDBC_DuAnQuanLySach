package com.example.duanquanlysach;

public class User {
    private int maNguoiDung;
    private String anh;
    private String ten;
    private String matKhau;
    private String diaChi;
    private String SDT;

    private String Role;
    private String trangThai;

    public User(int maNguoiDung, String anh, String ten, String matKhau, String diaChi, String SDT, String role, String trangThai) {
        this.maNguoiDung = maNguoiDung;
        this.anh = anh;
        this.ten = ten;
        this.matKhau = matKhau;
        this.diaChi = diaChi;
        this.SDT = SDT;
        this.Role = role;
        this.trangThai = trangThai;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        maNguoiDung = maNguoiDung;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        anh = anh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        ten = ten;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        matKhau = matKhau;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        diaChi = diaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        trangThai = trangThai;
    }
}
