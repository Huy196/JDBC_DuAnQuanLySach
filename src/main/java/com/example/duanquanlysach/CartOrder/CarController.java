package com.example.duanquanlysach.CartOrder;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.Product.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CarController implements Initializable {
    @FXML
    private TableView<CartOrder> tableView;
    @FXML
    private TableColumn<CartOrder, CheckBox> select;
    @FXML
    private TableColumn<CartOrder, Integer> MaDH;
    @FXML
    private TableColumn<CartOrder, String> image;
    @FXML
    private TableColumn<CartOrder, String> name;
    @FXML
    private TableColumn<CartOrder, Integer> price;
    @FXML
    private TableColumn<CartOrder, Integer> quantity;
    @FXML
    private TableColumn<CartOrder, Integer> sum;
    @FXML
    private TableColumn<CartOrder, Button> deleteButton;

    @FXML
    private Label sumAllProduct;
    private ObservableList<CartOrder> cartOrderObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        select.setCellFactory(column -> new TableCell<CartOrder, CheckBox>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(CheckBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CartOrder cartOrder = getTableView().getItems().get(getIndex());
                    CheckBox checkBox = new CheckBox();
                    checkBox.selectedProperty().set(cartOrder.isSelected());

                    checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                        cartOrder.setSelected(isSelected);
                        calculateTotalSum();
                    });

                    setGraphic(checkBox);

                }
            }
        });
        MaDH.setCellValueFactory(new PropertyValueFactory<CartOrder, Integer>("maDH"));
        image.setCellValueFactory(new PropertyValueFactory<CartOrder, String>("hinhAnh"));
        image.setCellFactory(column -> new TableCell<CartOrder, String>() {
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


        name.setCellValueFactory(new PropertyValueFactory<CartOrder,String>("tenSach"));
        quantity.setCellValueFactory(new PropertyValueFactory<CartOrder,Integer>("soLuong"));
        price.setCellValueFactory(new PropertyValueFactory<CartOrder,Integer>("gia"));
        quantity.setCellFactory(column -> new TableCell<CartOrder, Integer>() {
            private Spinner<Integer> spinner;

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CartOrder order = getTableView().getItems().get(getIndex());

                    spinner = new Spinner<>(1, 100, order.getSoLuong());
                    spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                        order.setSoLuong(newValue);
                        updateSumColumn();
                        calculateTotalSum();
                    });
                    setGraphic(spinner);
                }
            }
        });

        sum.setCellFactory(column -> new TableCell<CartOrder, Integer>() {
            private final Label sum = new Label();

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CartOrder order = getTableView().getItems().get(getIndex());
                    int total = order.getGia() * order.getSoLuong();
                    sum.setText(String.valueOf(total));
                    setGraphic(sum);
                }
            }
        });

        deleteButton.setCellFactory(column -> new TableCell<CartOrder, Button>() {
            private final Button deleteBtn = new Button("Xóa");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CartOrder order = getTableView().getItems().get(getIndex());
                    deleteBtn.setOnAction(event -> {
                        cartOrderObservableList.remove(order);
                        deleteItemFromDatabase(order.getMaDH());
                        calculateTotalSum();
                    });
                    setGraphic(deleteBtn);
                }
            }
        });



        loadCartData();
    }
    private void calculateTotalSum(){
        int sum = cartOrderObservableList.stream().filter(CartOrder :: isSelected).mapToInt(order -> order.getGia() * order.getSoLuong()).sum();

        sumAllProduct.setText(String.valueOf(sum));
    }

    private void updateSumColumn() {
        tableView.refresh();
    }

    private void loadCartData() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();

            String SQL = "SELECT * FROM donhang";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);


            while (resultSet.next()) {
                CartOrder cartOrder = new CartOrder(
                        resultSet.getInt("MaDH"),
                        resultSet.getInt("MaNguoiDung"),
                        resultSet.getString("Anh"),
                        resultSet.getString("TenSach"),
                        resultSet.getInt("SoLuong"),
                        resultSet.getInt("GiaSach")
                );
                cartOrderObservableList.add(cartOrder);
            }
            tableView.setItems(cartOrderObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteItemFromDatabase(int maDH) {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();

            String SQL = "delete from donhang where MaDH = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,maDH);

            int row = preparedStatement.executeUpdate();
            if (row >0){
                Alert("Đã Xóa");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void Alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông Báo");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
