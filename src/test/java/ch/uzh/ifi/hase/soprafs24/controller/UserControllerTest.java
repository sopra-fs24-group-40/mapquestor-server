package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.BDDMockito.doThrow;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private GameService gameService;


//Get
  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
      // given
      User user = new User();
      user.setUsername("firstname@lastname");
      user.setStatus(UserStatus.OFFLINE);

      List<User> allUsers = Collections.singletonList(user);

      // Convert List<User> to List<UserGetDTO> using DTOMapper
      List<UserGetDTO> allUserDTOs = allUsers.stream()
                                            .map(DTOMapper.INSTANCE::convertEntityToUserGetDTO)
                                            .collect(Collectors.toList());

      // Mock userService.getUsers() to return List<UserGetDTO>
      given(userService.getUsers()).willReturn(allUserDTOs);

      // when
      MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

      // then
      mockMvc.perform(getRequest).andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].username").value(user.getUsername()))
          .andExpect(jsonPath("$[0].status").value(user.getStatus().toString()));
  }

  @Test
  public void get_throw_error() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("firstname@lastname");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);

    given(userService.getUser(5L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
    MockHttpServletRequestBuilder getRequest = get("/users/5").contentType(MediaType.APPLICATION_JSON);
    mockMvc.perform(getRequest).andExpect(status().isNotFound());
  }

  //Post
  // Import necessary classes

  @Test
  public void createUser_validInput() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setUsername("firstname@lastname");
      user.setToken("1");
      user.setStatus(UserStatus.ONLINE);

      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setUsername("firstname@lastname");

      UserGetDTO userGetDTO = new UserGetDTO();
      userGetDTO.setId(user.getId());
      userGetDTO.setUsername(user.getUsername());
      userGetDTO.setStatus(user.getStatus());

      given(userService.createUser(Mockito.any())).willReturn(userGetDTO); // Return UserGetDTO instead of User

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.id", is(user.getId().intValue())))
              .andExpect(jsonPath("$.username", is(user.getUsername())))
              .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }


  //Trying to create a user with an invalid input, it throws HTTPS status 409- conflicted
  @Test
  public void createUser_invalidInput() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setPassword("Firstname Lastname");
      user.setUsername("firstname@lastname");
      user.setToken("1");
      user.setStatus(UserStatus.ONLINE);

      given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setPassword("Firstname Lastname");
      userPostDTO.setUsername("firstname@lastname");

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPostDTO));

      // then
      mockMvc.perform(postRequest).andExpect(status().isConflict());
  }

  @Test
  public void login_success() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setUsername("firstname@lastname");
      user.setToken("1");
      user.setStatus(UserStatus.OFFLINE);

      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setUsername("firstname@lastname");
      userPostDTO.setPassword("password");

      UserGetDTO userGetDTO = new UserGetDTO();
      userGetDTO.setId(user.getId());
      userGetDTO.setUsername(user.getUsername());
      userGetDTO.setStatus(user.getStatus());

      given(userService.login(Mockito.any())).willReturn(userGetDTO);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id", is(user.getId().intValue())))
              .andExpect(jsonPath("$.username", is(user.getUsername())))
              .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void putUser_Works() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setPassword("Firstname Lastname");
      user.setUsername("firstname@lastname");
      user.setToken("1");
      user.setStatus(UserStatus.OFFLINE);
  
      User updatedUser = new User();
      updatedUser.setId(user.getId());
      updatedUser.setUsername("testUsername1");
  
      UserPostDTO updatedUserDTO = DTOMapper.INSTANCE.convertEntityToUserPostDTO(updatedUser); // Convert User to UserPostDTO
  
      // Mock userService.createUser() to return a UserGetDTO
      UserGetDTO userGetDTO = new UserGetDTO();
      userGetDTO.setId(updatedUser.getId());
      userGetDTO.setUsername(updatedUser.getUsername());
      userGetDTO.setStatus(user.getStatus());
  
      given(userService.createUser(updatedUserDTO)).willReturn(userGetDTO); // Return UserGetDTO instead of User
  
      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder putRequest = put("/users/" + user.getId())
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedUser));
  
      // then
      mockMvc.perform(putRequest).andExpect(status().isNoContent());
  }
  

  // @Test
  // public void putUser_notWork() throws Exception {
  //     // given
  //     User user = new User(); // Create a User object
  //     user.setUsername("whatever1");
  //     user.setId(1L); // Set the id of the user object to 1
  
  //     UserPutDTO updatedUserDTO = DTOMapper.INSTANCE.convertEntityToUserPutDTO(user); // Convert User to UserPutDTO
  
  //     doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
  //             .when(userService).updateUser(4L, updatedUserDTO); // Pass UserPutDTO object to the updateUser method
  
  //     // when/then -> do the request + validate the result
  //     MockHttpServletRequestBuilder putRequest = put("/users/4")
  //             .contentType(MediaType.APPLICATION_JSON)
  //             .content(asJsonString(updatedUserDTO));
  
  //     // then
  //     mockMvc.perform(putRequest).andExpect(status().isNotFound());
  // }

  @Test
  public void putUser_Fails() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setPassword("Firstname Lastname");
      user.setUsername("firstname@lastname");
      user.setToken("1");
      user.setStatus(UserStatus.OFFLINE);

      UserPutDTO updatedUserDTO = new UserPutDTO();
      updatedUserDTO.setUsername("testUsername1"); // Updated username

      // Mock userService.updateUser() to throw an exception
      Mockito.doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            .when(userService).updateUser(Mockito.anyLong(), Mockito.any());

      // when/then -> perform the request and validate the result
      MockHttpServletRequestBuilder putRequest = put("/users/" + user.getId())
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedUserDTO));

      // then
      mockMvc.perform(putRequest)
              .andExpect(status().isInternalServerError()); // Expect HTTP status 500 - Internal Server Error
  }

  @Test
  public void putUser_UserNotFound() throws Exception {
      // given
      // Create a UserPutDTO representing the updated user information
      UserPutDTO updatedUserDTO = new UserPutDTO();
      updatedUserDTO.setUsername("testUsername1"); // Updated username

      // Mock userService.updateUser() to throw a ResponseStatusException with status NOT_FOUND
      Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(userService).updateUser(Mockito.anyLong(), Mockito.any());

      // when/then -> perform the request and validate the result
      MockHttpServletRequestBuilder putRequest = put("/users/1") // Assume user with ID 1 doesn't exist
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(updatedUserDTO));

      // then
      mockMvc.perform(putRequest)
              .andExpect(status().isNotFound()); // Expect HTTP status 404 - Not Found
  }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}