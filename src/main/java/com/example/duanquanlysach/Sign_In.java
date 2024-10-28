package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sign_In {
    @FXML
    private TextField TenDangNhap;
    @FXML
    private TextField SDT;
    @FXML
    private TextField DiaChi;
    @FXML
    private PasswordField MatKhau;
    @FXML
    private PasswordField NhapLaiMatKhau;

    public void DangKy() {
        String tendangnhap = TenDangNhap.getText();
        String matkhau = MatKhau.getText();
        String nhapLaiMatKhau = NhapLaiMatKhau.getText();
        String sdt = SDT.getText();
        String diachi = DiaChi.getText();

        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL_NguoiDung = "insert into NguoiDung (Ten ,MatKhau, DiaChi, SDT, Role, TrangThai )" +
                "value (?, ?, ?,?,?,?)";


        try {
            if (TenDangNhap.getText().isEmpty()) {
                highlightField(TenDangNhap);
                TenDangNhap.requestFocus();
                return;
            } else {
                clearHighlight(TenDangNhap);
            }

            if (sdt.isEmpty() || !sdt.matches("\\d{10}")) {
                highlightField(SDT);
                SDT.requestFocus();
                return;
            } else {
                clearHighlight(SDT);
            }

            if (diachi.isEmpty()) {
                highlightField(DiaChi);
                DiaChi.requestFocus();
                return;
            } else {
                clearHighlight(DiaChi);
            }

            if (matkhau.isEmpty()) {
                highlightField(MatKhau);
                MatKhau.requestFocus();
                return;
            } else {
                clearHighlight(MatKhau);
            }

            if (nhapLaiMatKhau.isEmpty()) {
                highlightField(NhapLaiMatKhau);
                NhapLaiMatKhau.requestFocus();
                return;
            } else if (!matkhau.equals(nhapLaiMatKhau)) {
                highlightField(NhapLaiMatKhau);
                NhapLaiMatKhau.requestFocus();
                return;
            } else {
                clearHighlight(NhapLaiMatKhau);
            }


            PreparedStatement preparedStatement = null;

            preparedStatement = connection.prepareStatement(SQL_NguoiDung);
            preparedStatement.setString(1, TenDangNhap.getText());
            preparedStatement.setString(2, MatKhau.getText());
            preparedStatement.setString(3, DiaChi.getText());
            preparedStatement.setString(4, SDT.getText());
            preparedStatement.setString(5, "Người Dùng");
            preparedStatement.setString(6, "On");

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                Alert("Đăng ký thành công");
                TenDangNhap.clear();
                DiaChi.clear();
                MatKhau.clear();
                NhapLaiMatKhau.clear();
                SDT.clear();

                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
<<<<<<< HEAD:src/main/java/com/example/duanquanlysach/Sign_In.java

=======
    public void Sigin_Login() {
        try {
            Main.changeScene("Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
>>>>>>> 4ee0520c619a6de303de58dfa0619bf9ed047b57:src/main/java/com/example/duanquanlysach/DangKy.java
    private void highlightField(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

    }

    public void Sigin_Login() {
        try {
            Main.changeScene("Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearHighlight(TextField field) {
        field.setStyle("");
    }

    private void Alert(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(name);

        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}