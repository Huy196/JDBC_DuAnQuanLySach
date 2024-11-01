package com.example.duanquanlysach.User;

import com.example.duanquanlysach.User.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

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

    public void handleChooseImage (){
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

    public void setUserData(User userData){
        this.user = userData;
        String image = userData.getAnh();
        Image image1 = new Image(image);

        imageUser.setImage(image1);
        nameUser.setText(userData.getTen());
        addressUser.setText(userData.getDiaChi());
        phoneNumberUser.setText(userData.getSDT());
    }
}
