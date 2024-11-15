package com.example.duanquanlysach.Product;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Product {
    private int maSach;
    private String Anh;
    private String tenSach;
    private String tacGia;
    private String noiDung;
    private int namXB;
    private int maNXB;
    private BigDecimal giaSach;
    private int soLuong;
    private int maLoaiSach;
    private String trangThai;

    public Product(int maSach,String anh, String tenSach, String tacGia, String noiDung, int namXB, int maNXB, BigDecimal giaSach, int soLuong, int maLoaiSach, String trangThai) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.noiDung = noiDung;
        this.namXB = namXB;
        this.maNXB = maNXB;
        this.giaSach = giaSach;
        this.soLuong = soLuong;
        this.maLoaiSach = maLoaiSach;
        this.trangThai = trangThai;
        this.Anh = anh;
    }

    public Product() {

    }

    public Product(int id, String name, BigDecimal price, String imagePath) {
        this.maSach = id;
        this.tenSach = name;
        this.giaSach = price;
        this.Anh = imagePath;
    }

    public Product(String nameProduct, int quantity, BigDecimal price) {
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public int getNamXB() {
        return namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    public int getMaNXB() {
        return maNXB;
    }

    public void setMaNXB(int maNXB) {
        this.maNXB = maNXB;
    }

    public BigDecimal getGiaSach() {
        return giaSach;
    }

    public void setGiaSach(BigDecimal giaSach) {
        this.giaSach = giaSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaLoaiSach() {
        return maLoaiSach;
    }

    public void setMaLoaiSach(int maLoaiSach) {
        this.maLoaiSach = maLoaiSach;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
