package com.example.duanquanlysach.Product;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.Product.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Functoin_ProductCotroller {

    private Product product;
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

        if (selectFile != null) {
            imagePath = selectFile.toURI().toString();

            Image image = new Image(imagePath);
            imageProduct.setImage(image);
        }
    }

    public void saveProduct() throws SQLException {

        try {
            if (nameProduct.getText().isEmpty()) {
                highlightField(nameProduct);
                nameProduct.requestFocus();
                return;
            } else {
                clearHighlight(nameProduct);
            }
            if (authorProduct.getText().isEmpty()) {
                highlightField(authorProduct);
                authorProduct.requestFocus();
                return;
            } else {
                clearHighlight(authorProduct);
            }
            if (contentProduct.getText().isEmpty()) {
                highlightField(contentProduct);
                contentProduct.requestFocus();
                return;
            } else {
                clearHighlight(contentProduct);
            }
            if (!isNumeric(priceProduct.getText())) {
                highlightField(priceProduct);
                Alert("Giá sách phải là một số hợp lệ!");
                priceProduct.requestFocus();
                return;
            } else {
                clearHighlight(priceProduct);
            }
            if (!isNumeric(quantityProduct.getText())) {
                highlightField(quantityProduct);
                Alert("Số lượng là một số hợp lệ!");
                quantityProduct.requestFocus();
                return;
            } else {
                clearHighlight(quantityProduct);
            }
            if (yearProduct.getValue() == null) {
                Alert("Vui lòng chọn năm xuất bản!");
                yearProduct.requestFocus();
                return;
            }
            if (publishingHouseProduct.getValue() == null) {
                Alert("Vui lòng chọn nhà xuất bản!");
                publishingHouseProduct.requestFocus();
                return;
            }
            if (typeProduct.getValue() == null) {
                Alert("Vui lòng chọn loại sách!");
                typeProduct.requestFocus();
                return;
            }

            if (statusProduct.getValue() == null) {
                Alert("Vui lòng chọn trạng thái sách!");
                statusProduct.requestFocus();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận cập nhật");
            alert.setHeaderText("Bạn có chắc chắn muốn thêm sản phẩm này?");

            Optional<ButtonType> result = alert.showAndWait();


            if (result.isPresent() && result.get() == ButtonType.OK) {
                ConnectionDatabase connectionDatabase = new ConnectionDatabase();
                var connection = connectionDatabase.connection();

                String SQL = "insert into sach (Anh, TenSach, TacGia, NoiDung, NamXuatBan, MaNXB, GiaSach, SoLuong, MaLoaiSach ,TrangThai) " +
                        "value(?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = null;


                int maLoaiSach = getMaLoaiSach((String) typeProduct.getValue());
                int maNXB = getMaNXB((String) publishingHouseProduct.getValue());


                String name = nameProduct.getText();
                String author = authorProduct.getText();
                String content = contentProduct.getText();
                int year = Integer.parseInt(yearProduct.getValue().toString());
                BigDecimal price = new BigDecimal(priceProduct.getText());
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
                preparedStatement.setBigDecimal(7, price);
                preparedStatement.setInt(8, quantity);
                preparedStatement.setInt(9, maLoaiSach);
                preparedStatement.setString(10, status);

                int row = preparedStatement.executeUpdate();
                if (row > 0) {
                    Alert("Đã thêm thành công !");
                    nameProduct.clear();
                    authorProduct.clear();
                    contentProduct.clear();
                    priceProduct.clear();
                    quantityProduct.clear();

                    Stage stage = (Stage) nameProduct.getScene().getWindow();
                    stage.close();
                } else {
                    Alert("Đã hủy thêm sản phẩm!");
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void highlightField(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    private void clearHighlight(TextField field) {
        field.setStyle("");
    }

    public int getMaLoaiSach(String typeProduct) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select MaLoaiSach from LoaiSach where TenLoaiSach = ?";

        int maLoaiSach = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, typeProduct);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                maLoaiSach = resultSet.getInt("MaLoaiSach");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maLoaiSach;
    }

    public int getMaNXB(String publishingHouseProduct) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select MaNXB from NhaXuatBan where TenNXB= ?";

        int maNXB = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, publishingHouseProduct);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                maNXB = resultSet.getInt("MaNXB");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maNXB;
    }

    @FXML
    public void handAddOrUpdateProduct() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận cập nhật");
            alert.setHeaderText("Bạn có chắc chắn muốn cập nhật sản phẩm này?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                ConnectionDatabase connectionDatabase = new ConnectionDatabase();
                var connection = connectionDatabase.connection();

                int maLoaiSach = getMaLoaiSach((String) typeProduct.getValue());
                int maNXB = getMaNXB((String) publishingHouseProduct.getValue());

                String name = nameProduct.getText();
                String author = authorProduct.getText();
                String content = contentProduct.getText();
                int year = Integer.parseInt(yearProduct.getValue().toString());
                BigDecimal price = new BigDecimal(priceProduct.getText());
                int quantity = Integer.parseInt(quantityProduct.getText());
                String image = imageProduct.getImage() != null ? imageProduct.getImage().getUrl() : "";

                String SQL = "update sach set Anh = ?, TenSach = ?, TacGia = ?, NoiDung = ?, NamXuatBan = ?, MaNXB = ?, GiaSach = ?, SoLuong = ?, MaLoaiSach = ? where MaSach = ?";

                PreparedStatement preparedStatement = null;

                preparedStatement = connection.prepareStatement(SQL);

                preparedStatement.setString(1, image);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, author);
                preparedStatement.setString(4, content);
                preparedStatement.setInt(5, year);
                preparedStatement.setInt(6, maNXB);
                preparedStatement.setBigDecimal(7, price);
                preparedStatement.setInt(8, quantity);
                preparedStatement.setInt(9, maLoaiSach);
                preparedStatement.setInt(10, product.getMaSach());

                int row = preparedStatement.executeUpdate();


                if (row > 0) {
                    Alert("Cập nhật sản phẩm thành công !");
                    nameProduct.clear();
                    authorProduct.clear();
                    contentProduct.clear();
                    priceProduct.clear();
                    quantityProduct.clear();
                    typeProduct.setValue(null);
                    yearProduct.setValue(null);
                    publishingHouseProduct.setValue(null);
                    imageProduct.setImage(null);

                    Stage stage = (Stage) nameProduct.getScene().getWindow();
                    stage.close();
                }
                connection.close();
            } else {
                Alert("Đã hủy cập nhật!");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setProductData(Product product) {
        this.product = product;
        String image = product.getAnh();
        Image image1 = new Image(image);

        imageProduct.setImage(image1);
        nameProduct.setText(product.getTenSach());
        authorProduct.setText(product.getTacGia());
        contentProduct.setText(product.getNoiDung());
        yearProduct.setValue(product.getNamXB());
        publishingHouseProduct.setValue(getTenNXB(product.getMaNXB()));
        priceProduct.setText(String.valueOf(product.getGiaSach()));
        quantityProduct.setText(String.valueOf(product.getSoLuong()));
        typeProduct.setValue(getTenLoaiSach(product.getMaLoaiSach()));

    }

    public String getTenNXB(int maNXB) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select TenNXB from NhaXuatBan where MaNXB = ?";

        String TenNXB = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, maNXB);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                TenNXB = resultSet.getString("TenNXB");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return TenNXB;
    }

    public String getTenLoaiSach(int maLoaiSach) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select TenLoaiSach from LoaiSach where MaLoaiSach = ?";

        String tenLoaiSach = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, maLoaiSach);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tenLoaiSach = resultSet.getString("TenLoaiSach");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenLoaiSach;
    }

    private void Alert(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(name);

        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.8), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();

    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}