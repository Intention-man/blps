package com.example.prac.service;

import com.example.prac.data.model.Route;
import com.example.prac.data.model.SimpleTravelSearchRequest;
import com.example.prac.data.model.Ticket;
import com.example.prac.data.req.simple.SimpleTravelSearchRequestDTO;
import com.example.prac.data.res.RouteDTO;
import com.example.prac.mappers.RouteMapper;
import com.example.prac.mappers.SimpleTravelSearchRequestMapper;
import com.example.prac.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketSearchService {
    private TicketService ticketService;
    private SimpleTravelSearchRequestMapper simpleTravelSearchRequestMapper;
    private TicketRepository ticketRepository;
    private List<Route> simpleRouteVariants;
    private RouteMapper routeMapper;

    //NOTE пока думаю сделать поиск в ширину. По сути все города (узлы) и перелеты между ними (ребра) можно представить как граф.
    // Сначала ищем билеты-кандидаты на первый полет в маршруте. Смотрим сколько есть таких,
    // которые сразу же нас ведут к цели при соблюдении всех условий (цена, класс, даты, время).
    // Если нашли N штук - заканчиваем поиск. Если нет, то ищем для каждого первого в маршруте билета второй билет в маршруте и т д.
    // Ясно что надо выбрать относительно небольшое N, сделать мало возможных городов
    // и наверное сделать лимит на поиск в ширину (ex: не более 4 билетов в перелете между A и B)

//    public TicketSearchResponse searchSimpleRoutes(SimpleTravelSearchRequestDTO simpleTravelSearchRequestDTO) {
//        SimpleTravelSearchRequest req = simpleTravelSearchRequestMapper.mapFrom(simpleTravelSearchRequestDTO);
//        List<TravelVariantDTO> travelVariants = new ArrayList<>();
//        // get res
//        TicketSearchResponse ticketSearchResponse = new TicketSearchResponse();
//        ticketSearchResponse.setRouteOptions(travelVariants);
//        return ticketSearchResponse;
//    }

    public List<RouteDTO> searchSimpleRoutes(SimpleTravelSearchRequestDTO simpleTravelSearchRequestDTO) {
        SimpleTravelSearchRequest req = simpleTravelSearchRequestMapper.mapFrom(simpleTravelSearchRequestDTO);
        findAndSetSimpleRouteVariants(req);
        simpleRouteVariants = simpleRouteVariants.stream().filter(route -> route.getTickets().size() > 2).toList();
        return simpleRouteVariants.stream().map(routeMapper::mapTo).toList();
    }

    private void findAndSetSimpleRouteVariants(SimpleTravelSearchRequest req) {
        simpleRouteVariants = new ArrayList<>();
        List<Ticket> firstTicketCandidates = ticketRepository.findFirstTickets(
                req.getServiceClass(), req.getPassengerCount(), req.getMaxPrice(), req.getMaxTravelTime(), req.getAvailableAirlines(),
                req.getDepartureCity(), req.getDepartureDateStart(), req.getDepartureDateFinish(),
                req.getDepartureTimeStart(), req.getDepartureTimeFinish());

        for (Ticket ticket : firstTicketCandidates) {
            if (ticket.getArrivalCity().equals(req.getArrivalCity())) {
                if (isSuitableFinishTicket(ticket, req)) {
                    Route route = initRouteWithFirstTicket(ticket, req);
                    simpleRouteVariants.add(route);
                }
            } else if (canTicketBeIncludeInRoute(ticket, req)) {
                Route route = initRouteWithFirstTicket(ticket, req);
                nextStep(req, route, req.getNumberOfTransfers() - 1);
            }
        }
    }

    private void nextStep(SimpleTravelSearchRequest req, Route route, int leftNumberOfTransfers) {
        if (leftNumberOfTransfers == 0)
            return;

        Ticket lastTicket = route.getTickets().get(route.getTickets().size() - 1);

        if (leftNumberOfTransfers == 1) {
            LocalDate departureDateStart = lastTicket.getArrivalDate().isAfter(req.getArrivalDateStart()) ?
                    lastTicket.getArrivalDate() :
                    req.getArrivalDateStart();

            List<Ticket> nextTicketCandidates = ticketRepository.findFinishTickets(
                    req.getServiceClass(),
                    req.getPassengerCount(),
                    req.getMaxPrice() - route.getTotalPrice(),
                    req.getMaxTravelTime() - route.getTotalHours(),
                    req.getAvailableAirlines(),
                    lastTicket.getArrivalCity(),
                    departureDateStart,
                    req.getDepartureDateFinish(),
                    LocalTime.MIN,
                    LocalTime.of(23, 59, 59),
                    req.getArrivalCity(),
                    req.getArrivalDateStart(),
                    req.getArrivalDateFinish(),
                    req.getArrivalTimeStart(),
                    req.getArrivalTimeFinish(),
                    route.getMaxFinishDatetime()
            );

            for (Ticket ticket : nextTicketCandidates) {
                Route updatedRoute = cloneRouteAddingTicket(route, ticket);
                simpleRouteVariants.add(updatedRoute);
            }
        } else {
            List<Ticket> nextTicketCandidates = ticketRepository.findIntermediateTickets(
                    req.getServiceClass(),
                    req.getPassengerCount(),
                    req.getMaxPrice() - route.getTotalPrice(),
                    req.getMaxTravelTime() - route.getTotalHours(),
                    req.getAvailableAirlines(),
                    lastTicket.getArrivalCity(),
                    lastTicket.getArrivalDateTime(),
                    route.getMaxFinishDatetime());

            for (Ticket ticket : nextTicketCandidates) {
                if (ticket.getArrivalCity().equals(req.getArrivalCity())) {
                    if (isSuitableFinishTicket(ticket, req)) {
                        Route updatedRoute = cloneRouteAddingTicket(route, ticket);
                        simpleRouteVariants.add(updatedRoute);
                    }
                } else if (canTicketBeIncludeInRoute(ticket, req)) {
                    Route updatedRoute = cloneRouteAddingTicket(route, ticket);
                    nextStep(req, updatedRoute, leftNumberOfTransfers - 1);
                }
            }
        }
    }

    private boolean isSuitableFinishTicket(Ticket ticket, SimpleTravelSearchRequest req) {
        return ticket.getArrivalCity().equals(req.getArrivalCity()) &&
                ticket.getArrivalTime().isAfter(req.getArrivalTimeStart()) &&
                ticket.getArrivalTime().isBefore(req.getArrivalTimeFinish()) &&
                ticket.getArrivalDate().isAfter(req.getArrivalDateStart()) &&
                ticket.getArrivalDate().isBefore(req.getArrivalDateFinish());
    }

    private boolean canTicketBeIncludeInRoute(Ticket ticket, SimpleTravelSearchRequest req) {
        return ticket.getArrivalTime().isBefore(req.getArrivalTimeFinish()) &&
                ticket.getArrivalDate().isBefore(req.getArrivalDateFinish());
    }

    private Route initRouteWithFirstTicket(Ticket ticket, SimpleTravelSearchRequest req) {
        Route route = new Route();
        route.setDepartureCity(req.getDepartureCity());
        route.setArrivalCity(req.getArrivalCity());
        route.setTotalHours(ticket.getHours());
        route.setTotalPrice(ticket.getPrice());
        route.setMaxFinishDatetime(calcMaxFinishDatetime(ticket, req));
        List<Ticket> list = new ArrayList<>();
        list.add(ticket);
        route.setTickets(list);
        return route;
    }

    private Route cloneRouteAddingTicket(Route route, Ticket ticket) {
        Route updatedRoute = new Route();
        updatedRoute.setDepartureCity(route.getDepartureCity());
        updatedRoute.setArrivalCity(ticket.getArrivalCity());

        double additionalTransferDuration = ticketService.calcTransferDurationInHours(route.getTickets().get(route.getTickets().size() - 1), ticket);
        updatedRoute.setTotalHours(route.getTotalHours() + additionalTransferDuration);

        updatedRoute.setTotalPrice(route.getTotalPrice() + ticket.getPrice());
        updatedRoute.setMaxFinishDatetime(route.getMaxFinishDatetime());

        //NOTE учитывая что Ticket - (по логике программы) неизменяемый объект, думаю, что можно просто копировать ссылки, а не делать глубокое копирование
        List<Ticket> list = new ArrayList<>(route.getTickets());
        list.add(ticket);
        updatedRoute.setTickets(list);
        return updatedRoute;
    }

    private LocalDateTime calcMaxFinishDatetime(Ticket ticket, SimpleTravelSearchRequest req) {
        return ticket.getDepartureDateTime().plusHours(req.getMaxTravelTime());
    }
}