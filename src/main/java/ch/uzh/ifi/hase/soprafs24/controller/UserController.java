package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final GameService gameService;

    UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        List<UserGetDTO> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserGetDTO> getUser(@PathVariable("id") long id) {
        UserGetDTO user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<UserGetDTO> createUser(@RequestBody UserPostDTO userPostDTO) {
        UserGetDTO newUser = userService.createUser(userPostDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserPutDTO userDetails) {
        userService.updateUser(id, userDetails);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/login")
    public ResponseEntity<UserGetDTO> login(@RequestBody UserPostDTO userPostDTO) {
        UserGetDTO loggedInUser = userService.login(userPostDTO);
        return ResponseEntity.ok(loggedInUser);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody UserTokenDTO userTokenDTO) {
        boolean loggedOut = userService.logout(userTokenDTO);


        if (loggedOut) {
            return ResponseEntity.ok("Logged out successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or already logged out");
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestBody TokenDTO tokenDTO) {
        boolean isValid = userService.verifyToken(tokenDTO.getToken());
        if (isValid) {
            return ResponseEntity.ok().body("Token is valid.");
        }
        else {
            return ResponseEntity.badRequest().body("Token is invalid.");
        }
    }

    @DeleteMapping("/users/{userID}")
    @ResponseStatus(HttpStatus.OK) //if it was found the OK https
    @ResponseBody
    public void deleteUser(@PathVariable(value = "userID") Long userID) {
        userService.deleteTheUser(userID);
    }
}
