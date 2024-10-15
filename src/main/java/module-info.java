module com.example.duanquanlysach {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.duanquanlysach to javafx.fxml;
    exports com.example.duanquanlysach;
}