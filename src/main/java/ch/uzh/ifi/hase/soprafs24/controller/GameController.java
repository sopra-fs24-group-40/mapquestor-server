package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CreateGameDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserGameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameDTO game) {
        Game newGame = gameService.createGame(game);
        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }

    @GetMapping("/games")
    public ResponseEntity<List<GameInfoDTO>> getGames() {
        List<GameInfoDTO> games = gameService.getGames();
        return ResponseEntity.ok(games);
    }

    @PostMapping("/games/{gameCode}/join")
    public ResponseEntity<Game> joinGame(@PathVariable("gameCode") String gameCode, @RequestBody UserGameInfoDTO user) {
        System.out.println("Received token: " + user.getToken());  // Debugging line
        Game game = gameService.joinGame(gameCode, user.getToken());
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping("/games/{gameCode}")
    public GameInfoDTO getGame(@PathVariable("gameCode") String gameCode) {
        return gameService.getGame(gameCode);
    }

    @GetMapping("/games/{gameCode}/users")
    public ResponseEntity<List<String>> getGameUsernames(@PathVariable String gameCode) {
        List<User> users = gameService.getUsersByGameId(gameCode);
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usernames);
    }

    @GetMapping("/city")
    public ResponseEntity<City> getCity() {
        return ResponseEntity.ok(gameService.getRandomCity());
    }

}
