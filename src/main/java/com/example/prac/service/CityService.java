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

    public City findOrCreateCity(String cityName) {
        return cityRepository.findByName(cityName)
                .orElseGet(() -> {
                    City newCity = new City();
                    newCity.setName(cityName);
                    return cityRepository.save(newCity);
                });
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}