package com.example.duanquanlysach.CartOrder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.math.BigDecimal;

public class CartOrder {
    private int maGH;
    private int maSach;
    private int maNguoiDung;
    private String tenSach;
    private BigDecimal gia;
    private int soLuong;
    private String hinhAnh;
    private boolean checkBox;

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    private BooleanProperty selected = new SimpleBooleanProperty(false);


    public CartOrder(int maGH, String anh, String tenSach, BigDecimal giaSach, int soLuong) {
        this.maGH = maGH;
        this.hinhAnh = anh;
        this.tenSach = tenSach;
        this.soLuong = soLuong;
        this.gia = giaSach;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public int getMaGH() {
        return maGH;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
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
