package ch.uzh.ifi.hase.soprafs24.messages;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.PlayerInfoDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void testProcessJoinMessage_ValidMessage() {
        // Prepare
        Message<String> joinMessage = new Message<>("token", "sender", MessageType.JOIN);
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByToken(joinMessage.getFrom())).thenReturn(Optional.of(user));

        // Test
        Message<PlayerInfoDTO> result = messageHandler.processJoinMessage(joinMessage);

        // Verify
        assertEquals("testUser", result.getContent().getUsername());
        assertEquals(0, result.getContent().getPoints());
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

//    @Test
//    public void handleMessage_ShouldReturnSamePointsMessage_WhenMessageTypeIsPointsAndContentIsEmpty() {
//        // Prepare
//        Message<List<PlayerInfoDTO>> pointsMessage = new Message<>(Collections.emptyList(), "sender", MessageType.POINTS);
//
//        // Test
//        Message<?> result = messageHandler.handleMessage(pointsMessage, "gameCode");
//
//        // Verify
//        assertEquals(pointsMessage, result);
//    }

    @Test
    public void testProcessLeaveMessage_ValidMessage() {
        // Prepare
        Message<String> leaveMessage = new Message<>("token", "sender", MessageType.LEAVE);
        when(userRepository.findByToken(leaveMessage.getFrom())).thenReturn(Optional.of(new User()));

        // Test
        Message<String> result = messageHandler.processLeaveMessage(leaveMessage, "gameCode");

        // Verify
        verify(gameService, times(1)).dumpUserAndDeleteGameIfEmpty(eq("token"), eq("gameCode")); // Update expected arguments here
        assertEquals(null, result.getFrom());
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
    public void testProcessCreatorLeaveMessage() {
        // Prepare
        Message<String> logoutCreatorMessage = new Message<>("Test message", "sender", MessageType.LOGOUT_CREATOR);
        String gameCode = "gameCode";

        // Test
        Message<String> result = messageHandler.processCreatorLeaveMessage(logoutCreatorMessage, gameCode);

        // Verify
        verify(gameService, times(1)).deleteGame(logoutCreatorMessage.getFrom(), gameCode);
        assertEquals(logoutCreatorMessage, result);
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
    public void testProcessPlay() {
        // Prepare
        Message<String> playedMessage = new Message<>("Test message", "sender", MessageType.PLAYED);

        // Test
        Message<String> result = messageHandler.processPlay(playedMessage);

        // Verify
        verify(userService, times(1)).processGameData(playedMessage.getContent(), playedMessage.getFrom());
        assertEquals(playedMessage, result);
    }

    @Test
    public void testProcessChatMessage() {
        // Prepare
        Message<String> chatMessage = new Message<>("Test message", "sender", MessageType.CHAT);

        // Test
        Message<String> result = messageHandler.processChatMessage(chatMessage);

        // Verify
        assertEquals(chatMessage, result);
    }

    @Test
    public void testProcessJSMessage() {
        // Prepare
        Message<String> jsMessage = new Message<>("Test message", "sender", MessageType.JS);

        // Test
        Message<String> result = messageHandler.processChatMessage(jsMessage);

        // Verify
        assertEquals(jsMessage, result);
    }

    @Test
    public void testProcessTimerMessage() {
        // Prepare
        Message<String> timerMessage = new Message<>("Test message", "sender", MessageType.TIMER);

        // Test
        Message<String> result = messageHandler.processChatMessage(timerMessage);

        // Verify
        assertEquals(timerMessage, result);
    }

    @Test
    public void testProcessPlayersMessage() {
        // Prepare
        Message<String> playersMessage = new Message<>("Test message", "sender", MessageType.PLAYERS);

        // Test
        Message<String> result = messageHandler.processChatMessage(playersMessage);

        // Verify
        assertEquals(playersMessage, result);
    }
}
