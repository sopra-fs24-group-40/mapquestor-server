package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CreateGameDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserTokenDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@WebAppConfiguration
@SpringBootTest
public class UserServiceGameServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void createUser_updateUser_validate() {
        // Create user
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");
        userPostDTO.setPassword("testPassword");

        UserGetDTO createdUser = userService.createUser(userPostDTO);
        assertNotNull(createdUser.getId());

        // Update user
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("updatedUser");
        userService.updateUser(createdUser.getId(), userPutDTO);

        // Validate update
        UserGetDTO updatedUser = userService.getUser(createdUser.getId());
        assertEquals("updatedUser", updatedUser.getUsername());
    }

    @Test
    public void createUser_logout_login_validate() {
        // Create user
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");
        userPostDTO.setPassword("testPassword");

        UserGetDTO createdUser = userService.createUser(userPostDTO);
        assertNotNull(createdUser.getId());

        // Logout user
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken(createdUser.getToken());
        boolean isLoggedOut = userService.logout(userTokenDTO);
        assertTrue(isLoggedOut);

        // Login user
        UserGetDTO loggedInUser = userService.login(userPostDTO);
        assertEquals(createdUser.getId(), loggedInUser.getId());
        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
    }

    @Test
    public void createGame_validateCreator() {
        // Create user
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");
        userPostDTO.setPassword("testPassword");

        UserGetDTO createdUserDTO = userService.createUser(userPostDTO);
        assertNotNull(createdUserDTO.getId());

        // Retrieve the created user entity from the database
        User createdUser = userRepository.findById(createdUserDTO.getId()).orElseThrow();

        // Create game DTO
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setMaxPlayers(2); // Ensure maxPlayers is at least 2
        createGameDTO.setRoundCount(1); // Ensure roundCount is at least 1
        createGameDTO.setCreator(createdUser.getToken());

        // Call the GameService to create the game
        Game savedGame = gameService.createGame(createGameDTO);

        // Reload the created user to ensure it's managed by the current session
        createdUser = userRepository.findById(createdUserDTO.getId()).orElseThrow();

        // Validate creator
        assertEquals(createdUser.getToken(), savedGame.getCreator());
    }

    @Transactional
    @Test
    public void createGame_twoUsers_joinGame_validatePlayers() {
        // Create first user
        UserPostDTO userPostDTO1 = new UserPostDTO();
        userPostDTO1.setUsername("user1");
        userPostDTO1.setPassword("password1");
        UserGetDTO user1DTO = userService.createUser(userPostDTO1);

        // Retrieve the first created user entity from the database
        User user1 = userRepository.findById(user1DTO.getId()).orElseThrow();

        // Create second user
        UserPostDTO userPostDTO2 = new UserPostDTO();
        userPostDTO2.setUsername("user2");
        userPostDTO2.setPassword("password2");
        UserGetDTO user2DTO = userService.createUser(userPostDTO2);

        // Retrieve the second created user entity from the database
        User user2 = userRepository.findById(user2DTO.getId()).orElseThrow();

        // Create game DTO with first user as the creator
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setMaxPlayers(2); // Ensure maxPlayers is at least 2
        createGameDTO.setRoundCount(1); // Ensure roundCount is at least 1
        createGameDTO.setCreator(user1.getToken());

        // Call the GameService to create the game
        Game createdGame = gameService.createGame(createGameDTO);

        // Reload the first created user to ensure it's managed by the current session
        user1 = userRepository.findById(user1DTO.getId()).orElseThrow();

        // Join game with the second user
        Game updatedGame = gameService.joinGame(createdGame.getGameCode(), user2.getToken());

        // Validate number of players
        assertEquals(2, updatedGame.getPlayers().size());

        // Retrieve players again to ensure they are managed by the current session
        user1 = userRepository.findById(user1DTO.getId()).orElseThrow();
        user2 = userRepository.findById(user2DTO.getId()).orElseThrow();

        // Validate that both users are in the game
        assertTrue(updatedGame.getPlayers().contains(user1));
        assertTrue(updatedGame.getPlayers().contains(user2));
    }

    @Transactional
    @Test
    public void createGame_deleteUser_validateGameDeletion() {
        // Create first user
        UserPostDTO userPostDTO1 = new UserPostDTO();
        userPostDTO1.setUsername("testUser1");
        userPostDTO1.setPassword("testPassword1");

        UserGetDTO createdUserDTO1 = userService.createUser(userPostDTO1);
        assertNotNull(createdUserDTO1.getId());

        // Retrieve the created first user entity from the database
        User createdUser1 = userRepository.findById(createdUserDTO1.getId()).orElseThrow();

        // Create second user
        UserPostDTO userPostDTO2 = new UserPostDTO();
        userPostDTO2.setUsername("testUser2");
        userPostDTO2.setPassword("testPassword2");

        UserGetDTO createdUserDTO2 = userService.createUser(userPostDTO2);
        assertNotNull(createdUserDTO2.getId());

        // Retrieve the created second user entity from the database
        User createdUser2 = userRepository.findById(createdUserDTO2.getId()).orElseThrow();

        // Create game with both users
        Game game = new Game();
        game.setMaxPlayers(2); // Ensure maxPlayers is at least 2
        game.setRoundCount(1); // Ensure roundCount is at least 1
        game.setCreator(createdUser1.getUsername());
        game.addPlayer(createdUser1);
        game.addPlayer(createdUser2); // Add the second user to the game
        Game savedGame = gameRepository.save(game);

        // Refresh the game to ensure it is correctly saved with its players
        savedGame = gameRepository.findById(savedGame.getGameId()).orElseThrow();
        assertTrue(savedGame.getPlayers().contains(createdUser1));
        assertTrue(savedGame.getPlayers().contains(createdUser2));

        // Delete the creator user using GameService's deleteGame method
        gameService.deleteGame(createdUser1.getToken(), savedGame.getGameCode());

        // Validate game deletion
        Optional<Game> deletedGame = gameRepository.findById(savedGame.getGameId());
        assertFalse(deletedGame.isPresent());
    } 
}
 