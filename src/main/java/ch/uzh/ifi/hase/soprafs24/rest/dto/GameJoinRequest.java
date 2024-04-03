package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GameJoinRequest {
    private Long userId;
    private Long gameId;

    // Getter und Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
