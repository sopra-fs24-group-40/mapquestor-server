package ch.uzh.ifi.hase.soprafs24.service;

import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.repository.CityRepository;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class CityServiceTest {

    @Test
    public void testSaveCities() {
        // Given
        CityRepository cityRepositoryMock = mock(CityRepository.class);
        CityService cityService = new CityService(cityRepositoryMock);

        List<City> citiesToSave = new ArrayList<>();
        citiesToSave.add(createCity("Zurich", "Switzerland", 47.3769, 8.5417, "no"));
        citiesToSave.add(createCity("Berlin", "Germany", 52.5200, 13.4050, "yes"));

        // When
        cityService.saveCities(citiesToSave);

        // Then
        for (City city : citiesToSave) {
            verify(cityRepositoryMock).save(city);
        }
    }

    // Helper method to create a City object
    private City createCity(String name, String capital, double latitude, double longitude, String isCapital) {
        City city = new City();
        city.setName(name);
        city.setCapital(capital);
        city.setLatitude(latitude);
        city.setLongitude(longitude);
        return city;
    }
}
