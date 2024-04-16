package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.messages.Message;
import ch.uzh.ifi.hase.soprafs24.messages.MessageHandler;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CityDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameStatusDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ch.uzh.ifi.hase.soprafs24.entity.City;

@Controller
public class GameWebSocketController {

    private final GameService gameService;
    private final UserService userService;

    private final MessageHandler messageHandler;

    GameWebSocketController(GameService gameService, UserService userService, MessageHandler messageHandler) {
        this.gameService = gameService;
        this.userService = userService;
        this.messageHandler = messageHandler;
    }

    @MessageMapping("/{gameId}/chat")
    @SendTo("/topic/{gameId}/chat")
    public Message<?> sendChatMessage(@DestinationVariable String gameId, Message<?> message) {
        return messageHandler.handleMessage(message, gameId);
    }

    @MessageMapping("/{gameId}/gameState")
    @SendTo("/topic/{gameId}/gameState")
    public GameStatusDTO startGame(@DestinationVariable String gameId, GameStatusDTO gameStatus) {
        return gameService.updateGameStatus(gameId, gameStatus);
    }

    // @MessageMapping("/{gameId}/sendCityData")
    // @SendTo("/topic/{gameId}/cityData")
    // public City sendCityData(@DestinationVariable String gameId) {
    //     return gameService.sendRandomCityData(gameId);
    // }

}
