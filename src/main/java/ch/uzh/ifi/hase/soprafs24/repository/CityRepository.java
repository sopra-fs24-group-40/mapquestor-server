package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CityRepository extends JpaRepository<City, Long> {
    // You can add custom query methods here if needed
}

