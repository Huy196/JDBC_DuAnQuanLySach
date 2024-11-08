package com.example.duanquanlysach.CartOrder;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.InterfaceShop.ProductDetialController;
import com.example.duanquanlysach.InterfaceShop.ViewShopSellBookController;
import com.example.duanquanlysach.Login.CurrentUser;
import com.example.duanquanlysach.Main;
import com.example.duanquanlysach.Product.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class CarController implements Initializable {
    @FXML
    private TableView<CartOrder> tableView;
    @FXML
    private TableColumn<CartOrder, CheckBox> select;
    @FXML
    private TableColumn<CartOrder, String> image;
    @FXML
    private TableColumn<CartOrder, String> name;
    @FXML
    private TableColumn<CartOrder, BigDecimal> price;
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
                        cartOrder.setCheckBox(isSelected);
                        updateCheckBoxCar(cartOrder.getMaGH(), cartOrder.getCheckBox());
                        calculateTotalSum();
                    });
                    setGraphic(checkBox);
                }
            }
        });
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


        name.setCellValueFactory(new PropertyValueFactory<CartOrder, String>("tenSach"));
        quantity.setCellValueFactory(new PropertyValueFactory<CartOrder, Integer>("soLuong"));
        price.setCellValueFactory(new PropertyValueFactory<CartOrder, BigDecimal>("gia"));
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
                    BigDecimal price = new BigDecimal(String.valueOf(order.getGia()));
                    BigDecimal quantity = new BigDecimal(order.getSoLuong());
                    BigDecimal total = price.multiply(quantity);
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
                        deleteItemFromDatabase(order.getMaGH());
                        calculateTotalSum();
                    });
                    setGraphic(deleteBtn);
                }
            }
        });
        loadCartData();
    }

    private void calculateTotalSum() {
        BigDecimal sum = cartOrderObservableList.stream().filter(CartOrder::isSelected).map(order -> {
            BigDecimal price = new BigDecimal(String.valueOf(order.getGia()));
            BigDecimal quantity = new BigDecimal(order.getSoLuong());
            return price.multiply(quantity);
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        sumAllProduct.setText(sum.setScale(3, RoundingMode.HALF_UP).toString());
    }

    private void updateSumColumn() {
        tableView.refresh();
    }

    public int getMaNguoiDung_1() {
        String name = CurrentUser.getInstance().getUsername();
        String pass = CurrentUser.getInstance().getPassword();

        int maNguoiDung = -1;

        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "select MaNguoiDung from NguoiDung where Ten = ? and MatKhau = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                maNguoiDung = resultSet.getInt("MaNguoiDung");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maNguoiDung;
    }

    private void loadCartData() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();

            int maNguoiDung = getMaNguoiDung_1();


            String SQL = "SELECT * FROM GioHang where MaNguoiDung = ? and TrangThai = 0 ";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, maNguoiDung);
            ResultSet resultSet = preparedStatement.executeQuery();

            cartOrderObservableList.clear();
            while (resultSet.next()) {
                CartOrder cartOrder = new CartOrder(resultSet.getInt("MaGH"), resultSet.getString("Anh"), resultSet.getString("Ten"), resultSet.getBigDecimal("Gia"), resultSet.getInt("SoLuong"));
                cartOrderObservableList.add(cartOrder);
            }
            tableView.setItems(cartOrderObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void order() {
        boolean hasSelectedItems = tableView.getItems().stream().anyMatch(CartOrder::getCheckBox);

        if (hasSelectedItems) {
            int orderID = addOrder();
            for (CartOrder cartOrder : tableView.getItems()) {
                if (cartOrder.getCheckBox()) {
                    int cartID = cartOrder.getMaGH();
                    addCartOrder(cartID, orderID);
                    updateStatusCar();
                }
            }
        } else {
            Alert("Vui lòng chọn ít nhất một sản phẩm để đặt hàng!");
        }
    }

    private void updateCheckBoxCar(int MaGH, boolean checkbox) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "update GioHang set CheckBox = ? where MaGH = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setBoolean(1, checkbox);
            preparedStatement.setInt(2, MaGH);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCartOrder(int MaGH, int MaDH) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQl = "insert into GioHang_DonHang(MaDH,MaGH)" + "value (?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQl);

            preparedStatement.setInt(1, MaDH);
            preparedStatement.setInt(2, MaGH);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int addOrder() {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        int maNguoiDung_1 = getMaNguoiDung_1();
        LocalDateTime time = LocalDateTime.now();

        String SQL = "insert into DonHang(MaNguoiDung,NgayDat,TrangThai)" + "value(?,?,'Chờ xác nhận')";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, maNguoiDung_1);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(time));

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                Alert("Tạo đơn hàng thành công!");
                closeCartInterface();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();


                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } else {
                Alert("Vui lòng chọn ít nhất 1 sản phẩm!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void updateStatusCar() {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "update GioHang set TrangThai = ? where CheckBox = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setBoolean(2, true);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeCartInterface() {
        Stage stage = (Stage) sumAllProduct.getScene().getWindow();
        stage.hide();
    }

    private void deleteItemFromDatabase(int maGH) {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();

            String SQL = "delete from GioHang where MaGH = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, maGH);
            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                Alert("Đã Xóa");
                Platform.runLater(() -> {
                    ViewShopSellBookController viewShopSellBookController = new ViewShopSellBookController();
                    viewShopSellBookController.quantityProductFromCar();
                });
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông Báo");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.9), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
