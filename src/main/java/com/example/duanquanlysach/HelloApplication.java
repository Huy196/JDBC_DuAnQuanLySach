package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    private static Stage primaryStage;
    public void start(Stage stage){

        HelloApplication.primaryStage = stage;
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GiaoDienAdmin.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("DangNhap");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void changeScene(String fxml) throws IOException {
        try {
            Parent pane = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));
            Scene scene = new Scene(pane);

            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}