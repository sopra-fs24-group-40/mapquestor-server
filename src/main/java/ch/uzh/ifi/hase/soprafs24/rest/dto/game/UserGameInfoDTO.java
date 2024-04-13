package ch.uzh.ifi.hase.soprafs24.rest.dto.game;


public class UserGameInfoDTO {
    private String gameCode;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
