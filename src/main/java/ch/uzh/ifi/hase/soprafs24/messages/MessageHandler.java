package ch.uzh.ifi.hase.soprafs24.messages;
 
import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.PlayerInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CitiesGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.stereotype.Service;
import java.util.Optional;
 
import java.util.List;
 
@Service
public class MessageHandler {
 
    private final UserRepository userRepository;
 
    private final GameRepository gameRepository;
 
    private final GameService gameService;
 
    private final UserService userService;
 
    public MessageHandler(UserRepository userRepository, GameRepository gameRepository, GameService gameService, UserService userService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
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
            return processPlay(chatMessage);
        }
 
        else if (message.getType() == MessageType.PLAY_AGAIN) {
            @SuppressWarnings("unchecked")
            Message<String> chatMessage = (Message<String>) message;
            return processChatMessage(chatMessage);
        }
 
        else if (message.getType() == MessageType.JS) {
            @SuppressWarnings("unchecked")
            Message<String> chatMessage = (Message<String>) message;
            return processChatMessage(chatMessage);
        }
 
        else if (message.getType() == MessageType.TIMER) {
            @SuppressWarnings("unchecked")
            Message<String> chatMessage = (Message<String>) message;
            return processChatMessage(chatMessage);
        }

        else if (message.getType() == MessageType.CITY) {
            @SuppressWarnings("unchecked")
            Message<String> chatMessage = (Message<String>) message;
            return processCityMessage(chatMessage);
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
        System.out.println("CREATOR LEAVE-------------------" + message.getFrom() + " " + gameCode);
        gameService.deleteGame(message.getFrom(), gameCode);
        return message;
    }
 
    public Message<String> processLogout(Message<String> message) {
        UserTokenDTO token = new UserTokenDTO();
        token.setToken(message.getFrom());
        Optional<Game> optionalGame = gameRepository.findByGameCode(message.getContent());
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            System.out.println("-----------------LOGOUT: --------------" + message.getContent());
           
            if (message.getFrom().equals(game.getCreator())) {
                gameService.deleteGame2(message.getFrom(), message.getContent());
                userService.logout(token);
                return new Message<>(message.getFrom(), "", MessageType.LEAVE_CREATOR);
            } else {
                gameService.dumpUserAndDeleteGameIfEmpty2(message.getFrom(), message.getContent());
                userService.logout(token);
                return new Message<>(message.getFrom(), "", MessageType.LEAVE);
            }
           
        } else {
            System.out.println("Game not found for game code: " + message.getContent());
        }
       
        return message;
}
 
    public Message<String> processPlay(Message<String> message) {
        userService.processGameData(message.getContent(), message.getFrom());
        return message;
    }

    public Message<List<City>> processCityMessage(Message<String> message) {
        List<City> city = gameService.returnCities(message.getContent());
        return new Message<>(message.getFrom(), city, MessageType.CITY);
    }
}

//last push was together with @B-M and @LaughingF0x