package at.htlleonding.vocafy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class VocalfyController {

    @FXML
    private void onSelectSongClicked() {
        new Alert(Alert.AlertType.INFORMATION, "Vocalfy song clicked").show();
    }


}