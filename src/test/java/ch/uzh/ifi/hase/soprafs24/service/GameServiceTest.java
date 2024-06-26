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
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.*;

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
    public void testGetGame_PlayerMapping() {
        // Given
        String gameCode = "ABC123";
        String userToken1 = "token1";
        String userToken2 = "token2";

        User user1 = new User();
        user1.setToken(userToken1);
        user1.setUsername("User1");

        User user2 = new User();
        user2.setToken(userToken2);
        user2.setUsername("User2");

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(2);
        game.setGameStatus(GameStatus.LOBBY);

        game.addPlayer(user1);
        game.addPlayer(user2);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
//        when(userRepository.findByToken(userToken1)).thenReturn(Optional.of(user1));
//        when(userRepository.findByToken(userToken2)).thenReturn(Optional.of(user2));

        // When
        GameInfoDTO gameInfoDTO = gameService.getGame(gameCode);

        // Then
        assertNotNull(gameInfoDTO);
        assertEquals(gameCode, gameInfoDTO.getGameCode());
        assertEquals(4, gameInfoDTO.getMaxPlayers());
        assertEquals(4, gameInfoDTO.getPlayerCount());
        assertEquals(2, gameInfoDTO.getPlayers().size());

        List<PlayerInfoDTO> playerInfoDTOs = gameInfoDTO.getPlayers();
        assertEquals(2, playerInfoDTOs.size());
        assertEquals(userToken1, playerInfoDTOs.get(0).getToken());
        assertEquals(userToken2, playerInfoDTOs.get(1).getToken());
    }

    @Test
    public void testGetUsersByGameId_GameExistsWithUsers() {
        // Given
        String gameCode = "ABC123";
        User user1 = new User();
        user1.setToken("token1");
        user1.setUsername("User1");
        User user2 = new User();
        user2.setToken("token2");
        user2.setUsername("User2");

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(2);
        game.setGameStatus(GameStatus.LOBBY);
        game.addPlayer(user1);
        game.addPlayer(user2);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));

        // When
        List<User> users = gameService.getUsersByGameId(gameCode);

        // Then
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testGetUsersByGameId_GameDoesNotExist() {
        // Given
        String gameCode = "XYZ789";

        assertThrows(EntityNotFoundException.class, () -> gameService.getUsersByGameId(gameCode));
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

    @Test
    public void testJoinGame_gameIsFull() {
        // Given
        String gameCode = "ABC123";
        String userToken = "userToken";

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(4);

        User user = new User();
        user.setToken(userToken);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
        when(userRepository.findByToken(userToken)).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> gameService.joinGame(gameCode, userToken), "A ResponseStatusException should be thrown because the game is already full");
    }

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
    void testDeleteGame_GameExists() {
        // Given
        String gameCode = "ABC123";
        String userToken = "userToken";

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(2);
        game.setGameStatus(GameStatus.LOBBY);

        User user = new User();
        user.setToken(userToken);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
        when(userRepository.findByToken(userToken)).thenReturn(Optional.of(user));

        // When
        gameService.deleteGame(userToken, gameCode);

        // Then
        // Verify that the game was deleted
        verify(gameRepository, times(1)).delete(any(Game.class)); // Use any(Game.class) instead of specific game instance
        // Additional assertions or verifications here to confirm the intended side effects of deleting the game
    }

    @Test
    public void testDeleteGame_GameDoesNotExist() {
        // Given
        String gameCode = "XYZ789";
        String userToken = "userToken";

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.deleteGame(userToken, gameCode), "A ResponseStatusException should be thrown because the game does not exist");
    }

    @Test
    public void testDumpUserAndDeleteGameIfEmpty2() {
        // Given
        String token = "testToken";
        String gameCode = "sampleGameCode";

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setPlayerCount(1);
        User user = new User();
        user.setToken(token);
        user.setStatus(UserStatus.INGAME);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        // When
        gameService.dumpUserAndDeleteGameIfEmpty2(token, gameCode);

        // Then
        verify(gameRepository).save(argThat(savedGame -> savedGame.getPlayers().isEmpty()));

        assertEquals(0, game.getPlayerCount());

        assertEquals(UserStatus.OFFLINE, user.getStatus());

        verify(gameRepository, times(1)).delete(any(Game.class));
    }

    @Test
    public void testDeleteGame2_SuccessfulDeletion() {
        // Given
        String gameCode = "ABC123";
        String userToken = "userToken";

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setMaxPlayers(4);
        game.setPlayerCount(2);
        game.setGameStatus(GameStatus.LOBBY);

        User user = new User();
        user.setToken(userToken);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));
        when(userRepository.findByToken(userToken)).thenReturn(Optional.of(user));

        // When
        gameService.deleteGame2(userToken, gameCode);

        // Then
        verify(gameRepository, times(1)).delete(any(Game.class));
    }

    @Test
    public void testDeleteGame2_GameDoesNotExist() {
        // Given
        String gameCode = "XYZ789";
        String userToken = "userToken";

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.deleteGame2(userToken, gameCode), "A ResponseStatusException should be thrown because the game does not exist");
    }

    @Test
    public void testDeleteGame2_UserNotFound() {
        // Given
        String gameCode = "ABC123";
        String userToken = "userToken";

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(new Game()));
        when(userRepository.findByToken(userToken)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.deleteGame2(userToken, gameCode), "A ResponseStatusException should be thrown because the user was not found");
    }

    @Test
    public void testReturnCities() {
        // Given
        String gameCode = "sampleGameCode";
        int roundCount = 0;
        List<City> expectedCities = new ArrayList<>(roundCount);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(new Game()));

        List<City> actualCities = gameService.returnCities(gameCode);

        assertEquals(roundCount, actualCities.size(), "The number of cities should match the round count.");

        Set<String> cityIds = new HashSet<>();
        for (City city : actualCities) {
            assertFalse(cityIds.contains(city.getId().toString()), "Each city should be unique.");
            cityIds.add(city.getId().toString());
        }
    }
    @Test
    public void testReturnPlayers() {
        // Given
        String gameCode = "sampleGameCode";
        Game game = new Game();
        game.setGameCode(gameCode);

        User player1 = new User();
        player1.setUsername("Player1");
        player1.setToken("token1");
        game.addPlayer(player1);

        User player2 = new User();
        player2.setUsername("Player2");
        player2.setToken("token2");
        game.addPlayer(player2);

        when(gameRepository.findByGameCode(gameCode)).thenReturn(Optional.of(game));

        // When
        List<PlayerInfoDTO> playerInfoDTOs = gameService.returnPlayers(gameCode);

        // Then
        assertNotNull(playerInfoDTOs);
        assertEquals(2, playerInfoDTOs.size());
        assertEquals("Player1", playerInfoDTOs.get(0).getUsername());
        assertEquals("token1", playerInfoDTOs.get(0).getToken());
        assertEquals(0, playerInfoDTOs.get(0).getPoints());
        assertEquals("Player2", playerInfoDTOs.get(1).getUsername());
        assertEquals("token2", playerInfoDTOs.get(1).getToken());
        assertEquals(0, playerInfoDTOs.get(1).getPoints());
    }

}