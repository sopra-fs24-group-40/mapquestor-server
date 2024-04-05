package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSocketService {

    private final UserRepository userRepository;

    public UserSocketService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
    }

    public void disconnectUser(User user) {
        var storedUser = userRepository.findById(user.getId()).orElse(null);

        if (storedUser != null) {
            user.setStatus(UserStatus.OFFLINE);
            userRepository.save(user);
        }

    }

    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(UserStatus.ONLINE);
    }


}
