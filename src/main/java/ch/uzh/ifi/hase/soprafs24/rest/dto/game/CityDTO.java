package ch.uzh.ifi.hase.soprafs24.rest.dto.game;

public class CityDTO {

    private Long id;
    private String name;
    private String capital;
    private double longitude;
    private double latitude;

    // Constructors
    public CityDTO() {
    }

    public CityDTO(Long id, String name, String capital, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}