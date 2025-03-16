package com.example.prac.service.data;

import com.example.prac.model.dataEntity.City;
import com.example.prac.repository.data.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City findOrCreateCity(String cityName) {
        City city = cityRepository.findByName(cityName);
        if (city == null) {
            city = new City();
            city.setName(cityName);
            city = cityRepository.save(city);
        }
        return city;
    }
}