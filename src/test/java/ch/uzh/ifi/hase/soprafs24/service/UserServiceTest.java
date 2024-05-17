package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById() {
        // Setup
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");
        user.setPlayedGames(5);
        user.setWonGames(3);
        user.setStatus(UserStatus.ONLINE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Execute
        UserGetDTO userDTO = userService.getUser(userId);

        // Verify
        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getPlayedGames(), userDTO.getPlayedGames());
        assertEquals(user.getWonGames(), userDTO.getWonGames());
        assertEquals(user.getStatus(), userDTO.getStatus());
    }

    @Test
    public void testCreateUser_WhenUsernameExists() {
        // Given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("existingUser");
        userPostDTO.setPassword("password");

        // Mock behavior for userRepository.existsByUsername
        when(userRepository.existsByUsername(userPostDTO.getUsername())).thenReturn(true);

        // When createUser is called with existing username
        // Then it should throw ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {
            userService.createUser(userPostDTO);
        });

        // Verify that save method is not called
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        // Setup
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(userPutDTO.getUsername())).thenReturn(false);

        // Execute
        userService.updateUser(userId, userPutDTO);

        // Verify
        assertEquals(userPutDTO.getUsername(), existingUser.getUsername());
        assertEquals("oldPassword", existingUser.getPassword()); // Password remains unchanged
    }

    @Test
    public void testLogin() {
        // Setup
        String username = "testUser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setStatus(UserStatus.OFFLINE);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(username);
        userPostDTO.setPassword(password);

        // Execute & Verify
        UserGetDTO loggedInUser = userService.login(userPostDTO);
        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        // Setup
        String username = "testUser";
        String correctPassword = "correctPassword";
        String incorrectPassword = "incorrectPassword";
        
        // Create a user with the correct password
        User user = new User();
        user.setUsername(username);
        user.setPassword(correctPassword);
        user.setStatus(UserStatus.OFFLINE);
        
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        
        // Create a userPostDTO with correct username and incorrect password
        UserPostDTO userPostDTOIncorrect = new UserPostDTO();
        userPostDTOIncorrect.setUsername(username);
        userPostDTOIncorrect.setPassword(incorrectPassword);
        
        // Execute & Verify
        assertThrows(ResponseStatusException.class, () -> userService.login(userPostDTOIncorrect));
        
        // Verify that status remains offline
        assertEquals(UserStatus.OFFLINE, user.getStatus());
        
        // Create a userPostDTO with incorrect username
        UserPostDTO userPostDTOIncorrectUsername = new UserPostDTO();
        userPostDTOIncorrectUsername.setUsername("incorrectUsername");
        userPostDTOIncorrectUsername.setPassword(correctPassword);
        
        // Execute & Verify
        assertThrows(ResponseStatusException.class, () -> userService.login(userPostDTOIncorrectUsername));
        
        // Verify that status remains offline
        assertEquals(UserStatus.OFFLINE, user.getStatus());
    }

    @Test
    public void testProcessGameData_Won() {
        // Setup
        String token = "testToken";
        User user = new User();
        user.setToken(token);
        user.setPlayedGames(5);
        user.setWonGames(3);
        
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));
        
        // Execute
        userService.processGameData("WON", token);
        
        // Verify
        assertEquals(6, user.getPlayedGames()); // Verify played games incremented by 1
        assertEquals(4, user.getWonGames()); // Verify won games incremented by 1
        verify(userRepository, times(1)).save(user); // Verify userRepository.save called once
    }

    @Test
    public void testProcessGameData_NotWon() {
        // Setup
        String token = "testToken";
        User user = new User();
        user.setToken(token);
        user.setPlayedGames(5);
        user.setWonGames(3);
        
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));
        
        // Execute
        userService.processGameData("NOT_WON", token);
        
        // Verify
        assertEquals(6, user.getPlayedGames()); // Verify played games incremented by 1
        assertEquals(3, user.getWonGames()); // Verify won games remain unchanged
        verify(userRepository, times(1)).save(user); // Verify userRepository.save called once
    }

    @Test
    public void testProcessGameData_UserNotFound() {
        // Setup
        String token = "nonExistingToken";
        when(userRepository.findByToken(token)).thenReturn(Optional.empty());
        
        // Execute & Verify
        assertThrows(RuntimeException.class, () -> userService.processGameData("WON", token));
    }

    @Test
    public void testLogout_UserExists() {
        // Setup
        String token = "testToken";
        User user = new User();
        user.setToken(token);
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");
        userPostDTO.setPassword("password");
        userService.createUser(userPostDTO); // Create a user to set the token (login)
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken(token);

        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        // Execute
        boolean loggedOut = userService.logout(userTokenDTO);

        // Verify
        assertTrue(loggedOut); // Verify that logout was successful
        assertEquals(UserStatus.OFFLINE, user.getStatus()); // Verify user status updated to offline
        assertEquals("0", user.getToken()); // Verify user token is cleared
        verify(userRepository, times(1)).save(user); // Verify userRepository.save called once
    }

    @Test
    public void testLogout_UserDoesNotExist() {
        // Setup
        String token = "nonExistingToken";
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken(token);

        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        // Execute
        boolean loggedOut = userService.logout(userTokenDTO);

        // Verify
        assertFalse(loggedOut); // Verify that logout was unsuccessful
        // Verify that userRepository.save is not called as the user does not exist
        verify(userRepository, never()).save(any(User.class));
    }
    

    @Test
    public void testDeleteUser() {
        // Setup
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.existsById(userId)).thenReturn(true);

        // Execute
        userService.deleteTheUser(userId);

        // Verify
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testGetUserByToken_UserExists() {
        // Given
        String token = "testToken";
        User user = createUser("testUser", token);
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        // When
        UserGetDTO userDTO = userService.getUserByToken(token);

        // Then
        assertNotNull(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    public void testGetUserByToken_UserNotExists() {
        // Given
        String token = "nonExistingToken";
        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> userService.getUserByToken(token));
    }

    @Test
    public void testGetUsernameByToken_UserExists() {
        // Given
        String token = "testToken";
        User user = createUser("testUser", token);
        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        // When
        String username = userService.getUsernameByToken(token);

        // Then
        assertEquals(user.getUsername(), username);
    }

    @Test
    public void testGetUsernameByToken_UserNotExists() {
        // Given
        String token = "nonExistingToken";
        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> userService.getUsernameByToken(token));
    }

    @Test
    public void testGetUsers() {
        // Setup
        User user1 = createUser("user1", "token1");
        User user2 = createUser("user2", "token2");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Execute
        List<UserGetDTO> userDTOs = userService.getUsers();

        // Verify
        assertEquals(2, userDTOs.size()); // Verify correct number of users returned
        assertEquals(user1.getUsername(), userDTOs.get(0).getUsername()); // Verify first user details
        assertEquals(user2.getUsername(), userDTOs.get(1).getUsername()); // Verify second user details
    }

    @Test
    public void testUpdateUser_UsernameAlreadyExists() {
        // Setup
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("existingUser");
        existingUser.setPassword("oldPassword");

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(userPutDTO.getUsername())).thenReturn(true);

        // When updateUser is called with username that already exists
        // Then it should throw ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(userId, userPutDTO));

        // Verify that save method is not called
        verify(userRepository, never()).save(any(User.class));
    }

//    @Test
//    public void testUpdateUser_UpdateAvatarOnUsernameChange() {
//        // Setup
//        Long userId = 1L;
//        User existingUser = new User();
//        existingUser.setId(userId);
//        existingUser.setUsername("oldUsername");
//        existingUser.setAvatar("oldAvatar");
//
//        UserPutDTO userPutDTO = new UserPutDTO();
//        userPutDTO.setUsername("newUsername");
//        userPutDTO.setAvatar("newAvatar");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.existsByUsername(userPutDTO.getUsername())).thenReturn(false);
//
//        // Execute
//        userService.updateUser(userId, userPutDTO);
//
//        // Verify
//        User updatedUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found after update"));
//        assertEquals("newUsername", updatedUser.getUsername());
//        assertEquals("oldAvatar", updatedUser.getAvatar());
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).save(updatedUser);
//    }

    @Test
    public void testVerifyToken_ValidToken() {
        // Setup
        String token = "validToken";
        when(userRepository.findByToken(token.trim())).thenReturn(Optional.of(new User()));

        // Execute
        boolean tokenValid = userService.verifyToken(token);

        // Verify
        assertTrue(tokenValid); // Verify that token is considered valid
        verify(userRepository, times(1)).findByToken(token.trim()); // Verify userRepository.findByToken called once with trimmed token
    }

    @Test
    public void testVerifyToken_NullToken() {
        // Execute
        boolean tokenValid = userService.verifyToken(null);

        // Verify
        assertFalse(tokenValid); // Verify that null token is considered invalid
        verify(userRepository, never()).findByToken(any()); // Verify userRepository.findByToken never called
    }

    @Test
    public void testVerifyToken_EmptyToken() {
        // Execute
        boolean tokenValid = userService.verifyToken("");

        // Verify
        assertFalse(tokenValid); // Verify that empty token is considered invalid
        verify(userRepository, never()).findByToken(any()); // Verify userRepository.findByToken never called
    }

    @Test
    public void testVerifyToken_TokenNotFound() {
        // Setup
        String token = "nonExistingToken";
        when(userRepository.findByToken(token.trim())).thenReturn(Optional.empty());

        // Execute
        boolean tokenValid = userService.verifyToken(token);

        // Verify
        assertFalse(tokenValid); // Verify that token is considered invalid
        verify(userRepository, times(1)).findByToken(token.trim()); // Verify userRepository.findByToken called once with trimmed token
    }

    // Helper method to create a User object
    private User createUser(String username, String token) {
        User user = new User();
        user.setId(1L); // Set an arbitrary ID for testing
        user.setUsername(username);
        user.setStatus(UserStatus.OFFLINE); // Initially set to OFFLINE
        user.setToken(token);
        return user;
    }
}

