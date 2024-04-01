package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @PostMapping("/users/login") //used for logging in the user
  @ResponseStatus(HttpStatus.OK) //used post, there is no strict rule what to use put/post
  @ResponseBody
  public UserGetDTO loginTheUser(@RequestBody UserPostDTO userPostDTO){
      // convert API user to internal representation
      User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

      // check credentials
      User EntityUser = userService.checkUser(userInput);

      //convert mappedUser back to API and return
      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(EntityUser);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> logOut(@RequestBody UserPostDTO userPostDTO) {
      boolean loggedOut = userService.logout(userPostDTO);
      if (loggedOut) {
          return ResponseEntity.ok("Logged out!");
      }
      else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }
  }


  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @GetMapping("/users/{userID}")
  @ResponseStatus(HttpStatus.OK) //if it was found the OK https
  @ResponseBody
  public UserGetDTO getOneUser(@PathVariable(value="userID") Long userID){
      User fetched = userService.getOneUser(userID); 
      fetched.setToken(null); 
      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(fetched);
  }

  @PutMapping("/users/{userID}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateTheUser(@RequestBody UserPostDTO userPostDTO, @PathVariable(value="userID") Long userID){
      User editUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

      String newUsername = editUser.getUsername();
      String newPassword = editUser.getPassword();

      userService.updateUser(userID, newUsername, newPassword);
  }
}
