package aviasales.common.mappers;


import aviasales.common.data.entity.City;
import aviasales.common.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityMapper implements Mapper<City, String> {
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