package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Functoin_Add_Product {
    @FXML
    private ImageView imageProduct;
    private String imagePath;
    @FXML
    private TextField nameProduct;
    @FXML
    private TextField authorProduct;
    @FXML
    private TextField contentProduct;
    @FXML
    private ComboBox yearProduct;
    @FXML
    private ComboBox publishingHouseProduct;
    @FXML
    private TextField priceProduct;
    @FXML
    private TextField quantityProduct;
    @FXML
    private ComboBox typeProduct;
    @FXML
    private ComboBox statusProduct;

    @FXML
    public void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectFile = fileChooser.showOpenDialog(null);

        if (selectFile != null){
            imagePath = selectFile.toURI().toString();

            Image image = new Image(imagePath);
            imageProduct.setImage(image);
        }
    }

    public void saveProduct() throws SQLException {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "insert into sach (Anh, TenSach, TacGia, NoiDung, NamXuatBan, MaNXB, GiaSach, SoLuong, MaLoaiSach ,TrangThai) " +
                "value(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;

        try {
            int maLoaiSach = getMaLoaiSach((String) typeProduct.getValue());
            int maNXB = getMaNXB((String) publishingHouseProduct.getValue());


            String name = nameProduct.getText();
            String author = authorProduct.getText();
            String content = contentProduct.getText();
            int year = Integer.parseInt(yearProduct.getValue().toString());
            double price = Double.parseDouble(priceProduct.getText());
            int quantity = Integer.parseInt(quantityProduct.getText());
            String status = statusProduct.getValue().toString();
            String image = imageProduct.getImage() != null ? imageProduct.getImage().getUrl() : "";

            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, image);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, author);
            preparedStatement.setString(4, content);
            preparedStatement.setInt(5, year);
            preparedStatement.setInt(6, maNXB);
            preparedStatement.setDouble(7,price);
            preparedStatement.setInt(8, quantity);
            preparedStatement.setInt(9, maLoaiSach);
            preparedStatement.setString(10,status);

            int row = preparedStatement.executeUpdate();
            if (row > 0){
                System.out.println("Đã thêm thành công !");
                nameProduct.clear();
                authorProduct.clear();
                contentProduct.clear();
                priceProduct.clear();
                quantityProduct.clear();


                try {

                    Main.changeScene("LoginInterface.fxml");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.close();
    }
    public  int getMaLoaiSach(String typeProduct){
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select MaLoaiSach from LoaiSach where TenLoaiSach = ?";

        int maLoaiSach = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,typeProduct);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                maLoaiSach = resultSet.getInt("MaLoaiSach");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return maLoaiSach;
    }
    public  int getMaNXB(String publishingHouseProduct){
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select MaNXB from NhaXuatBan where TenNXB= ?";

        int maNXB = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,publishingHouseProduct);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                maNXB = resultSet.getInt("MaNXB");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return maNXB;
    }
}
