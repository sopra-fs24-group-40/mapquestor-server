package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.CityRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CreateGameDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameStatusDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.PlayerInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CityDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Null;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Random;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;



    public GameService(GameRepository gameRepository, UserRepository userRepository, CityRepository cityRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    public Game createGame(CreateGameDTO newGame) {

        User creator = userRepository.findByToken(newGame.getCreator())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (creator.getGame() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in a game");
        }

        Game game = new Game();

        game.setGameCode(UUID.randomUUID().toString().substring(0, 5));
        game.setCreator(newGame.getCreator());
        game.setPlayerCount(0);
        game.setMaxPlayers(newGame.getMaxPlayers());
        game.setRoundCount(newGame.getRoundCount());
        game.setGameType(newGame.getGameType());
        game.setGameStatus(GameStatus.LOBBY);
        game.addPlayer(creator);

        return gameRepository.save(game);
    }


    public GameInfoDTO getGame(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with code: " + gameCode));

        List<PlayerInfoDTO> playerInfoDTOs = game.getPlayers().stream()
                .map(player -> {
                    PlayerInfoDTO dto = new PlayerInfoDTO();
                    dto.setUsername(player.getUsername());
                    dto.setToken(player.getToken());
                    dto.setPoints(0);
                    return dto;
                })
                .collect(Collectors.toList());

        GameInfoDTO gameDTO = new GameInfoDTO();
        gameDTO.setGameCode(game.getGameCode());
        gameDTO.setCreator(game.getCreator());
        gameDTO.setPlayerCount(game.getPlayerCount());
        gameDTO.setMaxPlayers(game.getMaxPlayers());
        gameDTO.setRoundCount(game.getRoundCount());
        gameDTO.setGameType(game.getGameType());
        gameDTO.setGameStatus(game.getGameStatus());
        gameDTO.setPlayers(playerInfoDTOs);

        return gameDTO;
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

    public Game joinGame(String gameCode, String token) {
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with code: " + gameCode));

        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with token: " + token));

        if (user.getGame() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in a game");
        }

        if (game.getPlayerCount() < game.getMaxPlayers()) {
            game.addPlayer(user);
            return gameRepository.save(game);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already full");
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

    public GameStatusDTO updateGameStatus(String gameCode, GameStatusDTO gameStatus) {
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with code: " + gameCode));

        game.setGameStatus(gameStatus.getStatus());
        gameRepository.save(game);

        return gameStatus;
    }

    public CityDTO sendRandomCityData(String gameId) {
        List<City> cities = cityRepository.findAll();

        if (cities.isEmpty()) {
            throw new IllegalStateException("No cities found in the repository");
        }

        Random random = new Random();
        City randomCity = cities.get(random.nextInt(cities.size()));

        // Convert City entity to CityDTO
        CityDTO randomCityDTO = convertToDTO(randomCity);
        
        // Assuming you want to send the random city data over WebSocket
        return randomCityDTO;
    }
    
    // Method to convert City entity to CityDTO
    private CityDTO convertToDTO(City city) {
        return new CityDTO(city.getId(), city.getName(), city.getCapital(), city.getLongitude(), city.getLatitude());
    }

    
    // public User addPointsToUser(String gameCode, String username, Integer points) {
    //     Game game = gameRepository.findByGameCode(gameCode)
    //             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with code: " + gameCode));

    //     User user = userRepository.findByUsername(username)
    //             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));

    //     if (game.getPlayers().contains(user)) {
    //         user.setPoints(user.getPoints() + points);
    //         userRepository.save(user);
    //         return user;
    //     }
    //     else {
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in the game");
    //     }        
    // }
}
