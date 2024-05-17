package ch.uzh.ifi.hase.soprafs24.messages;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.PlayerInfoDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MessageHandlerTest {

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageHandler messageHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleMessage_ChatMessage() {
        // Prepare
        Message<String> chatMessage = new Message<>("Test message", "sender", MessageType.CHAT);

        // Test
        Message<?> result = messageHandler.handleMessage(chatMessage, "gameCode");

        // Verify
        assertEquals(chatMessage, result);
    }

    @Test
    public void testHandleMessage_ChatInGameMessage() {
        // Prepare
        Message<String> chatMessage = new Message<>("Test message", "sender", MessageType.CHAT_INGAME);

        // Test
        Message<?> result = messageHandler.handleMessage(chatMessage, "gameCode");

        // Verify
        assertEquals(chatMessage, result);
    }

    @Test
    public void testHandleMessage_ChatInGameCorrectMessage() {
        // Prepare
        Message<String> chatMessage = new Message<>("Test message", "sender", MessageType.CHAT_INGAME_CORRECT);

        // Test
        Message<?> result = messageHandler.handleMessage(chatMessage, "gameCode");

        // Verify
        assertEquals(chatMessage, result);
    }

    @Test
    public void handleMessageReturnsJoinMessageWhenMessageTypeIsJoin() {
        // Arrange
        String token = "sender";
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setToken(token);
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        Message<String> joinMessage = new Message<>(token, "content", MessageType.JOIN);

        // Act
        Message<?> result = messageHandler.handleMessage(joinMessage, "gameCode");

        // Assert
        Assertions.assertEquals(joinMessage.getType(), result.getType());
    }

    @Test
    public void testHandleMessage_StartCountdownMessage() {
        // Prepare
        Message<String> startCountdownMessage = new Message<>("Test message", "sender", MessageType.START_COUNTDOWN);

        // Test
        Message<?> result = messageHandler.handleMessage(startCountdownMessage, "gameCode");

        // Verify
        assertEquals(startCountdownMessage, result);
    }

    // Add more test cases for other message types...

    @Test
    public void handleMessageReturnsSamePointsMessageWhenMessageTypeIsPoints() {
        List<PlayerInfoDTO> playerInfoList = new ArrayList<>();
        playerInfoList.add(new PlayerInfoDTO());
        playerInfoList.add(new PlayerInfoDTO());
        Message<List<PlayerInfoDTO>> pointsMessage = new Message<>("sender", playerInfoList, MessageType.POINTS);

        Message<?> result = messageHandler.handleMessage(pointsMessage, "gameCode");

        Assertions.assertEquals(pointsMessage, result);
    }

    @Test
    public void handleMessageReturnsSameLeaveMessageWhenMessageTypeIsLeave() {
        Message<String> leaveMessage = new Message<>("sender", "content", MessageType.LEAVE);

        Message<?> result = messageHandler.handleMessage(leaveMessage, "gameCode");

        Assertions.assertEquals(leaveMessage, result);
    }

    @Test
    public void testHandleMessage_LeaveCreatorMessage() {
        // Prepare
        Message<String> leaveCreatorMessage = new Message<>("Test message", "sender", MessageType.LEAVE_CREATOR);

        // Test
        Message<?> result = messageHandler.handleMessage(leaveCreatorMessage, "gameCode");

        // Verify
        verify(gameService, times(1)).deleteGame(eq(leaveCreatorMessage.getFrom()), eq("gameCode"));
        assertEquals(leaveCreatorMessage, result); // Ensure that the returned message is the same as the input message
    }

    @Test
    public void testHandleMessage_LogoutMessage() {
        // Prepare
        Message<String> logoutMessage = new Message<>("Test message", "sender", MessageType.LOGOUT);

        // Test
        Message<?> result = messageHandler.handleMessage(logoutMessage, "gameCode");

        // Verify
        assertEquals(logoutMessage, result);
    }

    @Test
    public void handleMessageReturnsSameMessageWhenMessageTypeIsLogoutCreator() {
        Message<String> logoutCreatorMessage = new Message<>("sender", "content", MessageType.LOGOUT_CREATOR);

        Message<?> result = messageHandler.handleMessage(logoutCreatorMessage, "gameCode");

        Assertions.assertEquals(logoutCreatorMessage, result);
    }

    @Test
    public void testHandleMessage_JokerMessage() {
        // Prepare
        Message<String> jokerMessage = new Message<>("Test message", "sender", MessageType.JOKER);

        // Test
        Message<?> result = messageHandler.handleMessage(jokerMessage, "gameCode");

        // Verify
        assertEquals(jokerMessage, result);
    }

    @Test
    public void handleMessageReturnsSameMessageWhenMessageTypeIsPlayed() {
        Message<String> playedMessage = new Message<>("sender", "content", MessageType.PLAYED);

        Message<?> result = messageHandler.handleMessage(playedMessage, "gameCode");

        Assertions.assertEquals(playedMessage, result);
    }

    @Test
    public void handleMessageReturnsSameMessageWhenMessageTypeIsPlayAgain() {
        Message<String> playAgainMessage = new Message<>("sender", "content", MessageType.PLAY_AGAIN);

        Message<?> result = messageHandler.handleMessage(playAgainMessage, "gameCode");

        Assertions.assertEquals(playAgainMessage, result);
    }

    @Test
    public void handleMessageReturnsSameMessageWhenMessageTypeIsJS() {
        Message<String> jsMessage = new Message<>("sender", "content", MessageType.JS);

        Message<?> result = messageHandler.handleMessage(jsMessage, "gameCode");

        Assertions.assertEquals(jsMessage, result);
    }

    @Test
    public void handleMessageReturnsSameMessageWhenMessageTypeIsTimer() {
        Message<String> timerMessage = new Message<>("sender", "content", MessageType.TIMER);

        Message<?> result = messageHandler.handleMessage(timerMessage, "gameCode");

        Assertions.assertEquals(timerMessage, result);
    }

    // @Test
    // public void handleMessageReturnsSameMessageWhenMessageTypeIsPlayers() {
    //     Message<String> playersMessage = new Message<>("sender", "content", MessageType.PLAYERS);

    //     Message<?> result = messageHandler.handleMessage(playersMessage, "gameCode");

    //     Assertions.assertEquals(playersMessage, result);
    // }


}
