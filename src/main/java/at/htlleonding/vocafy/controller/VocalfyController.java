package at.htlleonding.vocafy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VocalfyController {

    @FXML
    private Label songSelectedLabel;

    @FXML
    private Button pauseButton;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;

    @FXML
    private Label imageSelectedLabel;

    private String songPath;
    private String imagePath;

    @FXML
    private void initialize() {
        prevButton.getStyleClass().addAll("round-button", "prev-shape");
        pauseButton.getStyleClass().addAll("round-button", "pause-shape");
        nextButton.getStyleClass().addAll("round-button", "next-shape");

        pauseButton.setOnAction(e -> {
            if (pauseButton.getStyleClass().contains("pause-shape")) {
                pauseButton.getStyleClass().remove("pause-shape");
                pauseButton.getStyleClass().add("play-shape");
            } else {
                pauseButton.getStyleClass().remove("play-shape");
                pauseButton.getStyleClass().add("pause-shape");
            }
        });
    }

    @FXML
    private void onSelectSongClicked() {
        try{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            songPath = file.toURI().toString();
            String fileName = file.getName();
            String extension = getFileExtension(fileName);

            if(!extension.equals("mp3")) {
                showAlert("File extension not supported: " + extension);
                return;
            }

            songSelectedLabel.setText(fileName);
            songSelectedLabel.setVisible(true);
        }catch (Exception e){
            showAlert("Error occurred selecting song");
        }
    }

    @FXML
    private void onSelectImageClicked() {
        try{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            imagePath = file.toURI().toString();
            String fileName = file.getName();

            if(!isValidImageFile(fileName)) {
                showAlert("File extension not supported" + getFileExtension(fileName));
                return;
            }

            imageSelectedLabel.setText(fileName);
            imageSelectedLabel.setVisible(true);
        }catch (Exception e){
            showAlert("Error occurred selecting image");
        }
    }

    @FXML
    private void onAddSongClicked() {
        if (songPath == null || songPath.isEmpty() || imagePath == null || imagePath.isEmpty()) {
            showAlert("Select an Image and Song");
        }
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }

    private boolean isValidImageFile(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        Set<String> validExtensions = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "svg"));

        return validExtensions.contains(extension);
    }
}