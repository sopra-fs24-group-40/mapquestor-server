package ch.uzh.ifi.hase.soprafs24;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ch.uzh.ifi.hase.soprafs24.entity.City;
import ch.uzh.ifi.hase.soprafs24.service.CityService;

import java.util.List;


@Component
public class CityDataInitializer implements CommandLineRunner {

    private final CityService cityService;

    public CityDataInitializer(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<City> countries = getCountriesList();
        cityService.saveCities(countries);

        // Print the list of cities to the console
    }

    private List<City> getCountriesList() {
        // Define your list of countries here
        List<City> countries = List.of(
                new City("France", "Paris", 2.3522, 48.8566),
                new City("Germany", "Berlin", 13.4050, 52.5200),
                new City("Italy", "Rome", 12.4964, 41.9028),
                new City("Spain", "Madrid", -3.7038, 40.4168),
                new City("United Kingdom", "London", -0.1278, 51.5074),
                new City("Russia", "Moscow", 37.6176, 55.7558),
                new City("Netherlands", "Amsterdam", 4.9041, 52.3676),
                new City("Switzerland", "Bern", 7.4474, 46.9480),
                new City("Austria", "Vienna", 16.3738, 48.2082),
                new City("Sweden", "Stockholm", 18.0686, 59.3293),
                new City("Norway", "Oslo", 10.7522, 59.9139),
                new City("Denmark", "Copenhagen", 12.5683, 55.6761),
                new City("Greece", "Athens", 23.7275, 37.9838),
                new City("Portugal", "Lisbon", -9.1393, 38.7223),
                new City("Belgium", "Brussels", 4.3517, 50.8503),
                new City("Poland", "Warsaw", 21.0122, 52.2297),
                new City("Czech Republic", "Prague", 14.4378, 50.0755),
                new City("Hungary", "Budapest", 19.0402, 47.4979),
                new City("Finland", "Helsinki", 24.9354, 60.1695),
                new City("Ireland", "Dublin", -6.2603, 53.3498),
                new City("Romania", "Bucharest", 26.1025, 44.4268),
                new City("Turkey", "Ankara", 32.8597, 39.9334),
                new City("Ukraine", "Kyiv", 30.5234, 50.4501),
                new City("Serbia", "Belgrade", 20.4489, 44.7866),
                new City("Croatia", "Zagreb", 15.9819, 45.8150),
                new City("Bulgaria", "Sofia", 23.3219, 42.6977),
                new City("Slovakia", "Bratislava", 17.1077, 48.1486),
                new City("Lithuania", "Vilnius", 25.2797, 54.6872),
                new City("Latvia", "Riga", 24.1052, 56.9496),
                new City("Estonia", "Tallinn", 24.7536, 59.4370),
                new City("Slovenia", "Ljubljana", 14.5058, 46.0569),
                new City("Montenegro", "Podgorica", 19.2594, 42.4304),
                new City("Macedonia", "Skopje", 21.4280, 41.9973),
                new City("Luxembourg", "Luxembourg City", 6.1319, 49.6116),
                new City("Malta", "Valletta", 14.5146, 35.8989),
                new City("Andorra", "Andorra la Vella", 1.5218, 42.5063),
                new City("San Marino", "San Marino", 12.4578, 43.9424),
                new City("Liechtenstein", "Vaduz", 9.5209, 47.1410),
                new City("Monaco", "Monaco", 7.4246, 43.7384),
                new City("Vatican City", "Vatican City", 12.4534, 41.9029)
        );
        return countries;
    }
}