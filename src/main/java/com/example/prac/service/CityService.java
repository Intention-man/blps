package com.example.prac.service;

import com.example.prac.data.model.City;
import com.example.prac.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City findByName(String name) {
        return cityRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("City not found: " + name));
    }

    public City findOrCreateCity(String cityName) {
        Optional<City> city = cityRepository.findByName(cityName);
        if (city == null) {
            city = new City();
            city.setName(cityName);
            city = cityRepository.save(city);
        }
        return city;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}