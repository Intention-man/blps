package com.example.prac.service;

import com.example.prac.data.DTO.complex.req.ComplexRouteSearchRequest;
import com.example.prac.data.DTO.simple.req.SimpleRouteSearchRequestDTO;
import com.example.prac.data.DTO.simple.res.RouteVariantDTO;
import com.example.prac.data.DTO.TicketDTO;
import com.example.prac.data.DTO.simple.res.TicketSearchResponse;
import com.example.prac.data.model.Ticket;
import com.example.prac.mappers.TicketMapper;
import com.example.prac.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class TicketSearchService {
    private TicketService ticketService;
    private TicketMapper ticketMapper;
    private TicketRepository ticketRepository;

    //NOTE пока думаю сделать поиск в ширину. По сути все города (узлы) и перелеты между ними (ребра) можно представить как граф.
    // Сначала ищем билеты-кандидаты на первый полет в маршруте. Смотрим сколько есть таких,
    // которые сразу же нас ведут к цели при соблюдении всех условий (цена, класс, даты, время).
    // Если нашли N штук - заканчиваем поиск. Если нет, то ищем для каждого первого в маршруте билета второй билет в маршруте и т д.
    // Ясно что надо выбрать относительно небольшое N, сделать мало возможных городов
    // и наверное сделать лимит на поиск в ширину (ex: не более 4 билетов в перелете между A и B)

    public TicketSearchResponse searchSimpleRoutes(SimpleRouteSearchRequestDTO simpleRouteSearchRequestDTO) {
        List<RouteVariantDTO> routeOptions = findRouteVariants(simpleRouteSearchRequestDTO);
        TicketSearchResponse ticketSearchResponse = new TicketSearchResponse();
        ticketSearchResponse.setRouteOptions(routeOptions);
        return ticketSearchResponse;
    }

    private List<RouteVariantDTO> findRouteVariants(SimpleRouteSearchRequestDTO req) {
        for (int i = 0; i < 3; i++) {
            RouteVariantDTO routeOption = new RouteVariantDTO();
            ticketRepository.findTicketsByDepartureData(
                    req.getServiceClass(), req.getDepartureCity(), req.getDepartureDateStart(),
                    req.getDepartureDateFinish(), req.getDepartureTimeStart(), req.getDepartureTimeFinish())


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