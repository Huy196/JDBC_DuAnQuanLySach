module com.example.duanquanlysach {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.duanquanlysach to javafx.fxml;
    exports com.example.duanquanlysach;
}