package com.example.duanquanlysach.User;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.CartOrder.CarController;
import com.example.duanquanlysach.Product.Functoin_ProductCotroller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class UserController implements Initializable {
    private User user;
    @FXML
    private Label nameUser;
    @FXML
    private Label address;
    @FXML
    private ImageView imageUser;
    @FXML
    private Label phone;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CarController controller = new CarController();
        int maNguoiDung = controller.getMaNguoiDung_1();
        loadData(maNguoiDung);
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
                address.setText(resultSet.getString("DiaChi"));
                phone.setText(resultSet.getString("SDT"));

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
    private void updateInformation(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("User_interface_update_information.fxml"));
            Parent root = loader.load();

//            Update_Information_User updateInformationUser = loader.getController();
//            updateInformationUser.setInformation(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
