package ch.uzh.ifi.hase.soprafs24.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CreateGameDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.GameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserGameInfoDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;



@WebMvcTest(GameController.class)
public class GameControllerTest {

    // Testing

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void testCreateGame() throws Exception {
        CreateGameDTO createGameDTO = new CreateGameDTO();
        // set properties of createGameDTO

        when(gameService.createGame(any(CreateGameDTO.class))).thenReturn(new Game());

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createGameDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetGames() throws Exception {
        List<GameInfoDTO> gameInfoDTOList = Collections.singletonList(new GameInfoDTO());
        when(gameService.getGames()).thenReturn(gameInfoDTOList);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))); // Adjust based on the expected size of the list
    }

    @Test
    public void testJoinGame() throws Exception {
        String gameCode = "sampleGameCode";
        UserGameInfoDTO userGameInfoDTO = new UserGameInfoDTO();
        // set properties of userGameInfoDTO

        when(gameService.joinGame(gameCode, userGameInfoDTO.getToken())).thenReturn(new Game());

        mockMvc.perform(post("/games/{gameCode}/join", gameCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userGameInfoDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetGame() throws Exception {
        String gameCode = "sampleGameCode";
        GameInfoDTO gameInfoDTO = new GameInfoDTO();
        // set properties of gameInfoDTO

        when(gameService.getGame(gameCode)).thenReturn(gameInfoDTO);

        mockMvc.perform(get("/games/{gameCode}", gameCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameCode", is(gameInfoDTO.getGameCode()))); // Add more assertions as needed
    }

    @Test
    public void testGetGameUsernames() throws Exception {
        String gameCode = "sampleGameCode";
        List<String> usernames = Arrays.asList("user1", "user2"); // Sample usernames

        when(gameService.getUsersByGameId(gameCode)).thenReturn(usernames.stream()
                .map(username -> {
                    User user = new User();
                    user.setUsername(username);
                    return user;
                })
                .collect(Collectors.toList()));

        mockMvc.perform(get("/games/{gameCode}/users", gameCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(usernames.size()))); // Adjust based on the expected size
    }

    @Test
    public void testGetCity() throws Exception {
        // Mock the behavior to return a City object
        when(gameService.getRandomCity()).thenReturn(new City(/* parameters */));

        mockMvc.perform(get("/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists()); // Adjust based on the response structure
    }
    @Test
    public void testCreateGame_validInput() throws Exception {
        CreateGameDTO createGameDTO = new CreateGameDTO();
        // set properties of createGameDTO

        GameInfoDTO gameInfoDTO = new GameInfoDTO();
        // set properties of gameInfoDTO

        when(gameService.createGame(any(CreateGameDTO.class))).thenReturn(new Game());

        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createGameDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
                // Add more assertions as needed
    }

    @Test
    public void testJoinGame_validInput() throws Exception {
        String gameCode = "sampleGameCode";
        UserGameInfoDTO userGameInfoDTO = new UserGameInfoDTO();
        // set properties of userGameInfoDTO

        when(gameService.joinGame(gameCode, userGameInfoDTO.getToken())).thenReturn(new Game());

        MockHttpServletRequestBuilder postRequest = post("/games/{gameCode}/join", gameCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userGameInfoDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
                // Add more assertions as needed
    }

    // Helper method to convert objects to JSON strings
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
