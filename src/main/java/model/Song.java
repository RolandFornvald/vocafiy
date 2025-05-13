package model;

public class Song {
    private final String songPath;
    private final String imagePath;
    public Song(String title, String songPath, String imagePath) {
        this.songPath = songPath;
        this.imagePath = imagePath;
    }
    public String getSongPath() {
        return songPath;
    }
    public String getImagePath() {
        return imagePath;
    }
}
