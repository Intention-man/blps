package com.example.prac.service;

import com.example.prac.data.DTO.request.ComplexRouteSearchRequest;
import com.example.prac.data.DTO.request.SimpleRouteSearchRequest;
import com.example.prac.data.DTO.response.RouteVariantDTO;
import com.example.prac.data.DTO.response.TicketDTO;
import com.example.prac.data.DTO.response.TicketSearchResponse;
import com.example.prac.data.model.Ticket;
import com.example.prac.mappers.TicketMapper;
import com.example.prac.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TicketSearchService {
    private TicketService ticketService;
    private TicketMapper ticketMapper;
    private TicketRepository ticketRepository;

    public TicketSearchResponse searchSimpleRoutes(SimpleRouteSearchRequest simpleRouteSearchRequest) {
        List<RouteVariantDTO> routeOptions = findRouteVariants(simpleRouteSearchRequest);
        TicketSearchResponse ticketSearchResponse = new TicketSearchResponse();
        ticketSearchResponse.setRouteOptions(routeOptions);
        return ticketSearchResponse;
    }

    private List<RouteVariantDTO> findRouteVariants(SimpleRouteSearchRequest simpleRouteSearchRequest) {
        for (int i = 0; i < 3; i++) {
            RouteVariantDTO routeOption = new RouteVariantDTO();


        }
    }



    public TicketSearchResponse searchComplexRoutes(ComplexRouteSearchRequest complexRouteSearchRequest) {
        List<RouteVariantDTO> routeOptions = findRouteVariants(complexRouteSearchRequest);
        TicketSearchResponse ticketSearchResponse = new TicketSearchResponse();
        ticketSearchResponse.setRouteOptions(routeOptions);
        return ticketSearchResponse;
    }


//    private List<RouteVariantDTO> findRouteVariants(SimpleRouteSearchRequest simpleRouteSearchRequest) {
//        List<RouteVariantDTO> routeOptions = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            RouteVariantDTO routeOption = new RouteVariantDTO();
//            List<TicketDTO> tickets = new ArrayList<>();
//            LocalDateTime lastArrivalTime = null;
//
//            for (FlexibleFlightLegDTO leg : searchRequestDTO.getFlightLegs()) {
//                TicketDTO ticket = findTicket(leg, lastArrivalTime);
//                if (ticket != null) {
//                    tickets.add(ticket);
//                    lastArrivalTime = ticket.getArrivalTimeFinish();
//                } else {
//                    tickets = new ArrayList<>();
//                    break;
//                }
//            }
//            if (!tickets.isEmpty()) {
//                routeOption.setTickets(tickets);
//                routeOptions.add(routeOption);
//            }
//        }
//        return routeOptions;
//    }

    private TicketDTO findTicket(FlexibleFlightLegDTO leg, LocalDateTime lastArrivalTime) {
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