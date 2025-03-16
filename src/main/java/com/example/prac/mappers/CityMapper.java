package com.example.prac.mappers;


import com.example.prac.DTO.data.CityDTO;
import com.example.prac.model.dataEntity.City;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CityMapper implements Mapper<City, CityDTO> {
    private final ModelMapper modelMapper;

    public CityMapper() {
        this.modelMapper = new ModelMapper();
    }

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