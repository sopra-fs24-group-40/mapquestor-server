package ch.uzh.ifi.hase.soprafs24.rest.dto.game;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;

import java.util.List;

public class GameInfoDTO {


    private String gameCode;

    private String creator;

    private int playerCount;

    private int maxPlayers;

    private int roundCount;

    private GameType gameType;

    private GameStatus gameStatus;

    private List<PlayerInfoDTO> players;


    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
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

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<PlayerInfoDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInfoDTO> players) {
        this.players = players;
    }
}
