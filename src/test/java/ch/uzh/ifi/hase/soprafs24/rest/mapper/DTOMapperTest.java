package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameInfoDTO;
import org.junit.jupiter.api.Test;

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
        assertEquals(user.getPassword(), userPutDTO.getPassword());
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
        userPutDTO.setPassword("testPassword");

        // When
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // Then
        assertNotNull(user);
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getPassword(), user.getPassword());
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
}
