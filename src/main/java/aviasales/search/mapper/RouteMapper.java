package aviasales.search.mapper;

import aviasales.data.city.CityService;
import aviasales.data.ticket.TicketMapper;
import aviasales.search.data.dto.RouteDTO;
import aviasales.search.data.entity.Route;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RouteMapper {

    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final TicketMapper ticketMapper;

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