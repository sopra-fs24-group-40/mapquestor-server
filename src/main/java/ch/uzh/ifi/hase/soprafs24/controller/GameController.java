package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameJoinRequest;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/game/join")
    @SendTo("/topic/games")
    public Game joinGame(GameJoinRequest request) {
        boolean success = gameService.addUserToGame(request.getUserId(), request.getGameId());
        if (success) {
            Game updatedGame = gameService.findById(request.getGameId());
            return updatedGame;
        }
        else {
            return null;
        }
    }

    @MessageMapping("/game/create")
    @SendTo("/topic/games")
    public Game createGame() {
        return gameService.createGame();
    }


}
