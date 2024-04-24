package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void testFindByGameId_NonExistingGame_ReturnsEmptyOptional() {
        // When
        Optional<Game> optionalGame = gameRepository.findById(999L);

        // Then
        assertTrue(optionalGame.isEmpty());
    }

    @Test
    public void testFindByGameCode_ExistingGameCode_ReturnsGame() {
        // Given
        String gameCode = "ABC123";
        Game game = new Game();
        game.setGameCode(gameCode);
        game.setRoundCount(1); // Satisfy # of Rounds constraint
        game.setMaxPlayers(2); // Satisfy Maximum players constraint
        game.setCreator("Creator"); // Satisfy Creator constraint
        gameRepository.save(game);

        // When
        Optional<Game> optionalGame = gameRepository.findByGameCode(gameCode);

        // Then
        assertTrue(optionalGame.isPresent());
        assertEquals(game, optionalGame.get());
    }

    @Test
    public void testFindByGameCode_NonExistingGameCode_ReturnsEmptyOptional() {
        // When
        Optional<Game> optionalGame = gameRepository.findByGameCode("NonExistingGameCode");

        // Then
        assertTrue(optionalGame.isEmpty());
    }

    @Test
    public void testExistsByGameId_NonExistingGameId_ReturnsFalse() {
        // When
        boolean exists = gameRepository.existsById(999L);

        // Then
        assertFalse(exists);
    }
}
