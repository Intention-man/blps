package com.example.prac.service.data;

import com.example.prac.DTO.data.*;
import com.example.prac.mappers.TicketMapper;
import com.example.prac.model.dataEntity.Ticket;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TicketSearchService {
    private TicketService ticketService;
    private TicketMapper ticketMapper;

    public TicketSearchResponseDTO searchTickets(ComplexRouteSearchRequestDTO searchRequestDTO) {
        List<RouteOptionDTO> routeOptions = findRouteOptions(searchRequestDTO);
        TicketSearchResponseDTO ticketSearchResponseDTO = new TicketSearchResponseDTO();
        ticketSearchResponseDTO.setRouteOptions(routeOptions);
        return ticketSearchResponseDTO;
    }

    private List<RouteOptionDTO> findRouteOptions(ComplexRouteSearchRequestDTO searchRequestDTO) {
        List<RouteOptionDTO> routeOptions = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < random.nextInt(3) + 1; i++) {
            RouteOptionDTO routeOption = new RouteOptionDTO();
            List<TicketDTO> ticketsForRoute = new ArrayList<>();
            LocalDateTime lastArrivalTime = null;

            for (FlightLegDTO leg : searchRequestDTO.getFlightLegs()) {
                TicketDTO ticket = findTicket(leg, lastArrivalTime);
                if (ticket != null) {
                    ticketsForRoute.add(ticket);
                    lastArrivalTime = ticket.getArrivalTime();
                } else {
                    ticketsForRoute = new ArrayList<>();
                    break;
                }
            }
            if (!ticketsForRoute.isEmpty()) {
                routeOption.setTickets(ticketsForRoute);
                routeOptions.add(routeOption);
            }
        }
        return routeOptions;
    }

    private TicketDTO findTicket(FlightLegDTO leg, LocalDateTime lastArrivalTime) {
        List<Ticket> availableTickets;

        if (lastArrivalTime == null) {
            LocalDateTime startTime = leg.getDepartureDateStart().atStartOfDay();
            LocalDateTime endTime = leg.getDepartureDateEnd().atStartOfDay().plusDays(1);

            availableTickets = ticketService.findAvailableTickets(leg.getDepartureCity(), leg.getDestinationCity(), startTime, endTime);
        } else {
            availableTickets = ticketService.findAvailableTicketsAfter(leg.getDepartureCity(), leg.getDestinationCity(), lastArrivalTime);
        }
        if (availableTickets.isEmpty()) {
            return null;
        }
        Random random = new Random();
        Ticket ticket = availableTickets.get(random.nextInt(availableTickets.size()));
        return ticketMapper.mapTo(ticket);
    }
}