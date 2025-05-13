module at.htlleonding.vocafy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;


    opens at.htlleonding.vocafy.controller to javafx.fxml;
    opens at.htlleonding.vocafy to javafx.fxml;

    exports at.htlleonding.vocafy;
    exports at.htlleonding.vocafy.controller;
}