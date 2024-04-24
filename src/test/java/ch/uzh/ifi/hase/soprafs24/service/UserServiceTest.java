package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private UserPostDTO tester; // Declaring tester as a field

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testUsername");
    tester =  DTOMapper.INSTANCE.convertEntityToUserPostDTO(testUser); // Initializing tester

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(tester);
  }

  // @Test
  // public void createUser_validInputs_success() {
  //   // when -> any object is being save in the userRepository -> return the dummy
  //   // testUser
  //   UserGetDTO createdUser = userService.createUser(tester);

  //   // then
  //   Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

  //   assertEquals(testUser.getId(), createdUser.getId());
  //   assertEquals(testUser.getUsername(), createdUser.getUsername());
  //   assertNotNull(createdUser.getToken());
  //   assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
  // }

  // @Test
  // public void createUser_duplicateName_throwsException() {
  //   // given -> a first user has already been created
  //   userService.createUser(tester);

  //   // when -> setup additional mocks for UserRepository
  //   Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

  //   // then -> attempt to create second user with same user -> check that an error
  //   // is thrown
  //   assertThrows(ResponseStatusException.class, () -> userService.createUser(tester));
  // }

  // @Test
  // public void createUser_duplicateInputs_throwsException() {
  //     // given -> a first user has already been created
  //     userService.createUser(tester);
  
  //     // when -> setup additional mocks for UserRepository
  //     User userEntity = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(tester); // Convert UserPostDTO to User entity
  //     Mockito.when(userRepository.findByUsername(Mockito.any()))
  //            .thenReturn(Optional.of(userEntity)); // Return Optional<User> with the user entity
  
  //     // then -> attempt to create second user with same user -> check that an error
  //     // is thrown
  //     assertThrows(ResponseStatusException.class, () -> userService.createUser(tester));
  // }
}
