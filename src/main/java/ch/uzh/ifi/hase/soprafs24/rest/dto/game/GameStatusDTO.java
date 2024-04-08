package ch.uzh.ifi.hase.soprafs24.rest.dto.game;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;

public class GameStatusDTO {
    private GameStatus status;

    // Getter und Setter
    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}