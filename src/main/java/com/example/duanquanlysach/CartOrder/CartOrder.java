package com.example.duanquanlysach.CartOrder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

public class CartOrder {
    private int maDH;
    private int maSach;
    private int maNguoiDung;
    private String tenSach;
    private int gia;
    private int soLuong;
    private String hinhAnh;

    private BooleanProperty selected = new SimpleBooleanProperty(false);


    public CartOrder(int maDH, int maNguoiDung, String anh, String tenSach, int soLuong, int giaSach) {
        this.maDH = maDH;
        this.maNguoiDung = maNguoiDung;
        this.hinhAnh = anh;
        this.tenSach = tenSach;
        this.soLuong = soLuong;
        this.gia = giaSach;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }


}
