package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserTokenDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

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

//    @Test
//    public void createGame_validateCreator() {
//        // Create user
//        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setUsername("testUser");
//        userPostDTO.setPassword("testPassword");
//
//        UserGetDTO createdUserDTO = userService.createUser(userPostDTO);
//        assertNotNull(createdUserDTO.getId());
//
//        // Retrieve the created user entity from the database
//        User createdUser = userRepository.findById(createdUserDTO.getId()).orElseThrow();
//
//        // Create game
//        Game newGame = new Game();
//        newGame.setMaxPlayers(2); // Ensure maxPlayers is at least 2
//        newGame.setRoundCount(1); // Ensure roundCount is at least 1
//        newGame.setCreator(createdUser.getUsername());
//        newGame.addPlayer(createdUser);
//        Game savedGame = gameRepository.save(newGame);
//
//        // Validate creator
//        assertEquals(createdUser.getUsername(), savedGame.getCreator());
//    }
//
//    @Test
//    public void createGame_twoUsers_joinGame_validatePlayers() {
//        // Create first user
//        UserPostDTO userPostDTO1 = new UserPostDTO();
//        userPostDTO1.setUsername("user1");
//        userPostDTO1.setPassword("password1");
//        UserGetDTO user1DTO = userService.createUser(userPostDTO1);
//
//        // Retrieve the first created user entity from the database
//        User user1 = userRepository.findById(user1DTO.getId()).orElseThrow();
//
//        // Create second user
//        UserPostDTO userPostDTO2 = new UserPostDTO();
//        userPostDTO2.setUsername("user2");
//        userPostDTO2.setPassword("password2");
//        UserGetDTO user2DTO = userService.createUser(userPostDTO2);
//
//        // Retrieve the second created user entity from the database
//        User user2 = userRepository.findById(user2DTO.getId()).orElseThrow();
//
//        // Create game with first user
//        Game game = new Game();
//        game.setCreator(user1.getUsername());
//        game.addPlayer(user1);
//        Game createdGame = gameRepository.save(game);
//
//        // Join game with second user
//        createdGame.addPlayer(user2);
//        Game updatedGame = gameRepository.save(createdGame);
//
//        // Validate number of players
//        assertEquals(2, updatedGame.getPlayers().size());
//        assertTrue(updatedGame.getPlayers().contains(user1));
//        assertTrue(updatedGame.getPlayers().contains(user2));
//    }
//
//    @Test
//    public void createGame_deleteUser_validateGameDeletion() {
//        // Create user
//        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setUsername("testUser");
//        userPostDTO.setPassword("testPassword");
//
//        UserGetDTO createdUserDTO = userService.createUser(userPostDTO);
//        assertNotNull(createdUserDTO.getId());
//
//        // Retrieve the created user entity from the database
//        User createdUser = userRepository.findById(createdUserDTO.getId()).orElseThrow();
//
//        // Create game
//        Game game = new Game();
//        game.setMaxPlayers(2); // Ensure maxPlayers is at least 2
//        game.setRoundCount(1); // Ensure roundCount is at least 1
//        game.setCreator(createdUser.getUsername());
//        game.addPlayer(createdUser);
//        Game savedGame = gameRepository.save(game);
//
//        // Delete user
//        userService.deleteTheUser(createdUser.getId());
//
//        // Validate game deletion
//        Optional<Game> deletedGame = gameRepository.findById(savedGame.getGameId());
//        assertFalse(deletedGame.isPresent());
//    }
}
