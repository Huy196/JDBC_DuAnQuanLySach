package com.example.duanquanlysach.User;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.User.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Funtion_UserController {
    private User user;

    @FXML
    private ImageView imageUser;

    private String imagePath;

    @FXML
    private TextField nameUser;

    @FXML
    private TextField addressUser;

    @FXML
    private TextField phoneNumberUser;

    public void handleChooseImage() {
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

    public void setUserData(User userData) {
        this.user = userData;
        String image = userData.getAnh();
        if (image != null && !image.isEmpty()) {
            Image image1 = new Image(image);
            imageUser.setImage(image1);
        } else {
            imageUser.setImage(null);
        }
        nameUser.setText(userData.getTen());
        addressUser.setText(userData.getDiaChi());
        phoneNumberUser.setText(userData.getSDT());
    }

    @FXML
    private void updateInformation() {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        int id = user.getMaNguoiDung();
        String image = imageUser.getImage() != null ? imageUser.getImage().getUrl() : "";
        String name = nameUser.getText();
        String address = addressUser.getText();
        String SDT = phoneNumberUser.getText();

        String SQL = "update NguoiDung set Anh = ?, Ten = ?, DiaChi = ?, SDT = ? where MaNguoiDung = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, image);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, SDT);
            preparedStatement.setInt(5, id);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                Alert("Sửa thông tin thành công!");
            }
        } catch (SQLException e) {
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
