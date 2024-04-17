package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameId(Long gameId);

    Optional<Game> findByGameCode(String gameCode);

    boolean existsByGameId(Long gameId);

}
