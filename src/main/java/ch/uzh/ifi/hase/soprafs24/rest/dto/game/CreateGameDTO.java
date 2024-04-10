package ch.uzh.ifi.hase.soprafs24.rest.dto.game;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class CreateGameDTO {

    @NotNull(message = "Creator is required")
    private String creator;

    @Min(value = 2, message = "Maximum players must be at least 2")
    private int maxPlayers;

    @Min(value = 1, message = "# of Rounds must be at least 1")
    private int roundCount;

    @NotNull(message = "Game type is required")
    private GameType gameType;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
}
