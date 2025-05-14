package at.htlleonding.vocafy.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import at.htlleonding.vocafy.model.Playlist;
import at.htlleonding.vocafy.model.Song;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VocalfyController {
    @FXML
    private TextField volumeTextField;
    @FXML
    private ImageView selectedSongImage;
    @FXML
    private Label selectedSongTitle;
    @FXML
    private ListView<Song> songsListView;
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
    private final String DEFAULT_IMAGE_PATH = getClass().getResource("/at/htlleonding/vocafy/noImage.png").toExternalForm();
    private String imagePath = DEFAULT_IMAGE_PATH;
    private MediaPlayer mediaPlayer;
    private final Playlist playlist = new Playlist();

    @FXML
    private void initialize() {
        ObservableList<Song> songs = playlist.getSongs();
        FilteredList<Song> filteredSongs = songs.sorted().filtered(null);
        songsListView.setItems(filteredSongs);

        prevButton.getStyleClass().addAll("round-button", "prev-shape");
        pauseButton.getStyleClass().addAll("round-button", "pause-shape");
        nextButton.getStyleClass().addAll("round-button", "next-shape");


        songsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            volumeTextField.textProperty().bindBidirectional(volumeSlider.valueProperty(), integerStringConverter);

            Media media = new Media(newValue.getSongPath());
            mediaPlayer = new MediaPlayer(media);

            selectedSongTitle.setText(newValue.getSongTitle());
            selectedSongImage.setImage(new Image(newValue.getImagePath()));

            mediaPlayer.currentTimeProperty().addListener((_, _, nV) -> progressBar.setValue(nV.toSeconds()));

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
        });

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
        }catch (Exception e){
            System.out.println("Error occurred selecting song");
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
            System.out.println("Error occurred selecting image");
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Song added successfully!");
            alert.setTitle("Information");
            alert.setHeaderText("Song Added");
            alert.showAndWait();

            imageSelectedLabel.setVisible(false);
            songSelectedLabel.setVisible(false);

            songPath = null;
            imagePath = DEFAULT_IMAGE_PATH;
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

    // generated using Chatgpt
    DecimalFormat integerFormat = new DecimalFormat("0");
    StringConverter<Number> integerStringConverter = new StringConverter<>() {
        @Override
        public String toString(Number object) {
            return integerFormat.format(object);
        }

        @Override
        public Number fromString(String string) {
            try {
                return integerFormat.parse(string);
            } catch (Exception e) {
                return 0;
            }
        }
    };

}