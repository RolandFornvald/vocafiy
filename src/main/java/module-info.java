module com.example.vocafy {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.vocafy to javafx.fxml;
    exports com.example.vocafy;
}