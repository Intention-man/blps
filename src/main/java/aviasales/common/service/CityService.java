package aviasales.common.service;

import aviasales.common.data.entity.City;
import aviasales.common.errorHandler.CityNotFoundException;
import aviasales.common.repository.CityRepository;
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

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}