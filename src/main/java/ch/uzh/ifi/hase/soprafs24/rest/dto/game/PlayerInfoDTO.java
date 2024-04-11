package ch.uzh.ifi.hase.soprafs24.rest.dto.game;

public class PlayerInfoDTO {
    private String username;

    private String token;

    private int points;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
