package com.example.prac.mappers;

import com.example.prac.data.DTO.simple.req.SimpleRouteSearchRequestDTO;
import com.example.prac.data.model.Airline;
import com.example.prac.data.model.City;
import com.example.prac.data.model.ServiceClass;
import com.example.prac.data.model.SimpleRouteSearchRequest;
import com.example.prac.service.AirlineService;
import com.example.prac.service.CityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SimpleRouteSearchRequestMapper implements Mapper<SimpleRouteSearchRequest, SimpleRouteSearchRequestDTO>{

    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final AirlineService airlineService;

    @Override
    public SimpleRouteSearchRequestDTO mapTo(SimpleRouteSearchRequest req) {
        return modelMapper.map(req, SimpleRouteSearchRequestDTO.class);
    }

    @Override
    public SimpleRouteSearchRequest mapFrom(SimpleRouteSearchRequestDTO reqDTO) {
        return modelMapper.map(reqDTO, SimpleRouteSearchRequest.class);
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