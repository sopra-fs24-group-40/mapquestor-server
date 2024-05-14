package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CityRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CreateGameDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameStatusDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    public void testCreateGame_validInput() {
        // Given
        CreateGameDTO newGameDTO = new CreateGameDTO();
        newGameDTO.setCreator("userToken");
        newGameDTO.setMaxPlayers(4);
        newGameDTO.setRoundCount(3);
        newGameDTO.setGameType(GameType.CITY);

        User creator = new User();
        creator.setToken("userToken");

        when(userRepository.findByToken("userToken")).thenReturn(Optional.of(creator));
        when(cityRepository.findAll()).thenReturn(Collections.singletonList(new City()));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game game = invocation.getArgument(0);
            game.setGameId(1L);
            return game;
        });

        // When
        Game createdGame = gameService.createGame(newGameDTO);

        // Then
        assertNotNull(createdGame);
        assertEquals(newGameDTO.getMaxPlayers(), createdGame.getMaxPlayers());
        assertEquals(newGameDTO.getRoundCount(), createdGame.getRoundCount());
        assertEquals(newGameDTO.getGameType(), createdGame.getGameType());
        assertEquals(GameStatus.LOBBY, createdGame.getGameStatus());
        assertEquals(3, createdGame.getCities().size());
        assertEquals(creator.getToken(), createdGame.getCreator());
        assertEquals(1, createdGame.getPlayerCount());
        assertTrue(createdGame.getPlayers().contains(creator));
    }

    @Test
    public void testCreateGame_userNotFound() {
        // Given
        CreateGameDTO newGameDTO = new CreateGameDTO();
        newGameDTO.setCreator("userToken");

        when(userRepository.findByToken("userToken")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(newGameDTO));
    }
  

    @Test
    public void testJoinGame_validInput() {
        // Given
        String gameCode = "ABC123";
        String userToken = "userToken";

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(1); // One player already joined
        game.setGameStatus(GameStatus.LOBBY);

        User user = new User();
        user.setToken(userToken);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
        when(userRepository.findByToken(userToken)).thenReturn(Optional.of(user));

        // When
        gameService.joinGame(gameCode, userToken);

        // Then
        assertNotNull(game);
        assertTrue(game.getPlayers().contains(user));
        assertEquals(2, game.getPlayerCount());
        assertEquals(GameStatus.LOBBY, game.getGameStatus());
    }

    @Test
    public void testJoinGame_gameNotFound() {
        // Given
        String gameCode = "ABC123";
        String userToken = "userToken";

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> gameService.joinGame(gameCode, userToken));
    }

    // Add more test cases for other methods as needed...

    @Test
    public void testUpdateGameStatus_validInput() {
        // Given
        String gameCode = "ABC123";
        GameStatusDTO gameStatusDTO = new GameStatusDTO();
        gameStatusDTO.setStatus(GameStatus.LOBBY);

        Game game = new Game();
        game.setGameCode(gameCode);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));

        // When
        GameStatusDTO updatedStatus = gameService.updateGameStatus(gameCode, gameStatusDTO);

        // Then
        assertNotNull(updatedStatus);
        assertEquals(gameStatusDTO.getStatus(), game.getGameStatus());
    }

    @Test
    public void testUpdateGameStatus_gameNotFound() {
        // Given
        String gameCode = "ABC123";
        GameStatusDTO gameStatusDTO = new GameStatusDTO();
        gameStatusDTO.setStatus(GameStatus.LOBBY);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> gameService.updateGameStatus(gameCode, gameStatusDTO));
    }
    
    @Test
    public void testCreateGame_invalidCreator() {
        // Given
        CreateGameDTO newGame = new CreateGameDTO();
        newGame.setCreator("invalidToken");

        when(userRepository.findByToken(newGame.getCreator())).thenReturn(Optional.empty());

        // Then
        assertThrows(ResponseStatusException.class, () -> {
            gameService.createGame(newGame);
        });
    }

    @Test
    public void testJoinGame_userAlreadyInGame() {
        // Given
        String gameCode = "ABC123";
        String userToken = "userToken";

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(1); // One player already joined
        game.setGameStatus(GameStatus.LOBBY);

        User user = new User();
        user.setToken(userToken);
        user.setGame(game); // User already in a game

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
        when(userRepository.findByToken(userToken)).thenReturn(Optional.of(user));

        // Then
        assertThrows(ResponseStatusException.class, () -> {
            gameService.joinGame(gameCode, userToken);
        });
    }

    @Test
    public void testDumpUserAndDeleteGameIfEmpty() {
        // Given
        String token = "userToken";
        String gameCode = "sampleGameCode";

        // Mock game and user
        Game game = new Game();
        game.setGameCode(gameCode);
        game.setPlayerCount(1); // Initial player count
        User user = new User();
        user.setToken(token);
        user.setStatus(UserStatus.INGAME);

        // Mock repository methods
        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        // When
        gameService.dumpUserAndDeleteGameIfEmpty(token, gameCode);

        // Then
        // Verify that the user was removed from the game
        verify(gameRepository).save(argThat(savedGame -> savedGame.getPlayers().isEmpty()));

        // Verify that the player count was decremented
        assertEquals(0, game.getPlayerCount());

        // Verify that the user status was updated
        assertEquals(UserStatus.OFFLINE, user.getStatus());
    }

}