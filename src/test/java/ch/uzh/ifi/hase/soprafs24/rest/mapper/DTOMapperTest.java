package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.*;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    public void testConvertUserPostDTOtoEntity() {
        // Given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testPassword");

        // When
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // Then
        assertNotNull(user);
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
    }

    @Test
    public void testConvertEntityToUserPostDTO() {
        // Given
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        // When
        UserPostDTO userPostDTO = DTOMapper.INSTANCE.convertEntityToUserPostDTO(user);

        // Then
        assertNotNull(userPostDTO);
        assertEquals(user.getUsername(), userPostDTO.getUsername());
        assertEquals(user.getPassword(), userPostDTO.getPassword());
    }

    @Test
    public void testConvertEntityToUserPutDTO() {
        // Given
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        // When
        UserPutDTO userPutDTO = DTOMapper.INSTANCE.convertEntityToUserPutDTO(user);

        // Then
        assertNotNull(userPutDTO);
        assertEquals(user.getUsername(), userPutDTO.getUsername());
    }

    @Test
    public void testConvertEntityToUserGetDTO() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setPlayedGames(5);
        user.setWonGames(2);
        user.setToken("testToken");
        user.setStatus(UserStatus.ONLINE);

        // When
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // Then
        assertNotNull(userGetDTO);
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getPlayedGames(), userGetDTO.getPlayedGames());
        assertEquals(user.getWonGames(), userGetDTO.getWonGames());
        assertEquals(user.getToken(), userGetDTO.getToken());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void testConvertUserPutDTOtoEntity() {
        // Given
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("testUsername");

        // When
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // Then
        assertNotNull(user);
        assertEquals(userPutDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testConvertEntityToGameInfoDTO() {
        // Given
        Game game = new Game();
        game.setGameCode("testGameCode");
        game.setCreator("testCreator");
        game.setPlayerCount(5);
        game.setMaxPlayers(10);
        game.setRoundCount(3);
        game.setGameType(GameType.COUNTRY); // Assuming GameType.TYPE1 exists
        game.setGameStatus(GameStatus.LOBBY); // Assuming GameStatus.STARTED exists

        // When
        GameInfoDTO gameInfoDTO = DTOMapper.INSTANCE.convertEntityToGameInfoDTO(game);

        // Then
        assertNotNull(gameInfoDTO);
        assertEquals(game.getGameCode(), gameInfoDTO.getGameCode());
        assertEquals(game.getCreator(), gameInfoDTO.getCreator());
        assertEquals(game.getPlayerCount(), gameInfoDTO.getPlayerCount());
        assertEquals(game.getMaxPlayers(), gameInfoDTO.getMaxPlayers());
        assertEquals(game.getRoundCount(), gameInfoDTO.getRoundCount());
        assertEquals(game.getGameType(), gameInfoDTO.getGameType());
        assertEquals(game.getGameStatus(), gameInfoDTO.getGameStatus());
        // Add assertions for players and cities if necessary
    }


    @Test
    public void testSetCitiesCitiesGetDTO() {
        // Given
        List<City> cities = new ArrayList<>();
        City city1 = new City();
        city1.setName("City 1");
        City city2 = new City();
        city2.setName("City 2");
        cities.add(city1);
        cities.add(city2);

        // When
        CitiesGetDTO citiesGetDTO = new CitiesGetDTO();
        citiesGetDTO.setCities(cities);

        // Then
        assertNotNull(citiesGetDTO);
        assertNotNull(citiesGetDTO.getCities());
        assertEquals(2, citiesGetDTO.getCities().size());
        assertEquals("City 1", citiesGetDTO.getCities().get(0).getName());
        assertEquals("City 2", citiesGetDTO.getCities().get(1).getName());
    }

    @Test
    public void testSetRoundCountCitiesPostDTO() {
        // Given
        int roundCount = 5;

        // When
        CitiesPostDTO citiesPostDTO = new CitiesPostDTO();
        citiesPostDTO.setRoundCount(roundCount);

        // Then
        assertEquals(roundCount, citiesPostDTO.getRoundCount());
    }

    @Test
    public void testCityDTOConstructorAndGettersCityDTO() {
        // Given
        Long id = 1L;
        String name = "Zurich";
        String capital = "Switzerland";
        double longitude = 47.3769;
        double latitude = 8.5417;

        // When
        CityDTO cityDTO = new CityDTO(id, name, capital, longitude, latitude);

        // Then
        assertNotNull(cityDTO);
        assertEquals(id, cityDTO.getId());
        assertEquals(name, cityDTO.getName());
        assertEquals(capital, cityDTO.getCapital());
        assertEquals(longitude, cityDTO.getLongitude());
        assertEquals(latitude, cityDTO.getLatitude());
    }

    @Test
    public void testCityDTOSettersCityDTO() {
        // Given
        CityDTO cityDTO = new CityDTO();

        // When
        Long id = 1L;
        String name = "Zurich";
        String capital = "Switzerland";
        double longitude = 47.3769;
        double latitude = 8.5417;

        cityDTO.setId(id);
        cityDTO.setName(name);
        cityDTO.setCapital(capital);
        cityDTO.setLongitude(longitude);
        cityDTO.setLatitude(latitude);

        // Then
        assertEquals(id, cityDTO.getId());
        assertEquals(name, cityDTO.getName());
        assertEquals(capital, cityDTO.getCapital());
        assertEquals(longitude, cityDTO.getLongitude());
        assertEquals(latitude, cityDTO.getLatitude());
    }

    @Test
    public void testCreateGameDTOSettersAndGettersCreateGameDTO() {
        // Given
        String creator = "testCreator";
        int maxPlayers = 4;
        int roundCount = 3;
        GameType gameType = GameType.COUNTRY;

        // When
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setCreator(creator);
        createGameDTO.setMaxPlayers(maxPlayers);
        createGameDTO.setRoundCount(roundCount);
        createGameDTO.setGameType(gameType);

        // Then
        assertNotNull(createGameDTO);
        assertEquals(creator, createGameDTO.getCreator());
        assertEquals(maxPlayers, createGameDTO.getMaxPlayers());
        assertEquals(roundCount, createGameDTO.getRoundCount());
        assertEquals(gameType, createGameDTO.getGameType());
    }

    @Test
    public void testNotNullAnnotationsCreateGameDTO() throws NoSuchFieldException {
        // Given
        var fields = CreateGameDTO.class.getDeclaredFields();

        // Then
        for (var field : fields) {
            if (field.isAnnotationPresent(NotNull.class)) {
                assertNotNull(field.getAnnotations());
            }
        }
    }

    @Test
    public void testSettersAndGettersGameInfoDTO() {
        // Given
        String gameCode = "ABC123";
        String creator = "testCreator";
        int playerCount = 3;
        int maxPlayers = 4;
        int roundCount = 5;
        GameType gameType = GameType.CITY;
        GameStatus gameStatus = GameStatus.INGAME;
        List<PlayerInfoDTO> players = new ArrayList<>();
        List<City> cities = new ArrayList<>();

        // When
        GameInfoDTO gameInfoDTO = new GameInfoDTO();
        gameInfoDTO.setGameCode(gameCode);
        gameInfoDTO.setCreator(creator);
        gameInfoDTO.setPlayerCount(playerCount);
        gameInfoDTO.setMaxPlayers(maxPlayers);
        gameInfoDTO.setRoundCount(roundCount);
        gameInfoDTO.setGameType(gameType);
        gameInfoDTO.setGameStatus(gameStatus);
        gameInfoDTO.setPlayers(players);
        gameInfoDTO.setCities(cities);

        // Then
        assertNotNull(gameInfoDTO);
        assertEquals(gameCode, gameInfoDTO.getGameCode());
        assertEquals(creator, gameInfoDTO.getCreator());
        assertEquals(playerCount, gameInfoDTO.getPlayerCount());
        assertEquals(maxPlayers, gameInfoDTO.getMaxPlayers());
        assertEquals(roundCount, gameInfoDTO.getRoundCount());
        assertEquals(gameType, gameInfoDTO.getGameType());
        assertEquals(gameStatus, gameInfoDTO.getGameStatus());
        assertEquals(players, gameInfoDTO.getPlayers());
        assertEquals(cities, gameInfoDTO.getCities());
    }

    @Test
    public void testSettersAndGettersGameStatusDTO() {
        // Given
        GameStatus status = GameStatus.INGAME;

        // When
        GameStatusDTO gameStatusDTO = new GameStatusDTO();
        gameStatusDTO.setStatus(status);

        // Then
        assertNotNull(gameStatusDTO);
        assertEquals(status, gameStatusDTO.getStatus());
    }

    @Test
    public void testSettersAndGettersPlayerInfoDTO() {
        // Given
        String username = "testUser";
        String token = "testToken";
        int points = 100;

        // When
        PlayerInfoDTO playerInfoDTO = new PlayerInfoDTO();
        playerInfoDTO.setUsername(username);
        playerInfoDTO.setToken(token);
        playerInfoDTO.setPoints(points);

        // Then
        assertNotNull(playerInfoDTO);
        assertEquals(username, playerInfoDTO.getUsername());
        assertEquals(token, playerInfoDTO.getToken());
        assertEquals(points, playerInfoDTO.getPoints());
    }

    @Test
    public void testSettersAndGettersUserGameInfoDTO() {
        // Given
        String token = "testToken";

        // When
        UserGameInfoDTO userGameInfoDTO = new UserGameInfoDTO();
        userGameInfoDTO.setToken(token);

        // Then
        assertNotNull(userGameInfoDTO);
        assertEquals(token, userGameInfoDTO.getToken());
    }

    @Test
    public void testSettersAndGettersUserTokenDTO() {
        // Given
        String token = "testToken";

        // When
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken(token);

        // Then
        assertNotNull(userTokenDTO);
        assertEquals(token, userTokenDTO.getToken());
    }
}
