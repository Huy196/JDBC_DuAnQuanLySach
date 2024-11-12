package com.example.duanquanlysach.Order;

import ConnectionDatabase.ConnectionDatabase;
import com.example.duanquanlysach.CartOrder.CarController;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.sound.midi.MidiFileFormat;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Order_User_Controller implements Initializable {
    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableColumn<Order, Integer> maDH;
    @FXML
    private TableColumn<Order, Timestamp> date;
    @FXML
    private TableColumn<Order, VBox> products;
    @FXML
    private TableColumn<Order, BigDecimal> sum;
    @FXML
    private TableColumn<Order, String> status;
    @FXML
    private TableColumn<Order, String> function;
    private ObservableList<Order> orders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orders = FXCollections.observableArrayList();

        maDH.setCellValueFactory(new PropertyValueFactory<Order, Integer>("maDH"));
        date.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("date"));
        products.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            VBox vbox = new VBox(5);
            for (CartOrder cartOrder : order.getOrders()) {
                Label label = new Label(cartOrder.getTenSach() + "(" + cartOrder.getSoLuong() + "x) - " + cartOrder.getGia() + " VND");
                vbox.getChildren().add(label);
            }
            return new SimpleObjectProperty<>(vbox);
        });
        status.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));

        sum.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleObjectProperty<>(order.sum());
        });

        loadData();

        function.setCellFactory(param -> new TableCell<>() {
            private final Button payOrder = new Button("Thanh toán");
            private final Button cancelOrder = new Button("Hủy");

            private final Button updateOrder = new Button("Cập nhật");

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
                    if ("Đã thanh toán".equals(order.getStatus())) {
                        cancelOrder.setVisible(false);
                        payOrder.setVisible(false);
                    } else if ("Hủy".equals(order.getStatus())) {
                        cancelOrder.setVisible(false);
                        payOrder.setVisible(false);
                        updateOrder.setVisible(true);
                        hBox.getChildren().add(updateOrder);
                    } else if ("Chờ xác nhận".equals(order.getStatus())) {
                        cancelOrder.setVisible(true);
                        payOrder.setVisible(false);
                        hBox.getChildren().add(cancelOrder);
                    } else {
                        cancelOrder.setVisible(true);
                        payOrder.setVisible(true);
                        hBox.getChildren().addAll(payOrder, cancelOrder);
                    }
                    payOrder.setOnAction(event -> {
                        Order selectedOrder = getTableView().getItems().get(getIndex());
                        payOrder(selectedOrder);
                    });

                    cancelOrder.setOnAction(event -> {
                        Order selectedOrder = getTableView().getItems().get(getIndex());
                        cancelOrder(selectedOrder);
                    });
                    updateOrder.setOnAction(event -> {
                        Order selectedOrder = getTableView().getItems().get(getIndex());
                        updateOrder(selectedOrder);
                    });
                    setGraphic(hBox);
                    setText(null);
                }
            }
        });
    }

    public void loadData() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai" +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung " +
                    " where DonHang.MaNguoiDung = ?";

            CarController controller = new CarController();
            int maNguoiDung = controller.getMaNguoiDung_1();

            PreparedStatement preparedStatement = connection.prepareStatement(SQl);
            preparedStatement.setInt(1, maNguoiDung);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                Alert("Không có đơn hàng nào!");
            } else {
                orders.clear();
                while (resultSet.next()) {
                    int maDh = resultSet.getInt("MaDH");
                    Timestamp date = resultSet.getTimestamp("NgayDat");
                    String trangThai = resultSet.getString("TrangThai");

                    Order order = new Order(maDh, date, trangThai);
                    getProductInformation(connection, maDh, resultSet, order);
                }
            }
            orderTableView.setItems(orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDataCancel() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai" +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung " +
                    " where DonHang.MaNguoiDung = ? and DonHang.TrangThai = 'Hủy'";

            CarController controller = new CarController();
            int maNguoiDung = controller.getMaNguoiDung_1();

            PreparedStatement preparedStatement = connection.prepareStatement(SQl);
            preparedStatement.setInt(1, maNguoiDung);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                Alert("Không có đơn hàng nào đã hủy");
            } else {
                orders.clear();
                while (resultSet.next()) {
                    int maDh = resultSet.getInt("MaDH");
                    Timestamp date = resultSet.getTimestamp("NgayDat");
                    String trangThai = resultSet.getString("TrangThai");

                    Order order = new Order(maDh, date, trangThai);
                    getProductInformation(connection, maDh, resultSet, order);
                }
            }
            orderTableView.setItems(orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDataOderComplete() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai" +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung " +
                    " where DonHang.MaNguoiDung = ? and DonHang.TrangThai = 'Đã thanh toán'";

            CarController controller = new CarController();
            int maNguoiDung = controller.getMaNguoiDung_1();

            PreparedStatement preparedStatement = connection.prepareStatement(SQl);
            preparedStatement.setInt(1, maNguoiDung);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                Alert("Không có đơn hàng nào đã thanh toán!");
            } else {
                orders.clear();
                while (resultSet.next()) {
                    int maDh = resultSet.getInt("MaDH");
                    Timestamp date = resultSet.getTimestamp("NgayDat");
                    String trangThai = resultSet.getString("TrangThai");

                    Order order = new Order(maDh, date, trangThai);
                    getProductInformation(connection, maDh, resultSet, order);
                }
            }
            orderTableView.setItems(orders);
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDataOderWaitingForPayment() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            var connection = connectionDatabase.connection();
            String SQl = "select DonHang.MaDH, DonHang.NgayDat,DonHang.TrangThai" +
                    " from DonHang" +
                    " join NguoiDung on DonHang.MaNguoiDung = NguoiDung.MaNguoiDung " +
                    " where DonHang.MaNguoiDung = ? and DonHang.TrangThai = 'Chờ thanh toán'";

            CarController controller = new CarController();
            int maNguoiDung = controller.getMaNguoiDung_1();

            PreparedStatement preparedStatement = connection.prepareStatement(SQl);
            preparedStatement.setInt(1, maNguoiDung);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                Alert("Không có đơn hàng nào chờ thanh toán!");
            } else {
                orders.clear();
                while (resultSet.next()) {
                    int maDh = resultSet.getInt("MaDH");
                    Timestamp date = resultSet.getTimestamp("NgayDat");
                    String trangThai = resultSet.getString("TrangThai");

                    Order order = new Order(maDh, date, trangThai);
                    getProductInformation(connection, maDh, resultSet, order);
                }
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

    private void payOrder(Order selectedOrder) {
        Alert pay = new Alert(Alert.AlertType.CONFIRMATION);
        pay.setTitle("Chọn phương thức Thanh Toán!");
        pay.setHeaderText("Vui lòng chọn phương thức thanh toán cho đơn hàng của bạn!");

        ButtonType CK = new ButtonType("Thanh toán chuyển khoản");
        ButtonType TM = new ButtonType("Thanh toán tiền mặt");
        ButtonType cancelButton = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        pay.getButtonTypes().setAll(CK, TM, cancelButton);
        pay.showAndWait().ifPresent(response -> {
            if (response == CK) {
                handleCKPayment(selectedOrder);
            } else if (response == TM) {
                handleTMDelivery(selectedOrder);
                Alert("Thanh toán thành công");
            }
        });
    }

    private void handleCKPayment(Order selectedOrder) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        LocalDateTime time = LocalDateTime.now();

        String SQL = "update donhang set TrangThai = 'Đã thanh toán' where MaDH = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, selectedOrder.getMaDH());
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                Alert("Thanh toán thành công");
                transferMethod(selectedOrder, connection, time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void transferMethod(Order selectedOrder, Connection connection, LocalDateTime time) throws SQLException {
        CarController controller = new CarController();
        int maNguoiDung = controller.getMaNguoiDung_1();

        String SQL_1 = "insert into HoaDon(MaNguoiDung,MaDH,NgayLapHD,TongTien,PhuongThucThanhToan)" +
                "values (?,?,?,?,'Chuyển khoản')";
        PreparedStatement preparedStatement1 = connection.prepareStatement(SQL_1);
        preparedStatement1.setInt(1, maNguoiDung);
        preparedStatement1.setInt(2, selectedOrder.getMaDH());
        preparedStatement1.setTimestamp(3, Timestamp.valueOf(time));
        preparedStatement1.setBigDecimal(4, selectedOrder.sum());

        int row_1 = preparedStatement1.executeUpdate();
    }

    private void handleTMDelivery(Order selectedOrder) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        LocalDateTime time = LocalDateTime.now();

        String SQL = "update donhang set TrangThai = 'Đã thanh toán' where MaDH = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, selectedOrder.getMaDH());
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                Alert("Thanh toán thành công");
                cashMethod(selectedOrder, connection, time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cashMethod(Order selectedOrder, Connection connection, LocalDateTime time) throws SQLException {
        CarController controller = new CarController();
        int maNguoiDung = controller.getMaNguoiDung_1();

        String SQL_1 = "insert into HoaDon(MaNguoiDung,MaDH,NgayLapHD,TongTien,PhuongThucThanhToan)" +
                "values (?,?,?,?,'Tiền mặt')";
        PreparedStatement preparedStatement1 = connection.prepareStatement(SQL_1);
        preparedStatement1.setInt(1, maNguoiDung);
        preparedStatement1.setInt(2, selectedOrder.getMaDH());
        preparedStatement1.setTimestamp(3, Timestamp.valueOf(time));
        preparedStatement1.setBigDecimal(4, selectedOrder.sum());

        int row_1 = preparedStatement1.executeUpdate();
    }
    private void updateOrder(Order order){
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        LocalDateTime time = LocalDateTime.now();

        String SQL = "update donhang set TrangThai = 'Chờ xác nhận' where MaDH = ?";

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận cập nhật");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn cập nhật đơn hàng không?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setInt(1, order.getMaDH());
                int row = preparedStatement.executeUpdate();
                if (row > 0) {
                    Alert("Đã cập nhật đơn hàng");
                    cashMethod(order, connection, time);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cancelOrder(Order selectedOrder) {
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        var connection = connectionDatabase.connection();
        LocalDateTime time = LocalDateTime.now();

        String SQL = "update donhang set TrangThai = 'Hủy' where MaDH = ?";

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận cập nhật");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn hủy đơn không?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setInt(1, selectedOrder.getMaDH());
                int row = preparedStatement.executeUpdate();
                if (row > 0) {
                    Alert("Hủy đơn hàng");
                    cashMethod(selectedOrder, connection, time);
                }
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
