package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;


    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Game createGame(Game gamePost) {
        Game game = new Game();
        game.setPlayerCount(1);
        game.setMaxPlayers(gamePost.getMaxPlayers());
        game.setRoundCount(gamePost.getRoundCount());
        game.setGameStatus(GameStatus.LOBBY);
        game.setCreator(gamePost.getCreator());
        game.setGameCode(UUID.randomUUID().toString().substring(0, 5));

        return gameRepository.save(game);
    }


    public Game getGame(String gameCode) {
        return gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + gameCode));
    }

    public List<User> getUsersByGameId(String gameCode) {
        Optional<Game> game = gameRepository.findByGameCode(gameCode);
        if (game.isPresent()) {
            return new ArrayList<>(game.get().getPlayers());
        }
        else {
            throw new EntityNotFoundException("Spiel mit ID " + gameCode + " nicht gefunden.");
        }
    }


    public void addUserToGame(String token, String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode).orElseThrow(() -> new RuntimeException("Spiel nicht gefunden"));
        User user = userRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        if (user.getGame() != null) {
            throw new RuntimeException("Benutzer ist bereits in einem Spiel");
        }

        if (game.getPlayers().size() < game.getMaxPlayers()) {
            game.addPlayer(user);

            gameRepository.save(game);
            userRepository.save(user);
        }
        else {
            throw new RuntimeException("Das Spiel ist bereits voll");
        }
    }


}
