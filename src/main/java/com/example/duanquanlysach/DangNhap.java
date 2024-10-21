package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class DangNhap {
    @FXML
    private Button Home;
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

                    String trangThai = resultSet_MatKhau.getString("TrangThai");
                    if (trangThai.equalsIgnoreCase("On")) {
                        if (role.equalsIgnoreCase("Quản Lý")) {
                            tenDangNhap.clear();
                            matKhau.clear();
                            saiMatKhau.setText("");

                            thongBao();
                            chuyenManHinhAdmin();
                        } else {
                            System.out.println("Đăng nhập vào màn hình người dùng");
                            saiMatKhau.setText("");
                        }
                    } else {
                    }
                } else {
                    saiMatKhau.setText("Sai thông tin đăng nhập !");
                    matKhau.requestFocus();
                }
            } else {
                saiMatKhau.setText("Sai thông tin đăng nhập !");
                tenDangNhap.requestFocus();
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void chuyenManHinhAdmin() throws IOException {
        Stage stage = (Stage) tenDangNhap.getScene().getWindow();

        Parent adminRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GiaoDienAdmin.fxml")));
        Scene adminScene = new Scene(adminRoot);

        stage.setScene(adminScene);
    }

    private void thongBao() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Đăng nhập thành công");
        alert.setHeaderText(null);
        alert.setContentText("Đăng nhập thành công!");

        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void dangXuat(){
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận đăng xuất");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn đăng xuất không ?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                Stage stage = (Stage) Home.getScene().getWindow();

                Parent dangXuat = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DangNhap.fxml")));
                Scene dangXuat_1 = new Scene(dangXuat);
                stage.setScene(dangXuat_1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
