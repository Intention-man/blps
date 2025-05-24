package aviasales.common.service;

import aviasales.common.data.entity.Airline;
import aviasales.common.errorHandler.AirlineNotFoundException;
import aviasales.common.repository.AirlineRepository;
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