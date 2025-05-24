package aviasales.common.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import aviasales.common.data.entity.Airline;
import aviasales.common.errorHandler.AirlineNotFoundException;
import aviasales.common.repository.AirlineRepository;

@Service
@AllArgsConstructor
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public Airline findByName(String name) {
        return airlineRepository.findByName(name)
                .orElseThrow(() -> new AirlineNotFoundException("Авиакомпания не найдена: " + name));
    }
}