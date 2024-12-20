package com.example.duanquanlysach.User;

import ConnectionDatabase.ConnectionDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class Admin_UserController implements Initializable {
    @FXML
    private TableColumn<User, Integer> idUser;
    @FXML
    private TableColumn<User, String> imageUser;
    @FXML
    private TableColumn<User, String> nameUser;
    @FXML
    private TableColumn<User, String> passUser;
    @FXML
    private TableColumn<User, String> addressUser;
    @FXML
    private TableColumn<User, String> phoneUser;
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
        idUser.setCellValueFactory(new PropertyValueFactory<User, Integer>("MaNguoiDung"));
        imageUser.setCellValueFactory(new PropertyValueFactory<User, String>("Anh"));
        imageUser.setCellFactory(column -> new TableCell<User, String>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(120);
                imageView.setFitHeight(120);
                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);

            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    Image image = new Image(imagePath, true);
                    imageView.setImage(image);
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
        functionUser.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Sửa");
            private final Button editStatusButton = new Button("Cập nhật");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox hBox = new HBox(editButton, editStatusButton);
                    hBox.setSpacing(10);

                    editButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        editInformationUser(user);
                    });

                    editStatusButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        editStatusUser(user);

                    });
                    setGraphic(hBox);
                    setText(null);
                }
            }
        });
    }

    public void editInformationUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_interfaceUpdateInformationUser.fxml"));
            Parent root = loader.load();

            Funtion_UserController controller = loader.getController();
            controller.setUserData(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void editStatusUser(User user) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL_TrangThai = "Update nguoidung set TrangThai= case when TrangThai = 'On' then 'Off' else 'On' end where MaNguoiDung = ? and Role != 'Quản Lý'";
        PreparedStatement preparedStatement = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thay đổi trạng thái");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn khóa tài khoản này ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                preparedStatement = connection.prepareStatement(SQL_TrangThai);
                preparedStatement.setInt(1, user.getMaNguoiDung());

                int row = preparedStatement.executeUpdate();
                if (row > 0) {
                    show("Cập nhật thành công !");
                } else if (user.getRole().equalsIgnoreCase("Quản Lý")) {
                    show("Không thể cập nhật trạng thái của người quản lý");
                } else {
                    show("Cập nhật thất bại !");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    private void show(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cập nhật");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.9), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
