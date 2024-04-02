package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {

    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    newUser.setCreation_date(LocalDateTime.now());

    checkIfUserExists(newUser);
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          String.format(baseErrorMessage, "username and the name", "are"));
    }
  }

  public User checkUser(User LoginUser) {
    User userByToken = null;
    User us = userRepository.findByUsername(LoginUser.getUsername());

    if (us.getUsername().equals(LoginUser.getUsername())) {
            userByToken = us;
    }
    String password = LoginUser.getPassword();
    boolean valid = userByToken != null && userByToken.getPassword().equals(password); //equal password check
    if (!valid) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "False Username or password");
    } 
    userByToken.setStatus(UserStatus.ONLINE); //Status => Online
    userByToken.setToken(UUID.randomUUID().toString());
    User EntityUser = userRepository.save(userByToken);
    userRepository.flush(); //save all changes done
    return EntityUser;
}

public boolean logout(UserPostDTO userPostDTO) {
    User user = userRepository.findByUsername(userPostDTO.getUsername());
    if (user != null) {
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("");
        userRepository.save(user);
        return true;
    }
    return false;
}
  public User getOneUser(Long userID) {
    User userByID = null;
    List<User> usersByUsername = userRepository.findAll();
    for (User user : usersByUsername) {
        if (user.getId().equals(userID)) {
            userByID = user;
        }
    }
    if (userByID == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such User found!");
    }
    return userByID;
}

    public void updateUser(Long userID, String uname, String password) {
      User fetchedUser = getOneUser(userID);
      User user = userRepository.findByUsername(uname);
    if (user != null && !user.getId().equals(userID)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username taken!");
    }
      if (uname != null) {
          fetchedUser.setUsername(uname);
      }
    
      if (password != null) {
          fetchedUser.setPassword(password);
      }
    
      userRepository.save(fetchedUser);
      userRepository.flush();
    }

    public void deleteTheUser(Long userID) {
      // Check if the user exists
      if (userRepository.existsById(userID)) {
          // If user exists, delete it
          userRepository.deleteById(userID);
      } else {
          // Handle the case where the user does not exist
          throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "User not found for deletion!");
      }
  }}

