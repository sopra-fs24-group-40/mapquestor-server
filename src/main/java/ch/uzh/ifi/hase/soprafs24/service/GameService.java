package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;


    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Game gamePost) {
        Game game = new Game();
        game.setPlayerCount(1);
        game.setMaxPlayers(gamePost.getMaxPlayers());
        game.setRoundCount(gamePost.getRoundCount());
        game.setGameStatus(GameStatus.LOBBY);
        game.setGameCode(UUID.randomUUID().toString().substring(0, 5));
        return gameRepository.save(game);
    }

    public Game getGame(String gameCode) {
        return gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + gameCode));
    }


}
