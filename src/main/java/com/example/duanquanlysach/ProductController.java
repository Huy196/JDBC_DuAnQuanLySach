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

    public void logOut(){
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm sign-out");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out ?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                Main.changeScene("Login.fxml");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProduct(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductAdmin.fxml"));
            Parent dashboardView = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
