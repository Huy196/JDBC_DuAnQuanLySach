package com.example.duanquanlysach;

import ConnectionDatabase.ConnectionDatabase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    @FXML
    private VBox contentArea;
    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product,String> anhColumn;
    @FXML
    private TableColumn<Product, String> nameBookColumn;
    @FXML
    private TableColumn<Product, String> authorColumn;
    @FXML
    private TableColumn<Product, String> contentColumn;
    @FXML
    private TableColumn<Product, Integer> yearXBColumn;
    @FXML
    private TableColumn<Product, Integer> ghostNXBColumn;
    @FXML
    private TableColumn<Product, Double> priceBookColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Integer> codeBookColumn;
    @FXML
    private TableColumn<Product, String> statusColumn;
    @FXML
    private TableColumn<Product, String> functionColumn;
    private ObservableList<Product> products = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("maSach"));
        anhColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("Anh"));
        anhColumn.setCellFactory(column -> new TableCell<Product,String>() {
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
        nameBookColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("tenSach"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("tacGia"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("noiDung"));
        yearXBColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("namXB"));
        ghostNXBColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("maNXB"));
        priceBookColumn.setCellValueFactory(new PropertyValueFactory<Product,Double>("giaSach"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("soLuong"));
        codeBookColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("maLoaiSach"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("trangThai"));

        loadData();

        functionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Sửa");
            private final Button deleteButton = new Button("Xóa");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox hBox = new HBox(editButton, deleteButton);
                    hBox.setSpacing(10);

                    editButton.setOnAction(event -> {
                        Product product = getTableView().getItems().get(getIndex());
                        editProduct();
                    });

                    deleteButton.setOnAction(event -> {
                        Product product = getTableView().getItems().get(getIndex());
                        deleteProduct(product);
                    });

                    setGraphic(hBox);
                    setText(null);
                }
            }
        });
    }

    public void editProduct() {
        try {
            Main.changeScene("Admin_UpdateProduct.fxml");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteProduct(Product product) {
        System.out.println("Xóa sản phẩm: ");
    }

    public void loadData() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            connectionDatabase.connection();
            var connection = connectionDatabase.connection();

            String SQL = "SELECT * FROM sach";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);


            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("MaSach"),
                        resultSet.getString("Anh"),
                        resultSet.getString("TenSach"),
                        resultSet.getString("TacGia"),
                        resultSet.getString("NoiDung"),
                        resultSet.getInt("NamXuatBan"),
                        resultSet.getInt("MaNXB"),
                        resultSet.getDouble("GiaSach"),
                        resultSet.getInt("SoLuong"),
                        resultSet.getInt("MaLoaiSach"),
                        resultSet.getString("TrangThai")
                );
                products.add(product);
            }
            tableView.setItems(products);
        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận đăng xuất");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn đăng xuất không?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Main.changeScene("Login.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProduct() {
        try {
            contentArea.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductAdmin.fxml"));
            Parent parent = loader.load();

            contentArea.getChildren().add(parent);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addProduct(){
        try {
            Main.changeScene("Admin_AddProduct.fxml");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

