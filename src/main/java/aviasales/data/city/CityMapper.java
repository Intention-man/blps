package aviasales.data.city;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityMapper {
    private final CityService cityService;

    public String mapTo(City city) {
        return city.getName();
    }

    public City mapFrom(String cityName) {
        return cityService.findByName(cityName);
    }

    public String mapToName(City city) {
        return city != null ? city.getName() : null;
    }
}