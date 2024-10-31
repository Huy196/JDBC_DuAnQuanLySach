package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ViewShopSellBookController implements Initializable {

    @FXML
    private FlowPane hBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProductData();
    }

    private void loadProductData() {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        String query = "SELECT * FROM Sach";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("MaSach");
                String name = resultSet.getString("TenSach");
                int price = resultSet.getInt("GiaSach");
                String imagePath = resultSet.getString("Anh");

                // Tạo các thành phần cho sản phẩm
                VBox productBox = createProductBox(id, name, price, imagePath);
                hBox.getChildren().add(productBox); // Thêm sản phẩm vào hBox
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private VBox createProductBox(int id, String name, int price, String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
//        imageView.setPreserveRatio(true);
        imageView.setFitHeight(210);
        imageView.setFitWidth(265);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Label priceLabel = new Label(price + "  ₫");
        priceLabel.setStyle("-fx-text-fill: red; -fx-font-size: 17px;");


        Hyperlink detailLink = new Hyperlink("Chi tiết");
        detailLink.setOnAction(event -> showProductDetails(id));

        VBox productBox = new VBox(imageView, nameLabel, priceLabel, detailLink);
        productBox.setSpacing(10);
        hBox.setPrefWrapLength(20);
        hBox.setHgap(50);
        hBox.setVgap(50);
        productBox.setFillWidth(true);

        productBox.setStyle("-fx-border-color: #524e4e; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        return productBox;
    }

    private void showProductDetails(int productId) {
        System.out.println("Showing details for product ID: " + productId);
    }

    @FXML
    public void logOff() {
        try {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận đăng xuất");
                alert.setHeaderText(null);
                alert.setContentText("Bạn có chắc chắn muốn đăng xuất không?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Main.changeScene("Login.fxml");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
