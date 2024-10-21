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
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DangNhap.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 740);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        connectionDatabase.connection();
    }
    public static void main(String[] args) {
        launch();
    }
}