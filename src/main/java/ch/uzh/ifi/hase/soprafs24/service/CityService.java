package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.repository.CityRepository;
import org.springframework.stereotype.Service;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.CityDTO;
import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public void saveCities(List<City> cities) {
        for (City citi : cities) {
            City city = new City();
            city.setName(citi.getName());
            city.setLongitude(citi.getLongitude());
            city.setLatitude(citi.getLatitude());
            city.setCapital(citi.getCapital());
            cityRepository.save(city);
        }
    }
}