package ch.uzh.ifi.hase.soprafs24.service;

import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.repository.CityRepository;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class CityServiceTest {

//    @Test
//    public void testSaveCities() {
//        // Given
//        CityRepository cityRepositoryMock = mock(CityRepository.class);
//        CityService cityService = new CityService(cityRepositoryMock);
//
//        List<City> citiesToSave = new ArrayList<>();
//        citiesToSave.add(createCity("Bern", "Switzerland", 46.94824798153324, 7.447493709652632, "yes"));
//        citiesToSave.add(createCity("Berlin", "Germany", 52.5040109865205, 13.337379751410454, "yes"));
//
//        // When
//        cityService.saveCities(citiesToSave);
//
//        // Then
//        for (City city : citiesToSave) {
//            verify(cityRepositoryMock).save(city);
//        }
//    }

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
