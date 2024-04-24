package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByName_success() {
    // given
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("firstname@lastname");
    user.setStatus(UserStatus.ONLINE);
    user.setToken("1");
    user.setCreation_date(LocalDateTime.now());
    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByUsername(user.getUsername()).orElse(null);

    // then
    assertNotNull(found.getId());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getStatus(), user.getStatus());
  }
}
