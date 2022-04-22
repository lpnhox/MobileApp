package vu.htr.cs.muzikapp.favourites;

public class Favourite {
    private String userEmail;
    private String songId;

    public String getUserEmail() {
        return userEmail;
    }

    public Favourite() {
    }

    public Favourite(String userEmail, String songId) {
        this.userEmail = userEmail;
        this.songId = songId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
