package com.example.prac.mappers;

import com.example.prac.data.model.Route;
import com.example.prac.data.res.RouteDTO;
import com.example.prac.service.CityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RouteMapper implements Mapper<Route, RouteDTO> {

    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final TicketMapper ticketMapper;

    @Override
    public RouteDTO mapTo(Route route) {
        if (route == null) {
            return null;
        }

        RouteDTO dto = modelMapper.map(route, RouteDTO.class);
        dto.setDepartureCity(route.getDepartureCity().getName());
        dto.setArrivalCity(route.getArrivalCity().getName());
        dto.setTickets(route.getTickets()
                .stream()
                .map(ticketMapper::mapTo)
                .collect(Collectors.toList()));
        return dto;
    }

    @Override
    public Route mapFrom(RouteDTO dto) {
        if (dto == null) {
            return null;
        }

        Route route = modelMapper.map(dto, Route.class);
        route.setDepartureCity(cityService.findOrCreateCity(dto.getDepartureCity()));
        route.setArrivalCity(cityService.findOrCreateCity(dto.getArrivalCity()));
        route.setTickets(dto.getTickets()
                .stream()
                .map(ticketMapper::mapFrom)
                .collect(Collectors.toList()));
        return route;
    }
}