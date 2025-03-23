package com.example.prac.mappers;

import com.example.prac.data.req.simple.SimpleTravelSearchRequestDTO;
import com.example.prac.data.model.SimpleTravelSearchRequest;
import com.example.prac.service.AirlineService;
import com.example.prac.service.CityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleTravelSearchRequestMapper implements Mapper<SimpleTravelSearchRequest, SimpleTravelSearchRequestDTO>{

    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final AirlineService airlineService;

    @Override
    public SimpleTravelSearchRequestDTO mapTo(SimpleTravelSearchRequest req) {
        return modelMapper.map(req, SimpleTravelSearchRequestDTO.class);
    }

    @Override
    public SimpleTravelSearchRequest mapFrom(SimpleTravelSearchRequestDTO reqDTO) {
        return modelMapper.map(reqDTO, SimpleTravelSearchRequest.class);
    }

//    public SimpleRouteSearchRequest mapToEntity(SimpleRouteSearchRequest request) {
//        ServiceClass serviceClass = ServiceClass.valueOf(request.getServiceClass());
//
//        City departureCity = cityService.findByName(request.getDepartureCity());
//        City arrivalCity = cityService.findByName(String.valueOf(request.getArrivalCity()));
//
//        LocalDate departureDateStart = LocalDate.parse(request.getDepartureDateStart());
//        LocalDate departureDateFinish = LocalDate.parse(request.getDepartureDateFinish());
//        LocalTime departureTimeStart = LocalTime.parse(request.getDepartureTimeStart());
//        LocalTime departureTimeFinish = LocalTime.parse(request.getDepartureTimeFinish());
//
//        LocalDate arrivalDateStart = LocalDate.parse(request.getArrivalDateStart());
//        LocalDate arrivalDateFinish = LocalDate.parse(request.getArrivalDateFinish());
//        LocalTime arrivalTimeStart = LocalTime.parse(request.getArrivalTimeStart());
//        LocalTime arrivalTimeFinish = LocalTime.parse(request.getArrivalTimeFinish());
//
//        List<Airline> availableAirlines = request.getAvailableAirlines()
//                .stream()
//                .map(airlineName -> airlineService.findByName(airlineName))
//                .collect(Collectors.toList());
//
//        return SimpleRouteSearchRequest.builder()
//                .passengerCount(request.getPassengerCount())
//                .serviceClass(serviceClass)
//                .maxPrice(request.getMaxPrice())
//                .maxTravelHours(request.getMaxTravelHours())
//                .numberOfTransfers(request.getNumberOfTransfers())
//                .availableAirlines(availableAirlines)
//                .departureCity(departureCity)
//                .departureDateStart(departureDateStart)
//                .departureDateFinish(departureDateFinish)
//                .departureTimeStart(departureTimeStart)
//                .departureTimeFinish(departureTimeFinish)
//                .arrivalCity(arrivalCity)
//                .arrivalDateStart(arrivalDateStart)
//                .arrivalDateFinish(arrivalDateFinish)
//                .arrivalTimeStart(arrivalTimeStart)
//                .arrivalTimeFinish(arrivalTimeFinish)
//                .build();
//    }
}