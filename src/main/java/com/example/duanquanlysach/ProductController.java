package com.example.duanquanlysach;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Optional;

public class ProductController {
    @FXML
    private AnchorPane contentArea;
    public void dangXuat(){
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận đăng xuất");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn đăng xuất không ?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                HelloApplication.changeScene("DangNhap.fxml");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProduct(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SanPhamAdmin.fxml"));
            Parent dashboardView = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Có lỗi khi hiển thị giao diện  Trang chủ.");
        } finally {
            System.out.println("Đang hiển thị Trang chủ");
        }
    }
}
