package com.example.duanquanlysach.User;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.CartOrder.CarController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Update_Information_User implements Initializable {
    private User user;
    private String imagePath;

    @FXML
    private TextField nameUser;
    @FXML
    private TextField phoneUser;
    @FXML
    private TextField addressUser;
    @FXML
    private ImageView imageUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CarController controller = new CarController();
        int maNguoiDung = controller.getMaNguoiDung_1();
        loadData(maNguoiDung);
    }
    @FXML
    public void clickImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectFile = fileChooser.showOpenDialog(null);

        if (selectFile != null) {
            imagePath = selectFile.toURI().toString();

            Image image = new Image(imagePath);
            imageUser.setImage(image);
        }
    }
    private void loadData(int maNguoiDung) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select * from NguoiDung where MaNguoiDung = ?";
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(SQL);
            preparedStatement.setInt(1,maNguoiDung);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                user = new User();
                nameUser.setText(resultSet.getString("Ten"));
                addressUser.setText(resultSet.getString("DiaChi"));
                phoneUser.setText(resultSet.getString("SDT"));

                String imagePath = resultSet.getString("Anh");

                if (imagePath != null && !imagePath.isEmpty()) {
                    Image image = new Image(imagePath);
                    imageUser.setImage(image);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void updateUser(){
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        CarController controller = new CarController();
        int maNguoiDung = controller.getMaNguoiDung_1();

        String image = imageUser.getImage() != null ? imageUser.getImage().getUrl() : "";
        String name = nameUser.getText();
        String address = addressUser.getText();
        String SDT = phoneUser.getText();



        String SQL = "update NguoiDung set Anh = ?,Ten = ?,DiaChi = ?,SDT = ? where MaNguoiDung = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,image);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,address);
            preparedStatement.setString(4,SDT);
            preparedStatement.setInt(5,maNguoiDung);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                Alert("Cập nhật thông tin thành công!");

                Stage stage = (Stage) nameUser.getScene().getWindow();
                stage.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
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

}
