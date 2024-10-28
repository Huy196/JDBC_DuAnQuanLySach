package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Admin_UserController implements Initializable {
    @FXML
    private TableColumn<User, Integer> idUser;
    @FXML
    private TableColumn<User, String> imageUser;
    @FXML
    private TableColumn <User, String> nameUser;
    @FXML
    private TableColumn<User, String> passUser;
    @FXML
    private TableColumn<User, String> addressUser;
    @FXML
    private TableColumn<User, String>phoneUser;
    @FXML
    private TableColumn<User, String> roleUser;
    @FXML
    private TableColumn<User, String> statusUser;
    @FXML
    private TableColumn<User, String> functionUser;
    @FXML
    private TableView<User> tableView;
    private ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idUser.setCellValueFactory(new PropertyValueFactory<User, Integer> ("MaNguoiDung"));
        imageUser.setCellValueFactory(new PropertyValueFactory<User, String>("Anh"));
        imageUser.setCellFactory(column -> new TableCell<User, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    Image image = new Image(imagePath, 100, 100, true, true);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(120);
                    imageView.setImage(image);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        nameUser.setCellValueFactory(new PropertyValueFactory<User, String>("Ten"));
        passUser.setCellValueFactory(new PropertyValueFactory<User, String>("MatKhau"));
        addressUser.setCellValueFactory(new PropertyValueFactory<User, String>("DiaChi"));
        phoneUser.setCellValueFactory(new PropertyValueFactory<User, String>("SDT"));
        nameUser.setCellValueFactory(new PropertyValueFactory<User, String>("Ten"));
        roleUser.setCellValueFactory(new PropertyValueFactory<User, String>("Role"));
        statusUser.setCellValueFactory(new PropertyValueFactory<User, String>("TrangThai"));

        loadDataUser();
        functionUser.setCellFactory(param -> new TableCell<>(){
            private final Button editButton = new Button("Sửa");
            private final Button editStatusButton = new Button("Cập nhật TT");

            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item,empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox hBox = new HBox(editButton, editStatusButton);
                    hBox.setSpacing(10);

                    editButton.setOnAction(event -> {

                    });

                    editStatusButton.setOnAction(event -> {

                    });
                    setGraphic(hBox);
                    setText(null);
                }
            }
        });
    }


    public void loadDataUser() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            Connection connection = connectionDatabase.connection();

            String SQL = "select * from NguoiDung";


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("MaNguoiDung"),
                        resultSet.getString("Anh"),
                        resultSet.getString("Ten"),
                        resultSet.getString("MatKhau"),
                        resultSet.getString("DiaChi"),
                        resultSet.getString("SDT"),
                        resultSet.getString("Role"),
                        resultSet.getString("TrangThai")
                );
                users.add(user);
            }

            tableView.setItems(users);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
