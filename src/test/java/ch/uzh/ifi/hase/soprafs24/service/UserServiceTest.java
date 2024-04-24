package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
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

    // @Test
    // public void testCreateUser_WhenUsernameDoesNotExist() {
    //     // Given
    //     UserPostDTO userPostDTO = new UserPostDTO();
    //     userPostDTO.setUsername("newUser");
    //     userPostDTO.setPassword("password");

    //     // When createUser is called
    //     UserGetDTO createdUser = userService.createUser(userPostDTO);

    //     // Debug log
    //     System.out.println("Created user: " + createdUser);

    //     // Then verify repository interaction
    //     verify(userRepository, times(1)).existsByUsername(userPostDTO.getUsername());
    //     verify(userRepository, times(1)).save(any(User.class));

    //     // Then assert created user details
    //     assertNotNull(createdUser, "Created user should not be null");
    //     assertEquals(userPostDTO.getUsername(), createdUser.getUsername());
    //     assertEquals(0, createdUser.getPlayedGames());
    //     assertEquals(0, createdUser.getWonGames());
    //     assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    // }

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
        // Setup
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setStatus(UserStatus.ONLINE);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(username);

        // Execute
        boolean loggedOut = userService.logout(userPostDTO);

        // Verify
        assertTrue(loggedOut);
        assertEquals(UserStatus.OFFLINE, user.getStatus());
        assertEquals("", user.getToken());
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
}
