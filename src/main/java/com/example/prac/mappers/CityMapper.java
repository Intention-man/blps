package com.example.prac.mappers;


import com.example.prac.data.model.City;
import com.example.prac.service.CityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityMapper implements Mapper<City, String> {
    private final ModelMapper modelMapper;
    private final CityService cityService;

    @Override
    public String mapTo(City city) {
        return city.getName();
    }

    @Override
    public City mapFrom(String cityName) {
        return cityService.findByName(cityName);
    }

    public String mapToName(City city) {
        return city != null ? city.getName() : null;
    }
}