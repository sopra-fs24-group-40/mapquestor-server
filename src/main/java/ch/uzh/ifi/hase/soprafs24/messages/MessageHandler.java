package ch.uzh.ifi.hase.soprafs24.messages;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.PlayerInfoDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageHandler {

    private final UserRepository userRepository;
    private final GameService gameService;

    public MessageHandler(UserRepository userRepository, GameService gameService) {
        this.userRepository = userRepository;
        this.gameService = gameService;
    }

    public Message<?> handleMessage(Message<?> message, String gameCode) {
        if (message.getType() == MessageType.CHAT) {
            @SuppressWarnings("unchecked")
            Message<String> chatMessage = (Message<String>) message;

            return processChatMessage(chatMessage);
        }
        else if (message.getType() == MessageType.CHAT_INGAME) {
            @SuppressWarnings("unchecked")
            Message<String> chatMessage = (Message<String>) message;

            return processChatMessage(chatMessage);
        }
        else if (message.getType() == MessageType.JOIN) {
            @SuppressWarnings("unchecked")
            Message<String> joinMessage = (Message<String>) message;
            return processJoinMessage(joinMessage);
        }
        else if (message.getType() == MessageType.START_COUNTDOWN) {
            return message;
        }

        else if (message.getType() == MessageType.POINTS) {
            System.out.println("debug:" + message);
            @SuppressWarnings("unchecked")

            Message<List<PlayerInfoDTO>> pointsMessage = (Message<List<PlayerInfoDTO>>) message;
            return pointsMessage;
        }

        else if (message.getType() == MessageType.LEAVE) {
            @SuppressWarnings("unchecked")
            Message<String> leavMessage = (Message<String>) message;
            return porcessLeaveMessage(leavMessage, gameCode);
        }

        else if (message.getType() == MessageType.JOKER) {
            @SuppressWarnings("unchecked")
            Message<String> jokerMessage = (Message<String>) message;
            System.out.println("Joker message received: ");
            return jokerMessage;
        }

        else {
            throw new IllegalArgumentException("Unsupported message type: " + message.getType());
        }
    }

    private Message<String> processChatMessage(Message<String> message) {
        return message;
    }

    public Message<PlayerInfoDTO> processJoinMessage(Message<String> message) {
        PlayerInfoDTO playerInfo = new PlayerInfoDTO();
        playerInfo.setToken(message.getFrom());
        playerInfo.setUsername(userRepository.findByToken(message.getFrom()).get().getUsername());
        playerInfo.setPoints(0);

        return new Message<>(message.getFrom(), playerInfo, MessageType.JOIN);
    }

    public Message<List<PlayerInfoDTO>> processPointsMessage(Message<List<PlayerInfoDTO>> message) {
        return message;
    }

    public Message<String> porcessLeaveMessage(Message<String> message, String gameCode) {
        System.out.println("User " + message.getFrom() + " left game " + gameCode);
        gameService.dumpUserAndDeleteGameIfEmpty(message.getFrom(), gameCode);
        return message;
    }
}
