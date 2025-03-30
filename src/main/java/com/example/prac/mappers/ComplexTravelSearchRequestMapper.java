package com.example.prac.mappers;

import com.example.prac.data.model.*;
import com.example.prac.data.req.ComplexTravelSearchRequestDTO;
import com.example.prac.service.AirlineService;
import com.example.prac.service.CityService;
import com.example.prac.service.TicketService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ComplexTravelSearchRequestMapper implements Mapper<ComplexTravelSearchRequest, ComplexTravelSearchRequestDTO> {
    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final AirlineService airlineService;
    private TicketService ticketService;

    public ComplexTravelSearchRequestDTO mapTo(ComplexTravelSearchRequest req) {
        if (req == null) {
            return null;
        }
        return modelMapper.map(req, ComplexTravelSearchRequestDTO.class);
    }

    public ComplexTravelSearchRequest mapFrom(ComplexTravelSearchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        List<ComplexRouteLeg> routes = dto.getComplexRouteLegs().stream()
                .map(routeDTO -> ComplexRouteLeg.builder()
                        .departureCity(cityService.findByName(routeDTO.getDepartureCity()))
                        .departureDate(routeDTO.getDepartureDate())
                        .departureTimeStart(routeDTO.getDepartureTimeStart())
                        .departureTimeFinish(routeDTO.getDepartureTimeFinish())
                        .arrivalCity(cityService.findByName(routeDTO.getArrivalCity()))
                        .arrivalTimeStart(routeDTO.getArrivalTimeStart())
                        .arrivalTimeFinish(routeDTO.getArrivalTimeFinish())
                        .build())
                .collect(Collectors.toList());

        return ComplexTravelSearchRequest.builder()
                .passengerCount(dto.getPassengerCount())
                .serviceClass(ServiceClass.valueOf(dto.getServiceClass()))
                .maxPrice(dto.getMaxPrice())
                .maxTravelTime(dto.getMaxTravelTime())
                .numberOfTransfers(dto.getNumberOfTransfers())
                .availableAirlines(dto.getAvailableAirlines().stream()
                        .map(airlineService::findByName)
                        .collect(Collectors.toList()))
                .complexRouteLegs(routes)
                .build();
    }


    public SimpleTravelSearchRequest mapToLeg(ComplexTravelSearchRequest req, int legIndex, TravelVariant variant) {
        ComplexRouteLeg leg = req.getComplexRouteLegs().get(legIndex);

        LocalTime departureTimeStart;
        if (legIndex == 0) {
            departureTimeStart = leg.getDepartureTimeStart();
        } else {
            List<Ticket> lastRouteLastTicket = variant.getRoutes().get(legIndex - 1).getTickets();
            departureTimeStart = ticketService.max(
                    lastRouteLastTicket.get(lastRouteLastTicket.size() - 1).getArrivalTime(),
                    leg.getDepartureTimeStart());
        }

        if (departureTimeStart.isAfter(leg.getDepartureTimeFinish()))
            return null;

        return SimpleTravelSearchRequest.builder()
                .passengerCount(req.getPassengerCount())
                .serviceClass(req.getServiceClass())
                .maxPrice(req.getMaxPrice() - variant.getTotalPrice())
                .maxTravelTime(req.getMaxTravelTime())
                .numberOfTransfers(req.getNumberOfTransfers())
                .availableAirlines(req.getAvailableAirlines())
                .departureCity(leg.getDepartureCity())
                .arrivalCity(leg.getArrivalCity())
                .departureDateStart(leg.getDepartureDate())
                .departureDateFinish(leg.getDepartureDate())
                .departureTimeStart(departureTimeStart)
                .departureTimeFinish(leg.getDepartureTimeFinish())
                .arrivalDateStart(leg.getDepartureDate())
                .arrivalDateFinish(leg.getDepartureDate().plusDays(1))
                .arrivalTimeStart(leg.getArrivalTimeStart())
                .arrivalTimeFinish(leg.getArrivalTimeFinish())
                .build();
    }
}
