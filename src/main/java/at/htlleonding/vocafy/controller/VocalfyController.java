package at.htlleonding.vocafy.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.Playlist;
import model.Song;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VocalfyController {
    @FXML
    private Label volumeIcon;
    @FXML
    private Slider progressBar;
    @FXML
    private Slider volumeSlider;
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
    private String imagePath = System.getProperty("user.dir") + "/resources/at.htlleonding.vocafy/noImage.png";
    private MediaPlayer mediaPlayer;
    private Playlist playlist;

    @FXML
    private void initialize() {
        //playlist.getSongs();
        prevButton.getStyleClass().addAll("round-button", "prev-shape");
        pauseButton.getStyleClass().addAll("round-button", "pause-shape");
        nextButton.getStyleClass().addAll("round-button", "next-shape");

        pauseButton.setOnAction(e -> {
            if (pauseButton.getStyleClass().contains("pause-shape")) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
                pauseButton.getStyleClass().remove("pause-shape");
                pauseButton.getStyleClass().add("play-shape");
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.play();
                }
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

            Media media = new Media(songPath);
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.currentTimeProperty().addListener((_, _, newValue) -> progressBar.setValue(newValue.toSeconds()));

            progressBar.setOnMousePressed((_) -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            mediaPlayer.setOnReady(() -> progressBar.setMax(media.getDuration().toSeconds()));

            volumeSlider.setValue(mediaPlayer.getVolume() * 100);

            volumeSlider.valueProperty().addListener((_) -> {
                double value = volumeSlider.getValue() / 100;
                mediaPlayer.setVolume(value);
                if(value == 0){
                    volumeIcon.setText("🔇");
                }
                else if(value > 0.7){
                    volumeIcon.setText("🔊");
                }
                else if(value > 0.4){
                    volumeIcon.setText("🔉");
                }
                else if(value > 0.1){
                    volumeIcon.setText("🔈");
                }
            });


            mediaPlayer.play();
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
        if (songPath == null || songPath.isEmpty()) {
            showAlert("Select a Song (with optional image)");
            return;
        }
        String title = new File(songPath).getName();

        Song newSong = new Song(title, songPath, imagePath);

        try {
            playlist.addSong(newSong);
            showAlert("Song added successfully!");
            songPath = null;
            imagePath = null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to add song: " + e.getMessage());
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