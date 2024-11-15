package com.example.duanquanlysach.InterfaceShop;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.Login.CurrentUser;
import com.example.duanquanlysach.Login.Login;
import com.example.duanquanlysach.Product.Functoin_ProductCotroller;
import com.example.duanquanlysach.Product.Product;
import com.example.duanquanlysach.User.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Random;

public class ProductDetialController {
    private User user;
    private Product product;
    @FXML
    private Label maProductDetail;
    @FXML
    private ImageView imageProductDetail;
    @FXML
    private Label nameProductDetail;
    @FXML
    private Label authorProductDetail;
    @FXML
    private Label publishingHouseProductDetail;
    @FXML
    private Label typeProductDetail;
    @FXML
    private Label yearProductDetail;
    @FXML
    private Label contentProductDetail;
    @FXML
    private Label priceProductDetail;
    @FXML
    private Spinner quatityProductDetail;


    public void setProductDetailData(Product product) {
        Functoin_ProductCotroller productCotroller = new Functoin_ProductCotroller();
        this.product = product;

        maProductDetail.setText(Integer.toString(product.getMaSach()));
        imageProductDetail.setImage(new Image(product.getAnh()));
        nameProductDetail.setText(product.getTenSach());
        authorProductDetail.setText(product.getTacGia());
        contentProductDetail.setText(product.getNoiDung());
        yearProductDetail.setText(Integer.toString(product.getNamXB()));
        publishingHouseProductDetail.setText(productCotroller.getTenNXB(product.getMaNXB()));
        priceProductDetail.setText((product.getGiaSach()) + " VND");
        typeProductDetail.setText(productCotroller.getTenLoaiSach(product.getMaLoaiSach()));
    }

    public void addProductToCart() {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        int quatity = (int) quatityProductDetail.getValue();
        int maSach = product.getMaSach();
        int maNguoiDung = getMaNguoiDung();
        String image = product.getAnh();
        String name = product.getTenSach();
        BigDecimal price = product.getGiaSach();

        String SQL = "INSERT INTO GioHang (MaNguoiDung,MaSach,Anh,Ten,Soluong,Gia)" +
                " VALUES (?,?,?,?,?,?) ";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,maNguoiDung);
            preparedStatement.setInt(2,maSach);
            preparedStatement.setString(3,image);
            preparedStatement.setString(4,name);
            preparedStatement.setInt(5,quatity);
            preparedStatement.setBigDecimal(6,price);


            int row = preparedStatement.executeUpdate();
            if (row > 0){
                Alert("Đã thêm vào đơn hàng !");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaNguoiDung() {
        String name = CurrentUser.getInstance().getUsername();
        String pass = CurrentUser.getInstance().getPassword();

        int maNguoiDung = -1;

        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select MaNguoiDung from NguoiDung where Ten = ? and MatKhau = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,pass);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                maNguoiDung = resultSet.getInt("MaNguoiDung");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maNguoiDung;
    }

    private void Alert( String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Đăng nhập thành công");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.9), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
