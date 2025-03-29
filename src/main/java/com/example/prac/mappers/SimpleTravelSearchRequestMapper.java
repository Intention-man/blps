package com.example.prac.mappers;

import com.example.prac.data.model.Route;
import com.example.prac.data.model.ServiceClass;
import com.example.prac.data.model.SimpleTravelSearchRequest;
import com.example.prac.data.req.SimpleTravelSearchRequestDTO;
import com.example.prac.service.AirlineService;
import com.example.prac.service.CityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SimpleTravelSearchRequestMapper implements Mapper<SimpleTravelSearchRequest, SimpleTravelSearchRequestDTO> {

    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final AirlineService airlineService;


    @Override
    public SimpleTravelSearchRequestDTO mapTo(SimpleTravelSearchRequest req) {
        return modelMapper.map(req, SimpleTravelSearchRequestDTO.class);
    }

    @Override
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

    public SimpleTravelSearchRequest mapFrom2(SimpleTravelSearchRequestDTO req0, Route route) {
        SimpleTravelSearchRequest reqBack = modelMapper.map(req0, SimpleTravelSearchRequest.class);
        reqBack.setServiceClass(ServiceClass.valueOf(req0.getServiceClass()));
        reqBack.setMaxPrice(req0.getMaxPrice() - route.getTotalPrice());
        reqBack.setAvailableAirlines(
                req0.getAvailableAirlines().stream()
                        .map(airlineService::findByName)
                        .collect(Collectors.toList())
        );
        reqBack.setDepartureCity(cityService.findByName(req0.getArrivalCity()));
        reqBack.setArrivalCity(cityService.findByName(req0.getDepartureCity()));
        reqBack.setDepartureDateStart(req0.getBackDepartureDateStart());
        reqBack.setDepartureDateFinish(req0.getBackDepartureDateFinish());
        reqBack.setDepartureTimeStart(req0.getBackDepartureTimeStart());
        reqBack.setDepartureTimeFinish(req0.getBackDepartureTimeFinish());
        reqBack.setArrivalDateStart(req0.getBackArrivalDateStart());
        reqBack.setArrivalDateFinish(req0.getBackArrivalDateFinish());
        reqBack.setArrivalTimeStart(req0.getBackArrivalTimeStart());
        reqBack.setArrivalTimeFinish(req0.getBackArrivalTimeFinish());

        return reqBack;
    }
}