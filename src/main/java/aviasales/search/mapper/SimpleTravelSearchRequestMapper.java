package aviasales.search.mapper;

import aviasales.data.airline.AirlineService;
import aviasales.data.city.CityService;
import aviasales.data.ticket.ServiceClass;
import aviasales.search.data.dto.SimpleTravelSearchRequestDTO;
import aviasales.search.data.entity.Route;
import aviasales.search.data.entity.SimpleTravelSearchRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SimpleTravelSearchRequestMapper {

    private final ModelMapper modelMapper;

    private final CityService cityService;
    private final AirlineService airlineService;


    public SimpleTravelSearchRequestDTO mapTo(SimpleTravelSearchRequest req) {
        return modelMapper.map(req, SimpleTravelSearchRequestDTO.class);
    }

    public SimpleTravelSearchRequest mapFrom(SimpleTravelSearchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return SimpleTravelSearchRequest.builder()
                .passengerCount(dto.getPassengerCount())
                .serviceClass(ServiceClass.valueOf(dto.getServiceClass()))
                .maxPrice(dto.getMaxPrice())
                .maxTravelTime(dto.getMaxTravelTime())
                .numberOfTransfers(dto.getNumberOfTransfers())
                .availableAirlines(dto.getAvailableAirlines().stream()
                        .map(airlineService::findByName)
                        .collect(Collectors.toList()))
                .departureCity(cityService.findByName(dto.getDepartureCity()))
                .arrivalCity(cityService.findByName(dto.getArrivalCity()))
                .departureDateStart(dto.getDepartureDateStart())
                .departureDateFinish(dto.getDepartureDateFinish())
                .departureTimeStart(dto.getDepartureTimeStart())
                .departureTimeFinish(dto.getDepartureTimeFinish())
                .arrivalDateStart(dto.getArrivalDateStart())
                .arrivalDateFinish(dto.getArrivalDateFinish())
                .arrivalTimeStart(dto.getArrivalTimeStart())
                .arrivalTimeFinish(dto.getArrivalTimeFinish())
                .build();
    }

    public SimpleTravelSearchRequest mapFrom2(SimpleTravelSearchRequestDTO dto, Route route) {


        return SimpleTravelSearchRequest.builder()
                .passengerCount(dto.getPassengerCount())
                .serviceClass(ServiceClass.valueOf(dto.getServiceClass()))
                .maxPrice(dto.getMaxPrice() - route.getTotalPrice())
                .maxTravelTime(dto.getMaxTravelTime())
                .numberOfTransfers(dto.getNumberOfTransfers())
                .availableAirlines(dto.getAvailableAirlines().stream()
                        .map(airlineService::findByName)
                        .collect(Collectors.toList()))
                .departureCity(cityService.findByName(dto.getArrivalCity()))
                .arrivalCity(cityService.findByName(dto.getDepartureCity()))
                .departureDateStart(dto.getBackDepartureDateStart())
                .departureDateFinish(dto.getBackDepartureDateFinish())
                .departureTimeStart(dto.getBackDepartureTimeStart())
                .departureTimeFinish(dto.getBackDepartureTimeFinish())
                .arrivalDateStart(dto.getBackArrivalDateStart())
                .arrivalDateFinish(dto.getBackArrivalDateFinish())
                .arrivalTimeStart(dto.getBackArrivalTimeStart())
                .arrivalTimeFinish(dto.getBackArrivalTimeFinish())
                .build();
    }
}