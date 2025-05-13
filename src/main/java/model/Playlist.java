package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Playlist {
    private final ObservableList<Song> songs;
    private final Connection connection;

    public Playlist(Connection connection) {
        this.songs = FXCollections.observableArrayList();
        this.connection = connection;
        loadSongsFromDatabase();
    }

    public ObservableList<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        try {
            String sql = "INSERT INTO songs (title, songPath, imagePath) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, song.getSongTitle());
            stmt.setString(2, song.getSongPath());
            stmt.setString(3, song.getImagePath());
            stmt.executeUpdate();

            songs.add(song);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSongsFromDatabase() {
        try {
            String sql = "SELECT * FROM songs";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String title = rs.getString("title");
                String path = rs.getString("path");
                String imagePath = rs.getString("imagePath");

                Song song = new Song(title, path, imagePath);
                songs.add(song);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
