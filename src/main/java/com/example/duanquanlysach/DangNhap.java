package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DangNhap {
    @FXML
    private TextField tenDangNhap;
    @FXML
    private PasswordField matKhau;
    @FXML
    private Label saiMatKhau;

    public void dangNhap() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            Connection connection = connectionDatabase.connection();

            String SQL_Ten = "select * from nguoidung where Ten = ?";

            PreparedStatement preparedStatement = null;

            preparedStatement = connection.prepareStatement(SQL_Ten);
            preparedStatement.setString(1, tenDangNhap.getText());

            ResultSet resultSet_Ten = preparedStatement.executeQuery();

            if (resultSet_Ten.next()) {
                String SQL_MatKhau = "select * from nguoidung where Ten = ? and MatKhau = ?";
                preparedStatement = connection.prepareStatement(SQL_MatKhau);
                preparedStatement.setString(1, tenDangNhap.getText());
                preparedStatement.setString(2, matKhau.getText());

                ResultSet resultSet_MatKhau = preparedStatement.executeQuery();

                if (resultSet_MatKhau.next()) {
                    String role = resultSet_Ten.getString("Role");

                    if (role.equalsIgnoreCase("Quản Lý")) {
                        System.out.println("Đăng nhập vào màn hình quản lý");
                        saiMatKhau.setText("");
                    } else {
                        System.out.println("Đăng nhập vào màn hình người dùng");
                        saiMatKhau.setText("");
                    }
                }else {
                    matKhau.requestFocus();
                }
            } else {
                saiMatKhau.setText("Sai thông tin đăng nhập !");
                tenDangNhap.requestFocus();
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
