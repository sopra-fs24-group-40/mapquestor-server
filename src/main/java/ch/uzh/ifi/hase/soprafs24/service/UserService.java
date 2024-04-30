package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserGetDTO> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        users.forEach(user -> userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user)));

        return userGetDTOs;
    }

    public UserGetDTO getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    public UserGetDTO getUserByToken(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + token));
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    }

    public String getUsernameByToken(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + token));
        return user.getUsername();
    }


    public UserGetDTO createUser(UserPostDTO userPostDTO) {

        if (userRepository.existsByUsername(userPostDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");
        }

        User newUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreation_date(LocalDateTime.now());
        newUser.setPlayedGames(0);
        newUser.setWonGames(0);
        newUser.setAvatar("AVATAR");


        newUser = userRepository.save(newUser);
        System.out.println("-------------------------"+newUser.getAvatar());

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(newUser);
    }

    public void updateUser(long id, UserPutDTO userPutDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        
        System.out.println(userPutDTO.getAvatar());
        if (!existingUser.getUsername().equals(userPutDTO.getUsername())) {
            if (userRepository.existsByUsername(userPutDTO.getUsername())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");
            }
            existingUser.setUsername(userPutDTO.getUsername());
        }

        if (!existingUser.getUsername().equals(userPutDTO.getUsername())) {
            existingUser.setAvatar(userPutDTO.getAvatar());
        }
        // if (userPutDTO.getPassword() != null && !userPutDTO.getPassword().isEmpty()) {
        //     existingUser.setPassword(userPutDTO.getPassword());
        // }


        userRepository.save(existingUser);
    }


    public UserGetDTO login(UserPostDTO userPostDTO) {
        User user = userRepository.findByUsername(userPostDTO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password!"));

        if (user != null && checkPassword(userPostDTO.getPassword(), user.getPassword())) {
            user.setStatus(UserStatus.ONLINE);
            user.setToken(UUID.randomUUID().toString());
            userRepository.save(user);
            return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password!");
        }
    }

    public boolean logout(UserPostDTO userPostDTO) {
        Optional<User> userOptional = userRepository.findByUsername(userPostDTO.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(UserStatus.OFFLINE);
            user.setToken("");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean verifyToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        Optional<User> user = userRepository.findByToken(token.trim());
        return user.isPresent();
    }

    private boolean checkPassword(String old_password, String new_password) {
        return old_password.equals(new_password);
    }

    public void deleteTheUser(Long userID) {

        if (userRepository.existsById(userID)) {
            userRepository.deleteById(userID);
        }
        else {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "User not found for deletion!");
        }
    }


}


