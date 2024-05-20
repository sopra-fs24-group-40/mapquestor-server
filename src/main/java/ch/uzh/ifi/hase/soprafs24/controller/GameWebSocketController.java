package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.messages.Message;
import ch.uzh.ifi.hase.soprafs24.messages.MessageHandler;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CitiesGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CitiesPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CityDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameStatusDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ch.uzh.ifi.hase.soprafs24.entity.City;

@Controller
public class GameWebSocketController {

    private final GameService gameService;


    private final MessageHandler messageHandler;

    GameWebSocketController(GameService gameService, MessageHandler messageHandler) {
        this.gameService = gameService;
        this.messageHandler = messageHandler;
    }

    @MessageMapping("/{gameId}/chat")
    @SendTo("/topic/{gameId}/chat")
    public Message<?> sendChatMessage(@DestinationVariable String gameId, Message<?> message) {
        return messageHandler.handleMessage(message, gameId);
    }

    @MessageMapping("/logout")
    @SendTo("/topic/logout")
    public Message<?> sendLogoutMessage(Message<?> message) {
        return messageHandler.handleMessage(message, null);
    }

    @MessageMapping("/{gameId}/gameState")
    @SendTo("/topic/{gameId}/gameState")
    public GameStatusDTO startGame(@DestinationVariable String gameId, GameStatusDTO gameStatus) {
        return gameService.updateGameStatus(gameId, gameStatus);
    }

    @MessageMapping("/cities")
    @SendTo("/topic/{gameId}/cities")
    public Message<?> sendCities(Message<?> message) {
        return messageHandler.handleMessage(message, null);
    }


}
