package com.example.duanquanlysach;

public class User {
    private int MaNguoiDung;
    private String Anh;
    private String Ten;
    private String MatKhau;
    private String DiaChi;
    private String SDT;

    private String Role;
    private String TrangThai;

    public User(int maNguoiDung, String anh, String ten, String matKhau, String diaChi, String SDT, String role, String trangThai) {
        this.MaNguoiDung = maNguoiDung;
        this.Anh = anh;
        this.Ten = ten;
        this.MatKhau = matKhau;
        this.DiaChi = diaChi;
        this.SDT = SDT;
        this.Role = role;
        this.TrangThai = trangThai;
    }

    public int getMaNguoiDung() {
        return MaNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        MaNguoiDung = maNguoiDung;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
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
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }
}
