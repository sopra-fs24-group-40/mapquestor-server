package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

   @Test
   public void createUser_validInputs_success() {
       // given
       UserPostDTO testUserDTO = new UserPostDTO();
       testUserDTO.setUsername("testUsername");
       testUserDTO.setPassword("testPassword");

       // when
       UserGetDTO createdUser = userService.createUser(testUserDTO);

       // then
       assertNotNull(createdUser.getId());
       assertEquals(testUserDTO.getUsername(), createdUser.getUsername());
       assertNotNull(createdUser.getToken());
       assertEquals(UserStatus.ONLINE, createdUser.getStatus());
   }

   @Test
   public void createUser_duplicateUsername_throwsException() {
       // given
       UserPostDTO testUserDTO1 = new UserPostDTO();
       testUserDTO1.setUsername("testUsername");
       testUserDTO1.setPassword("testPassword");
       userService.createUser(testUserDTO1);

       // when, then
       UserPostDTO testUserDTO2 = new UserPostDTO();
       testUserDTO2.setUsername("testUsername"); // Duplicate username
       testUserDTO2.setPassword("anotherPassword");
       assertThrows(ResponseStatusException.class, () -> userService.createUser(testUserDTO2));
   }

//    @Test
//    public void findUserByToken_existingUser_returnsUser() {
//        // given
//        User testUser = new User();
//        User testUser1 = new User();
//        testUser.setUsername("testUsername");
//        testUser.setPassword("testPassword");
//        testUser.setToken("testToken");
//        testUser1 = userRepository.saveAndFlush(testUser);
//
//        // when
//        UserGetDTO foundUser = userService.getUserByToken("testToken");
//
//        // then
//        assertNotNull(foundUser);
//        assertEquals(testUser.getToken(), foundUser.getToken());
//    }

}
