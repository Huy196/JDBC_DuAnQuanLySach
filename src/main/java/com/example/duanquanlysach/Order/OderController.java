package com.example.duanquanlysach.Order;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.CartOrder.CartOrder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OderController implements Initializable {
    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableColumn<Order, Integer> maDH;
    @FXML
    private TableColumn<Order, Timestamp> date;
    @FXML
    private TableColumn<Order, String> nameUser;
    @FXML
    private TableColumn<Order, VBox> productOrder;
    @FXML
    private TableColumn<Order, BigDecimal> sum;
    @FXML
    private TableColumn<CartOrder, String> status;
    @FXML
    private TableColumn<Order, String> pay;
    @FXML
    private TableColumn<Order, String> function;

    private ObservableList<Order> orders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orders = FXCollections.observableArrayList();

        maDH.setCellValueFactory(new PropertyValueFactory<Order, Integer>("maDH"));
        date.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("date"));
        nameUser.setCellValueFactory(new PropertyValueFactory<Order, String>("nameUser"));
        productOrder.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            VBox vbox = new VBox(5);
            for (CartOrder cartOrder : order.getOrders()) {
                Label label = new Label(cartOrder.getTenSach() + "(" + cartOrder.getSoLuong() + "x) - " + cartOrder.getGia() + " VND");
                vbox.getChildren().add(label);
            }
            return new SimpleObjectProperty<>(vbox);
        });
        sum.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleObjectProperty<>(order.sum());
        });
        status.setCellValueFactory(new PropertyValueFactory<CartOrder, String>("status"));
        loadData();
        function.setCellFactory(param -> new TableCell<>() {
            private final Button paidOrder = new Button("Xác nhận");
            private final Button cancelOrder = new Button("Hủy");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Order order = getTableView().getItems().get(getIndex());
                    HBox hBox = new HBox(10);
                    setStyle("-fx-alignment: CENTER;");
                    if ("Đã thanh toán".equalsIgnoreCase(order.getStatus())) {
                        paidOrder.setVisible(false);
                        cancelOrder.setVisible(false);
                    } else if ("Chờ thanh toán".equals(order.getStatus())) {
                        paidOrder.setVisible(false);
                        hBox.getChildren().add(cancelOrder);
                    } else if ("Hủy".equals(order.getStatus())) {
                        cancelOrder.setVisible(false);
                        hBox.getChildren().add(paidOrder);
                    } else {
                        cancelOrder.setVisible(true);
                        paidOrder.setVisible(true);
                        hBox.getChildren().addAll(paidOrder, cancelOrder);
                    }
                    paidOrder.setOnAction(event -> {
                        Order selectedOrder = getTableView().getItems().get(getIndex());
                        paidOrder(selectedOrder);
                    });

                    cancelOrder.setOnAction(event -> {
                        Order selectedOrder = getTableView().getItems().get(getIndex());
                        cancelOrder(selectedOrder);
                    });

                    setGraphic(hBox);
                    setText(null);
                }
            }
        });
    }

    private void paidOrder(Order order) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "update donhang set TrangThai = 'Chờ thanh toán' where MaDH = ?";
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận cập nhật");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn cập nhật trạng thái không?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setInt(1, order.getMaDH());

                    int row = preparedStatement.executeUpdate();

                    if (row > 0) {
                        Alert("Cập nhật thành công");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelOrder(Order order) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();

        String SQL = "update donhang set TrangThai = 'Hủy' where MaDH = ?";
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận cập nhật");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn cập nhật trạng thái không?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setInt(1, order.getMaDH());

                    int row = preparedStatement.executeUpdate();

                    if (row > 0) {
                        Alert("Cập nhật thành công");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai , NguoiDung.Ten " +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung ";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQl);
            orders.clear();
            while (resultSet.next()) {

                int maDh = resultSet.getInt("MaDH");
                Timestamp date = resultSet.getTimestamp("NgayDat");
                String name = resultSet.getString("Ten");
                String status = resultSet.getString("TrangThai");

                Order order = new Order(maDh, date, name, status);

                getProductInformation(connection, maDh, resultSet, order);
            }
            orderTableView.setItems(orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getProductInformation(Connection connection, int maDh, ResultSet resultSet, Order order) throws SQLException {
        try {

            String SQL_1 = "select GioHang.Ten, GioHang.SoLuong, GioHang.Gia from GioHang \n" +
                    "join GioHang_DonHang on GioHang.MaGH = GioHang_DonHang.MaGH \n" +
                    "join DonHang on DonHang.MaDH = GioHang_DonHang.MaDH\n" +
                    "where DonHang.MaDH = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_1);
            preparedStatement.setInt(1, maDh);

            ResultSet resultSet1 = preparedStatement.executeQuery();
            List<CartOrder> cartOrders = new ArrayList<>();

            while (resultSet1.next()) {
                String nameOrder = resultSet1.getString("Ten");
                int quantity = resultSet1.getInt("SoLuong");
                BigDecimal price = resultSet1.getBigDecimal("Gia");

                cartOrders.add(new CartOrder(nameOrder, quantity, price));
            }
            order.setOrders(FXCollections.observableArrayList(cartOrders));
            orders.add(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cập nhật");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> alert.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void confirmOder(MouseEvent mouseEvent) {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai , NguoiDung.Ten " +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung " +
                    " where DonHang.TrangThai = 'Chờ xác nhận'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQl);

            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không có đơn hàng nào đang chờ xác nhận.");
                alert.showAndWait();
            } else {
                orders.clear();
                while (resultSet.next()) {
                    int maDh = resultSet.getInt("MaDH");
                    Timestamp date = resultSet.getTimestamp("NgayDat");
                    String name = resultSet.getString("Ten");
                    String status = resultSet.getString("TrangThai");

                    Order order = new Order(maDh, date, name, status);
                    getProductInformation(connection, maDh, resultSet, order);
                }
            }
            orderTableView.setItems(orders);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void confirmedOder(MouseEvent mouseEvent) {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai , NguoiDung.Ten " +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung " +
                    " where DonHang.TrangThai = 'Chờ thanh toán'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQl);

            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không có đơn hàng nào đang chờ thanh toán.");
                alert.showAndWait();
            } else {
                orders.clear();

                while (resultSet.next()) {
                    int maDh = resultSet.getInt("MaDH");
                    Timestamp date = resultSet.getTimestamp("NgayDat");
                    String name = resultSet.getString("Ten");
                    String status = resultSet.getString("TrangThai");

                    Order order = new Order(maDh, date, name, status);
                    getProductInformation(connection, maDh, resultSet, order);
                }
            }
            orderTableView.setItems(orders);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelOrder(MouseEvent mouseEvent) {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai , NguoiDung.Ten " +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung " +
                    " where DonHang.TrangThai = 'Hủy'";


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQl);

            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không có đơn hàng nào hủy.");
                alert.showAndWait();
            } else {
                orders.clear();

                while (resultSet.next()) {
                    int maDh = resultSet.getInt("MaDH");
                    Timestamp date = resultSet.getTimestamp("NgayDat");
                    String name = resultSet.getString("Ten");
                    String status = resultSet.getString("TrangThai");

                    Order order = new Order(maDh, date, name, status);
                    getProductInformation(connection, maDh, resultSet, order);
                }
            }
            orderTableView.setItems(orders);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
