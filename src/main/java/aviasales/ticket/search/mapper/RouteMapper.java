package aviasales.ticket.search.mapper;

import aviasales.common.mappers.Mapper;
import aviasales.common.mappers.TicketMapper;
import aviasales.common.service.CityService;
import aviasales.ticket.search.data.dto.RouteDTO;
import aviasales.ticket.search.data.entity.Route;
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
        route.setDepartureCity(cityService.findByName(dto.getDepartureCity()));
        route.setArrivalCity(cityService.findByName(dto.getArrivalCity()));
        route.setTickets(dto.getTickets()
                .stream()
                .map(ticketMapper::mapFrom)
                .collect(Collectors.toList()));
        return route;
    }
}