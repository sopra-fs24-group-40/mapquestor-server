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
        System.out.println("Cities inserted:");
        for (City city : countries) {
            System.out.println(city);
        }
    }

    private List<City> getCountriesList() {
        // Define your list of countries here
        List<City> countries = List.of(
            new City("France", "Paris", 48.8566, 2.3522),
            new City("Germany", "Berlin", 52.5200, 13.4050),
            new City("Italy", "Rome", 41.9028, 12.4964),
            new City("Spain", "Madrid", 40.4168, -3.7038),
            new City("United Kingdom", "London", 51.5074, -0.1278),
            new City("Russia", "Moscow", 55.7558, 37.6176),
            new City("Netherlands", "Amsterdam", 52.3676, 4.9041),
            new City("Switzerland", "Bern", 46.9480, 7.4474),
            new City("Austria", "Vienna", 48.2082, 16.3738),
            new City("Sweden", "Stockholm", 59.3293, 18.0686),
            new City("Norway", "Oslo", 59.9139, 10.7522),
            new City("Denmark", "Copenhagen", 55.6761, 12.5683),
            new City("Greece", "Athens", 37.9838, 23.7275),
            new City("Portugal", "Lisbon", 38.7223, -9.1393),
            new City("Belgium", "Brussels", 50.8503, 4.3517),
            new City("Poland", "Warsaw", 52.2297, 21.0122),
            new City("Czech Republic", "Prague", 50.0755, 14.4378),
            new City("Hungary", "Budapest", 47.4979, 19.0402),
            new City("Finland", "Helsinki", 60.1695, 24.9354),
            new City("Ireland", "Dublin", 53.3498, -6.2603),
            new City("Romania", "Bucharest", 44.4268, 26.1025),
            new City("Turkey", "Ankara", 39.9334, 32.8597),
            new City("Ukraine", "Kyiv", 50.4501, 30.5234),
            new City("Serbia", "Belgrade", 44.7866, 20.4489),
            new City("Croatia", "Zagreb", 45.8150, 15.9819),
            new City("Bulgaria", "Sofia", 42.6977, 23.3219),
            new City("Slovakia", "Bratislava", 48.1486, 17.1077),
            new City("Lithuania", "Vilnius", 54.6872, 25.2797),
            new City("Latvia", "Riga", 56.9496, 24.1052),
            new City("Estonia", "Tallinn", 59.4370, 24.7536),
            new City("Slovenia", "Ljubljana", 46.0569, 14.5058),
            new City("Montenegro", "Podgorica", 42.4304, 19.2594),
            new City("Macedonia", "Skopje", 41.9973, 21.4280),
            new City("Luxembourg", "Luxembourg City", 49.6116, 6.1319),
            new City("Malta", "Valletta", 35.8989, 14.5146),
            new City("Andorra", "Andorra la Vella", 42.5063, 1.5218),
            new City("San Marino", "San Marino", 43.9424, 12.4578),
            new City("Liechtenstein", "Vaduz", 47.1410, 9.5209),
            new City("Monaco", "Monaco", 43.7384, 7.4246),
            new City("Vatican City", "Vatican City", 41.9029, 12.4534)
            );
        return countries;
    }
}