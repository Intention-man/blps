package com.example.prac.service;

import com.example.prac.data.model.Route;
import com.example.prac.data.model.Ticket;
import com.example.prac.data.req.simple.SimpleTravelSearchRequestDTO;
import com.example.prac.data.res.TravelVariantDTO;
import com.example.prac.data.res.TicketSearchResponse;
import com.example.prac.data.model.SimpleTravelSearchRequest;
import com.example.prac.mappers.SimpleTravelSearchRequestMapper;
import com.example.prac.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketSearchService {
    private TicketService ticketService;
    private SimpleTravelSearchRequestMapper simpleTravelSearchRequestMapper;
    private TicketRepository ticketRepository;

    //NOTE пока думаю сделать поиск в ширину. По сути все города (узлы) и перелеты между ними (ребра) можно представить как граф.
    // Сначала ищем билеты-кандидаты на первый полет в маршруте. Смотрим сколько есть таких,
    // которые сразу же нас ведут к цели при соблюдении всех условий (цена, класс, даты, время).
    // Если нашли N штук - заканчиваем поиск. Если нет, то ищем для каждого первого в маршруте билета второй билет в маршруте и т д.
    // Ясно что надо выбрать относительно небольшое N, сделать мало возможных городов
    // и наверное сделать лимит на поиск в ширину (ex: не более 4 билетов в перелете между A и B)

    public TicketSearchResponse searchSimpleRoutes(SimpleTravelSearchRequestDTO simpleTravelSearchRequestDTO) {
        SimpleTravelSearchRequest req = simpleTravelSearchRequestMapper.mapFrom(simpleTravelSearchRequestDTO);
        List<TravelVariantDTO> travelVariants = new ArrayList<>();
        // get res
        TicketSearchResponse ticketSearchResponse = new TicketSearchResponse();
        ticketSearchResponse.setRouteOptions(travelVariants);
        return ticketSearchResponse;
    }

    // TODO надо найти такие последовательности билетов, которые доставляют нас из A -> B, удовлетворяя всем условиям
    private List<Route> findRouteVariants(SimpleTravelSearchRequest req) {
        List<Route> res = new ArrayList<>();

        List<Ticket> firstTicketCandidates = ticketRepository.findTicketsByDepartureData(
                req.getServiceClass(), req.getPassengerCount(), req.getMaxPrice(), req.getAvailableAirlines(),
                req.getDepartureCity(), req.getDepartureDateStart(), req.getDepartureDateFinish(),
                req.getDepartureTimeStart(), req.getDepartureTimeFinish());

        for (Ticket ticket : firstTicketCandidates) {
            Route route = initRouteWithFirstTicket(ticket, req);

        }

        return res;
    }

    private void nextStep(SimpleTravelSearchRequest req, Route route) {

    }

    private Route initRouteWithFirstTicket(Ticket ticket, SimpleTravelSearchRequest req) {
        Route route = new Route();
        route.setDepartureCity(req.getDepartureCity());
        route.setArrivalCity(req.getArrivalCity());
        route.setTotalHours(ticketService.calculateTravelTimeInHours(ticket));
        List<Ticket> list = new ArrayList<>();
        list.add(ticket);
        route.setTickets(list);
        return route;
    }
}