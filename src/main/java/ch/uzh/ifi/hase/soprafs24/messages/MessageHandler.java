package ch.uzh.ifi.hase.soprafs24.messages;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.PlayerInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserTokenDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageHandler {

    private final UserRepository userRepository;
    private final GameService gameService;

    private final UserService userService;

    public MessageHandler(UserRepository userRepository, GameService gameService, UserService userService) {
        this.userRepository = userRepository;
        this.gameService = gameService;
        this.userService = userService;
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

        else if (message.getType() == MessageType.CHAT_INGAME_CORRECT) {
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
            @SuppressWarnings("unchecked")

            Message<List<PlayerInfoDTO>> pointsMessage = (Message<List<PlayerInfoDTO>>) message;
            return pointsMessage;
        }

        else if (message.getType() == MessageType.LEAVE) {
            @SuppressWarnings("unchecked")
            Message<String> leaveMessage = (Message<String>) message;
            return processLeaveMessage(leaveMessage, gameCode);
        }

        else if (message.getType() == MessageType.LEAVE_CREATOR) {
            @SuppressWarnings("unchecked")
            Message<String> leaveMessage = (Message<String>) message;
            return processCreatorLeaveMessage(leaveMessage, gameCode);
        }

        else if (message.getType() == MessageType.LOGOUT) {
            @SuppressWarnings("unchecked")
            Message<String> logoutMessage = (Message<String>) message;
            return processLogout(logoutMessage);
        }

        else if (message.getType() == MessageType.LOGOUT_CREATOR) {
            @SuppressWarnings("unchecked")
            Message<String> leaveMessage = (Message<String>) message;
            return processCreatorLeaveMessage(leaveMessage, gameCode);
        }

        else if (message.getType() == MessageType.JOKER) {
            @SuppressWarnings("unchecked")
            Message<String> jokerMessage = (Message<String>) message;
            return jokerMessage;
        }

        else if (message.getType() == MessageType.PLAYED) {
            @SuppressWarnings("unchecked")
            Message<String> chatMessage = (Message<String>) message;
            if (chatMessage.getContent().equals("WON!")){
                User user = userRepository.findByToken(chatMessage.getFrom()).orElseThrow(() -> new RuntimeException("User not found"));
                user.setWonGames(user.getWonGames() + 1);
                userRepository.save(user);
            }

            return processPlay(chatMessage);
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


    public Message<String> processLeaveMessage(Message<String> message, String gameCode) {
        gameService.dumpUserAndDeleteGameIfEmpty(message.getFrom(), gameCode);
        return message;
    }

    public Message<String> processCreatorLeaveMessage(Message<String> message, String gameCode) {
        gameService.deleteGame(message.getFrom(), gameCode);
        return message;
    }

    public Message<String> processLogout(Message<String> message) {
        UserTokenDTO token = new UserTokenDTO();
        token.setToken(message.getFrom());
        userService.logout(token);
        return message;
    }
    public Message<String> processPlay(Message<String> message) {
        User user = userRepository.findByToken(message.getFrom()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPlayedGames(user.getPlayedGames() + 1);
        userRepository.save(user);
        return message;
    }
}

