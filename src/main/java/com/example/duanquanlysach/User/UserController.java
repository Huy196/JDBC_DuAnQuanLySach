package com.example.duanquanlysach.User;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.CartOrder.CarController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class UserController implements Initializable {
    @FXML
    private Label nameUser;
    @FXML
    private Label address;
    @FXML
    private ImageView imageUser;
    @FXML
    private Label phone;
//    @FXML
//    private Label pass;


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
}
