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
     public void testCreateUser_WhenUsernameDoesNotExist() {
         // Given
         UserPostDTO userPostDTO = new UserPostDTO();
         userPostDTO.setUsername("newUser");
         userPostDTO.setPassword("password");

         // When createUser is called
         UserGetDTO createdUser = userService.createUser(userPostDTO);

         // Debug log
         System.out.println("Created user: " + createdUser);

         // Then verify repository interaction
         verify(userRepository, times(1)).existsByUsername(userPostDTO.getUsername());
         verify(userRepository, times(1)).save(any(User.class));

         // Then assert created user details
         assertNotNull(createdUser, "Created user should not be null");
         assertEquals(userPostDTO.getUsername(), createdUser.getUsername());
         assertEquals(0, createdUser.getPlayedGames());
         assertEquals(0, createdUser.getWonGames());
         assertEquals(UserStatus.ONLINE, createdUser.getStatus());
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
    public void testLogout() {
        // Given
        String username = "testUser";
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(username);
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken(UUID.randomUUID().toString());

        User newUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        newUser.setToken(userTokenDTO.getToken());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreation_date(LocalDateTime.now());
        newUser.setPlayedGames(0);
        newUser.setWonGames(0);
        newUser.setAvatar("AVATAR");


        // Mock UserRepository and its behavior
        UserRepository userRepositoryMock = mock(UserRepository.class);
        when(userRepositoryMock.findByUsername(username)).thenReturn(Optional.of(newUser));

        // Create an instance of your UserService, injecting the mocked UserRepository
        UserService userService = new UserService(userRepositoryMock);

        // When
        boolean loggedOut = userService.logout(userTokenDTO);

        // Then
        assertTrue(loggedOut);
        assertEquals(UserStatus.OFFLINE, newUser.getStatus());
        assertEquals("", newUser.getToken());
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
