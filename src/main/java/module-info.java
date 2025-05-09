module com.example.vocafy {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.htlleonding.vocafy to javafx.fxml;
    exports at.htlleonding.vocafy;
}