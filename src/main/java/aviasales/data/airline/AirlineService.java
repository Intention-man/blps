package aviasales.data.airline;

import aviasales.exception.AirlineNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public Airline findByName(String name) {
        return airlineRepository.findByName(name)
                .orElseThrow(() -> new AirlineNotFoundException("Авиакомпания не найдена: " + name));
    }
}