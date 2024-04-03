package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;


    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Game createGame() {
        Game game = new Game();
        game.setPlayerCount(1);
        game.setMaxPlayers(4);
        game.setGameStatus(GameStatus.LOBBY);
        return gameRepository.save(game);
    }

    public boolean addUserToGame(Long userId, Long gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (gameOptional.isPresent() && userOptional.isPresent()) {
            Game game = gameOptional.get();
            User user = userOptional.get();

            if (user.getGame() != null) {
                return false;
            }


            game.addPlayer(user);
            user.setGame(game);

            gameRepository.save(game);
            userRepository.save(user);

            return true;
        }
        return false;
    }

    public Game findById(Long gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        return gameOptional.orElseThrow(() -> new RuntimeException("Game not found!"));
    }


    public boolean existsById(Long gameId) {
        return gameRepository.existsById(gameId);
    }
}
