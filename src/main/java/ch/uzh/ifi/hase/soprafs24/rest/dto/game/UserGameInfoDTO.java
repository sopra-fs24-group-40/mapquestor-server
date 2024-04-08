package ch.uzh.ifi.hase.soprafs24.rest.dto.game;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserGameInfoDTO {
    private String token;
    private String username;
    private boolean isCreator;
    private int score;

    public UserGameInfoDTO(String token, String username, boolean isCreator, int score) {
        this.token = token;
        this.username = username;
        this.isCreator = isCreator;
        this.score = score;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
