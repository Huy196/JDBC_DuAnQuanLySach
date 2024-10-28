package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DangKy {
    @FXML
    private TextField  TenDangNhap;
    @FXML
    private TextField  SDT;
    @FXML
    private TextField  DiaChi;
    @FXML
    private PasswordField MatKhau;
    @FXML
    private PasswordField  NhapLaiMatKhau;

    public void DangKy(){
        String tendangnhap = TenDangNhap.getText();
        String matkhau = MatKhau.getText();
        String nhapLaiMatKhau = NhapLaiMatKhau.getText();
        String sđt =SDT.getText();
        String diachi = DiaChi.getText();
        if(matkhau.isEmpty() || tendangnhap.isEmpty() || sđt.isEmpty() || diachi.isEmpty()){
            System.out.println("Chưa điền thông tin người đăng ký");
        }else if (matkhau.equalsIgnoreCase(nhapLaiMatKhau)){
            try {
                    ConnectionDatabase connectionDatabase = new ConnectionDatabase();
                var connection = connectionDatabase.connection();

                String SQL_NguoiDung = "insert into NguoiDung (Ten ,MatKhau, DiaChi, SDT, Role, TrangThai )" +
                        "value (?, ?, ?,?,?,?)";

                PreparedStatement preparedStatement = null;

                preparedStatement = connection.prepareStatement(SQL_NguoiDung);
                preparedStatement.setString(1, TenDangNhap.getText());
                preparedStatement.setString(2,MatKhau.getText());
                preparedStatement.setString(3,DiaChi.getText());
                preparedStatement.setString(4,SDT.getText());
                preparedStatement.setString(5,"Người Dùng");
                preparedStatement.setString(6,"On");

                int row = preparedStatement.executeUpdate();
                if (row > 0){
                    System.out.println("Đã thêm người dùng thành công");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                NhapLaiMatKhau.requestFocus();
            }
        }else {


        }

    }
}
