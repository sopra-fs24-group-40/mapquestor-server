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
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameStatusDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    public void testGetRandomCity() {
        // Given
        City city1 = new City();
        city1.setId(1L);
        city1.setName("City1");
        City city2 = new City();
        city2.setId(2L);
        city2.setName("City2");
        List<City> cities = Arrays.asList(city1, city2);

        when(cityRepository.findAll()).thenReturn(cities);

        // When
        City randomCity = gameService.getRandomCity();

        // Then
        assertTrue(cities.contains(randomCity));
    }

    @Test
    public void testGetRandomCity_ThrowsExceptionWhenNoCitiesFound() {
        // Given
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        assertThrows(IllegalStateException.class, () -> gameService.getRandomCity());
    }

//    @Test
//    public void testCreateGame_validInput() {
//        // Given
//        CreateGameDTO newGameDTO = new CreateGameDTO();
//        newGameDTO.setCreator("userToken");
//        newGameDTO.setMaxPlayers(4);
//        newGameDTO.setRoundCount(3);
//        newGameDTO.setGameType(GameType.CITY);
//
//        User creator = new User();
//        creator.setToken("userToken");
//
//        when(userRepository.findByToken("userToken")).thenReturn(Optional.of(creator));
//        when(cityRepository.findAll()).thenReturn(Collections.singletonList(new City()));
//        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
//            Game game = invocation.getArgument(0);
//            game.setGameId(1L);
//            return game;
//        });
//
//        // When
//        Game createdGame = gameService.createGame(newGameDTO);
//
//        // Then
//        assertNotNull(createdGame);
//        assertEquals(newGameDTO.getMaxPlayers(), createdGame.getMaxPlayers());
//        assertEquals(newGameDTO.getRoundCount(), createdGame.getRoundCount());
//        assertEquals(newGameDTO.getGameType(), createdGame.getGameType());
//        assertEquals(GameStatus.LOBBY, createdGame.getGameStatus());
//        assertEquals(3, createdGame.getCities().size());
//        assertEquals(creator.getToken(), createdGame.getCreator());
//        assertEquals(1, createdGame.getPlayerCount());
//        assertTrue(createdGame.getPlayers().contains(creator));
//    }

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
    public void testCreateGame_userAlreadyInGame() {
        // Given
        CreateGameDTO newGameDTO = new CreateGameDTO();
        newGameDTO.setCreator("userToken");

        User creator = new User();
        creator.setToken("userToken");
        creator.setGame(new Game());

        when(userRepository.findByToken("userToken")).thenReturn(Optional.of(creator));

        // Then
        assertThrows(ResponseStatusException.class, () -> {
            gameService.createGame(newGameDTO);
        });
    }

    @Test
    public void testGetGames() {
        // Given
        Game game1 = new Game();
        game1.setGameCode("ABC123");
        game1.setMaxPlayers(4);
        game1.setPlayerCount(1);
        game1.setGameStatus(GameStatus.LOBBY);

        Game game2 = new Game();
        game2.setGameCode("DEF456");
        game2.setMaxPlayers(4);
        game2.setPlayerCount(2);
        game2.setGameStatus(GameStatus.LOBBY);

        List<Game> games = Arrays.asList(game1, game2);
        when(gameRepository.findAll()).thenReturn(games);

        // When
        List<GameInfoDTO> gameInfoDTOs = gameService.getGames();

        // Then
        assertEquals(2, gameInfoDTOs.size(), "The number of games should match the number of games in the repository");
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void testGetGame_validGameCode() {
        // Given
        String gameCode = "ABC123";
        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(2);
        game.setGameStatus(GameStatus.LOBBY);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));

        // When
        GameInfoDTO gameInfoDTO = gameService.getGame(gameCode);

        // Then
        assertNotNull(gameInfoDTO);
        assertEquals(gameCode, gameInfoDTO.getGameCode());
        assertEquals(4, gameInfoDTO.getMaxPlayers());
        assertEquals(2, gameInfoDTO.getPlayerCount());
        assertEquals(GameStatus.LOBBY, gameInfoDTO.getGameStatus());
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