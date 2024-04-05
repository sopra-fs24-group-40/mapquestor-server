package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.game.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameSocketController {

    @MessageMapping("/{gameId}/chat")
    @SendTo("/topic/{gameId}/chat")
    public ChatMessage sendChatMessage(@DestinationVariable String gameId, ChatMessage message) {
        MessageType type = message.getType();
        return message;
    }

}
