module com.example.duanquanlysach {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.duanquanlysach to javafx.fxml;
    exports com.example.duanquanlysach;
    exports com.example.duanquanlysach.Product;
    opens com.example.duanquanlysach.Product to javafx.fxml;
    exports com.example.duanquanlysach.Login;
    opens com.example.duanquanlysach.Login to javafx.fxml;
    exports com.example.duanquanlysach.InterfaceShop;
    opens com.example.duanquanlysach.InterfaceShop to javafx.fxml;
    exports com.example.duanquanlysach.User;
    opens com.example.duanquanlysach.User to javafx.fxml;
}