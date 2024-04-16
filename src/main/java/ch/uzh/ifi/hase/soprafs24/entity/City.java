package ch.uzh.ifi.hase.soprafs24.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    private String name;
    private String capital;
    private double longitude;
    private double latitude;

    // Constructors
    public City() {
    }

    public City(String name, String capital, double longitude, double latitude) {
        this.name = name;
        this.capital = capital;
        this.longitude = longitude;
        this.latitude = latitude;
    }


}