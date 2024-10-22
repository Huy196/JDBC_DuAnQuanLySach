package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    @FXML
    private TextField nameLogin;
    @FXML
    private PasswordField password;
    @FXML
    private Label falsePassword;

    public void Login() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            Connection connection = connectionDatabase.connection();

            String SQL_Ten = "select * from nguoidung where Ten = ?";

            PreparedStatement preparedStatement = null;

            preparedStatement = connection.prepareStatement(SQL_Ten);
            preparedStatement.setString(1, nameLogin.getText());

            ResultSet resultSet_Ten = preparedStatement.executeQuery();

            if (resultSet_Ten.next()) {
                String SQL_MatKhau = "select * from nguoidung where Ten = ? and MatKhau = ?";
                preparedStatement = connection.prepareStatement(SQL_MatKhau);
                preparedStatement.setString(1, nameLogin.getText());
                preparedStatement.setString(2, password.getText());

                ResultSet resultSet_MatKhau = preparedStatement.executeQuery();

                if (resultSet_MatKhau.next()) {
                    String role = resultSet_Ten.getString("Role");

                    String trangThai = resultSet_MatKhau.getString("TrangThai");
                    if (trangThai.equalsIgnoreCase("On")) {
                        if (role.equalsIgnoreCase("Quản Lý")) {
                            nameLogin.clear();
                            password.clear();
                            falsePassword.setText("");

                            thongBao();
                            Main.changeScene("LoginInterface.fxml");
                        } else {
                            System.out.println("Đăng nhập vào màn hình người dùng");
                            falsePassword.setText("");
                        }
                    } else {
                    }
                } else {
                    falsePassword.setText("Wrong login information!");
                    password.requestFocus();
                }
            } else {
                falsePassword.setText("Wrong login information!");
                nameLogin.requestFocus();
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void thongBao() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Successfully");
        alert.setHeaderText(null);
        alert.setContentText("Login Successfully");

        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }


}