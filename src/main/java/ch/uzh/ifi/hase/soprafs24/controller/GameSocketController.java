package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.game.ChatMessage;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserGameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameSocketController {

    private final GameService gameService;
    private final UserService userService;

    GameSocketController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @MessageMapping("/{gameId}/chat")
    @SendTo("/topic/{gameId}/chat")
    public ChatMessage sendChatMessage(@DestinationVariable String gameId, ChatMessage message) {
        MessageType type = message.getType();

        if (type == MessageType.JOIN) {
            gameService.addUserToGame(message.getFrom(), gameId);
            message = new ChatMessage(userService.getUsernameByToken(message.getFrom()), message.getText(), MessageType.JOIN);

        } else {
            message = new ChatMessage(userService.getUsernameByToken(message.getFrom()), message.getText(), MessageType.CHAT);
        }

        return message;
    }

}
