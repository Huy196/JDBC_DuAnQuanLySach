package com.example.duanquanlysach.Order;

import com.example.duanquanlysach.CartOrder.CartOrder;
import com.example.duanquanlysach.Product.Product;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private boolean checkBox;
    private int maDH;
    private Timestamp date;
    private String nameUser;
    private ObservableList<CartOrder> orders;
    private String status;
    private String pay;

    public Order() {
    }


    public Order(int maDH, Timestamp date, String nameUser, ObservableList<CartOrder> orders, String status, String pay) {
        this.maDH = maDH;
        this.date = date;
        this.nameUser = nameUser;
        this.orders = orders;
        this.status = status;
        this.pay = pay;
    }


    public Order(String nameProduct, int quantity, BigDecimal price) {
    }

    public Order(int maDh, Timestamp date, String name, String status) {
        this.maDH = maDh;
        this.date = date;
        this.nameUser = name;
        this.status = status;
    }

    public Order(int maDh, Timestamp date,String status) {
        this.maDH = maDh;
        this.date = date;
        this.status =status;
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public ObservableList<CartOrder> getOrders() {
        return orders;
    }

    public void setOrders(ObservableList<CartOrder> orders) {
        this.orders = orders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public BigDecimal sum() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartOrder cartOrder : orders) {
            BigDecimal productTotal = cartOrder.getGia().multiply(BigDecimal.valueOf(cartOrder.getSoLuong()));
            total = total.add(productTotal);
        }
        return total;
    }
}

