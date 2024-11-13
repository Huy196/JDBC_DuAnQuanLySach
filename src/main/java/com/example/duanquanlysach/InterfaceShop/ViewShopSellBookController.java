package com.example.duanquanlysach.InterfaceShop;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.Main;
import com.example.duanquanlysach.Product.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ViewShopSellBookController implements Initializable {
    @FXML
    private FlowPane hBox;
    @FXML
    private TextField nameBook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProductData();
    }

    private void loadProductData() {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        String query = "SELECT * FROM Sach where TrangThai = 'Còn hàng'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("MaSach");
                String name = resultSet.getString("TenSach");
                BigDecimal price = resultSet.getBigDecimal("GiaSach");
                String imagePath = resultSet.getString("Anh");

                VBox productBox = createProductBox(id, name, price, imagePath);
                hBox.getChildren().add(productBox);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void seachBook() {
        hBox.getChildren().clear();
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        String query = "SELECT * FROM Sach where TrangThai = 'Còn hàng' and TenSach like ?";

        String name_1 = nameBook.getText().trim();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + name_1 + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("MaSach");
                String name = resultSet.getString("TenSach");
                BigDecimal price = resultSet.getBigDecimal("GiaSach");
                String imagePath = resultSet.getString("Anh");

                VBox productBox = createProductBox(id, name, price, imagePath);
                hBox.getChildren().add(productBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createProductBox(int id, String name, BigDecimal price, String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitHeight(210);
        imageView.setFitWidth(265);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Label priceLabel = new Label(price + "  VND");
        priceLabel.setStyle("-fx-text-fill: red; -fx-font-size: 17px;");

        hBox.setPrefWrapLength(20);
        hBox.setHgap(50);
        hBox.setVgap(50);

        Hyperlink detailLink = new Hyperlink("Chi tiết");
        detailLink.setOnAction(event -> showProductDetails(id));
        VBox productBox = new VBox(imageView, nameLabel, priceLabel, detailLink);

        productBox.setStyle("-fx-border-color: #524e4e; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        return productBox;
    }

    private void showProductDetails(int maSach) {
        Product product1 = getProductByMaSach(maSach);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceShop_Detail_Product.fxml"));
            Parent root = loader.load();

            ProductDetialController productDetialController = loader.getController();

            productDetialController.setProductDetailData(product1);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProductToCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceShop_Cart.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProductOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Interface_Oder.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Product getProductByMaSach(int maSach) {

        String SQL = "SELECT * FROM Sach WHERE MaSach = ?";

        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();

            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, maSach);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("MaSach"),
                        resultSet.getString("Anh"),
                        resultSet.getString("TenSach"),
                        resultSet.getString("TacGia"),
                        resultSet.getString("NoiDung"),
                        resultSet.getInt("NamXuatBan"),
                        resultSet.getInt("MaNXB"),
                        resultSet.getBigDecimal("GiaSach"),
                        resultSet.getInt("SoLuong"),
                        resultSet.getInt("MaLoaiSach"),
                        resultSet.getString("TrangThai")
                );
                return product;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
