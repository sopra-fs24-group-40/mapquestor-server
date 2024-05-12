package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserSocketServiceTest {

    @Test
    public void testSaveUser() {
        // Given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UserSocketService userSocketService = new UserSocketService(userRepositoryMock);

        User user = createUser("testUser");

        // When
        userSocketService.saveUser(user);

        // Then
        assertEquals(UserStatus.ONLINE, user.getStatus());
        verify(userRepositoryMock).save(user);
    }

    @Test
    public void testDisconnectUser_UserExists() {
        // Given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UserSocketService userSocketService = new UserSocketService(userRepositoryMock);

        User user = createUser("testUser");
        when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        userSocketService.disconnectUser(user);

        // Then
        assertEquals(UserStatus.OFFLINE, user.getStatus());
        verify(userRepositoryMock).save(user);
    }

    @Test
    public void testDisconnectUser_UserNotExists() {
        // Given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UserSocketService userSocketService = new UserSocketService(userRepositoryMock);

        User user = createUser("testUser");
        when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.empty());

        // When
        userSocketService.disconnectUser(user);

        // Then
        // No changes should be made if the user does not exist
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    public void testFindConnectedUsers() {
        // Given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UserSocketService userSocketService = new UserSocketService(userRepositoryMock);

        List<User> connectedUsers = new ArrayList<>();
        connectedUsers.add(createUser("user1"));
        connectedUsers.add(createUser("user2"));

        when(userRepositoryMock.findAllByStatus(UserStatus.ONLINE)).thenReturn(connectedUsers);

        // When
        List<User> foundUsers = userSocketService.findConnectedUsers();

        // Then
        assertEquals(connectedUsers.size(), foundUsers.size());
        for (User user : connectedUsers) {
            assertTrue(foundUsers.contains(user));
        }
    }

    // Helper method to create a User object
    private User createUser(String username) {
        User user = new User();
        user.setId(1L); // Set an arbitrary ID for testing
        user.setUsername(username);
        user.setStatus(UserStatus.OFFLINE); // Initially set to OFFLINE
        return user;
    }
}
