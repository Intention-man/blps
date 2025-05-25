package aviasales.data.city;

import aviasales.exception.CityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City findByName(String cityName) {
        return cityRepository.findByName(cityName)
                .orElseThrow(() -> new CityNotFoundException("Город не найден: " + cityName));
    }

    public City save(String cityName) {
        return cityRepository.save(new City(cityName));
    }

    public void ensureCityExists(String cityName) {
        cityRepository.findByName(cityName).orElseGet(() -> save(cityName));
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}