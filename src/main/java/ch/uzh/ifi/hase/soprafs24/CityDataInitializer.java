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
                new City("France", "Paris", 48.87126711447683, 2.302802377552483),
                new City("Germany", "Berlin", 52.5040109865205, 13.337379751410454),
                new City("Italy", "Rome", 41.902804456960965, 12.495961409167057),
                new City("Spain", "Madrid", 40.41632625763435, -3.7135292191619063),
                new City("United Kingdom", "London", 51.50725879175181, -0.12798662320142665),
                new City("Russia", "Moscow", 55.75385519987297, 37.620153029413565),
                new City("Netherlands", "Amsterdam", 52.3736446591349, 4.890838633614255),
                new City("Switzerland", "Bern", 46.94824798153324, 7.447493709652632),
                new City("Austria", "Vienna", 48.20833208376515, 16.37405995559292),
                new City("Sweden", "Stockholm", 59.329248101515084, 18.0682102988283),
                new City("Norway", "Oslo", 59.914410806409, 10.75264381448203),
                new City("Denmark", "Copenhagen", 55.67599817441607, 12.567900330576071),
                new City("Greece", "Athens", 37.97143207373527, 23.7255191300171),
                new City("Portugal", "Lisbon", 38.70774136831338, -9.136549755718937),
                new City("Belgium", "Brussels", 50.850351323154925, 4.351740823781303),
                new City("Poland", "Warsaw", 52.23070175163676, 21.01141063776464),
                new City("Czech Republic", "Prague", 50.07582097488927, 14.437611661821807),
                new City("Hungary", "Budapest", 47.49824808274062, 19.040195055111752),
                new City("Finland", "Helsinki", 60.16798676581166, 24.956970641795788),
                new City("Ireland", "Dublin", 53.349781191841764, -6.260425188337249),
                new City("Romania", "Bucharest", 44.43196487702438, 26.097091367529373),
                new City("Turkey", "Ankara", 39.94410041565228, 32.854601189249514),
                new City("Ukraine", "Kyiv", 50.45010810318563, 30.52420379546972),
                new City("Serbia", "Belgrade", 44.80499038074344, 20.47051567119739),
                new City("Croatia", "Zagreb", 45.81474041940831, 15.978442496771061),
                new City("Bulgaria", "Sofia", 42.69600915673251, 23.331792147904387),
                new City("Slovakia", "Bratislava", 48.143093298346464, 17.117232023538225),
                new City("Lithuania", "Vilnius", 54.68387062376313, 25.292997380682753),
                new City("Latvia", "Riga", 56.94953542722532, 24.105198073897352),
                new City("Estonia", "Tallinn", 59.43710642443398, 24.745484017385568),
                new City("Slovenia", "Ljubljana", 46.05216908927745, 14.510280706880367),
                new City("Montenegro", "Podgorica", 42.43892440986343, 19.262239282177894),
                new City("North Macedonia", "Skopje", 41.991263267543346, 21.429292808223682),
                new City("Luxembourg", "Luxembourg City", 49.60888628922198, 6.131709146165798),
                new City("Malta", "Valletta", 35.89928621551829, 14.514049671267626),
                new City("Andorra", "Andorra la Vella", 42.507609039758954, 1.5224654968279714),
                new City("San Marino", "San Marino", 43.936002536205756, 12.44676512458346),
                new City("Liechtenstein", "Vaduz", 47.13627160799362, 9.522246480788212),
                new City("Monaco", "Monaco", 43.739295613361584, 7.427843956067588),
                new City("Vatican City", "Vatican City", 41.902283460769716, 12.45896035312199),
                new City("Belarus", "Minsk", 53.89396241050173, 27.563408308930303),
                new City("Moldova", "Chisinau", 47.01312857730332, 28.84674184962407),
                new City("Bosnia and Herzegovina", "Sarajevo", 43.858851531242124, 18.421260736051778),
                new City("Albania", "Tirana", 41.32281184481657, 19.819903381065938),
                new City("Kosovo", "Pristina", 42.65757093351852, 21.161787507807528),
                new City("Iceland", "Reykjavík", 64.14959309492315, -21.931267472059414)
        );
        return countries;
    }
}