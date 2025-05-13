package model;

public class Song {
    private final String songPath;
    private final String imagePath;
    private final String songTitle;
    public Song(String title, String songPath, String imagePath) {
        this.songTitle = title;
        this.songPath = songPath;
        this.imagePath = imagePath;
    }
    public String getSongPath() {
        return songPath;
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getSongTitle() {
        return songTitle;
    }

    @Override
    public String toString() {
        return songTitle;
    }
}
