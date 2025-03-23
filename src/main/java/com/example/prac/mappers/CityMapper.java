package com.example.prac.mappers;


import com.example.prac.data.res.CityDTO;
import com.example.prac.data.model.City;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityMapper implements Mapper<City, CityDTO> {
    private final ModelMapper modelMapper;

    @Override
    public CityDTO mapTo(City city) {
        return modelMapper.map(city, CityDTO.class);
    }

    @Override
    public City mapFrom(CityDTO cityDTO) {
        return modelMapper.map(cityDTO, City.class);
    }

    public String mapToName(City city) {
        return city != null ? city.getName() : null;
    }
}