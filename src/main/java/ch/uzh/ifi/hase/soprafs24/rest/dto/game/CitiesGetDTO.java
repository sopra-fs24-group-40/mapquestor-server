package ch.uzh.ifi.hase.soprafs24.rest.dto.game;

import ch.uzh.ifi.hase.soprafs24.entity.City;

import java.util.List;

public class CitiesGetDTO {

    List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
