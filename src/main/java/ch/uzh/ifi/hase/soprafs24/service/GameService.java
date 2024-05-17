package ch.uzh.ifi.hase.soprafs24.service;
 
 
import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CityRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
 
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
 
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
 
 
    public City getRandomCity() {
        List<City> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            throw new IllegalStateException("No cities found in the repository");
        }
 
        Random random = new Random();
        City randomCity = cities.get(random.nextInt(cities.size()));
        return randomCity;
    }
 
 
    public Game createGame(CreateGameDTO newGame) {
        // Find the creator user
        User creator = userRepository.findByToken(newGame.getCreator())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
 
        creator.setStatus(UserStatus.INGAME);
        // Check if the creator is already in a game
        if (creator.getGame() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in a game");
        }
 
        Game game = new Game();
        game.setGameCode(UUID.randomUUID().toString().substring(0, 5));
        game.setCreator(newGame.getCreator());
        game.setMaxPlayers(newGame.getMaxPlayers());
        game.setRoundCount(newGame.getRoundCount());
        game.setGameType(newGame.getGameType());
        game.setGameStatus(GameStatus.LOBBY);
 
        List<City> selectedCities = new ArrayList<>();
        for (int i = 0; i < newGame.getRoundCount(); i++) {
            selectedCities.add(getRandomCity());
        }
        game.setCities(selectedCities);
 
        game = gameRepository.save(game);
 
        // Add the creator user to the game
        game.addPlayer(creator);
 
        // Update the user's game reference
        creator.setGame(game);
        userRepository.save(creator);
 
        return game;
    }
 
 
    public List<GameInfoDTO> getGames() {
        List<Game> games = gameRepository.findAll();
        List<GameInfoDTO> gameInfoDTOS = new ArrayList<>();
 
        games.forEach(game -> gameInfoDTOS.add(DTOMapper.INSTANCE.convertEntityToGameInfoDTO(game)));
 
        return gameInfoDTOS;
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
        gameDTO.setCities(game.getCities());
 
 
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
            user.setStatus(UserStatus.INGAME);
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
 
    public void dumpUserAndDeleteGameIfEmpty(String token, String gameCode) {
        System.out.println("User " + token + " left game " + gameCode);
        Game game = gameRepository.findByGameCode(gameCode).orElseThrow(() -> new RuntimeException("Game not found"));
        User user = userRepository.findByToken(token).orElseThrow(() -> new RuntimeException("User not found"));
 
        // Remove the user from the game
        game.removePlayer(user);
        game.setPlayerCount(game.getPlayerCount() - 1);
        user.setStatus(UserStatus.OFFLINE);
        user.setGame(null);
        // Save the user's status and the updated game
        gameRepository.save(game);
        userRepository.save(user);
 
        if (game.getPlayerCount() == 0) {
            gameRepository.delete(game);
        }
    }
 
    public void deleteGame(String token, String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode).orElseThrow(() -> new RuntimeException("Game not found"));
        User user = userRepository.findByToken(token).orElseThrow(() -> new RuntimeException("User not found"));
 
 
        List<User> playersCopy = new ArrayList<>(game.getPlayers());
        for (User player : playersCopy) {
            game.removePlayer(player);
            player.setStatus(UserStatus.ONLINE);
            player.setGame(null);
            userRepository.save(player);
        }
 
        gameRepository.delete(game);
    }
 
    public CitiesGetDTO returnCities(CitiesPostDTO citiesPostDTO) {
        List<City> selectedCities = new ArrayList<>();
        CitiesGetDTO citiesGetDTO = new CitiesGetDTO();
        for (int i = 0; i < citiesPostDTO.getRoundCount(); i++) {
            selectedCities.add(getRandomCity());
        }
 
        citiesGetDTO.setCities(selectedCities);
 
        return citiesGetDTO;
    }
 
 
}
 
