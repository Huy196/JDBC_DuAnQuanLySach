module com.example.duanquanlysach {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.duanquanlysach to javafx.fxml;
    exports com.example.duanquanlysach;
}