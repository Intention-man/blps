package com.example.prac.service;

import com.example.prac.data.model.ComplexTravelSearchRequest;
import com.example.prac.data.model.Route;
import com.example.prac.data.model.SimpleTravelSearchRequest;
import com.example.prac.data.model.Ticket;
import com.example.prac.data.req.ComplexTravelSearchRequestDTO;
import com.example.prac.data.req.SimpleTravelSearchRequestDTO;
import com.example.prac.data.res.SearchResponseDTO;
import com.example.prac.data.res.TravelVariantDTO;
import com.example.prac.mappers.*;
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
    private SimpleTravelSearchRequestMapper simpleReqMapper;
    private ComplexTravelSearchRequestMapper complexReqMapper;
    private TravelVariantMapper travelVariantMapper;
    private TicketRepository ticketRepository;
    private TicketService ticketService;
    private RouteMapper routeMapper;

    public SearchResponseDTO searchComplexRoutes(ComplexTravelSearchRequestDTO reqDto, int page, int limit) {
        int maxResCount = (page + 1) * limit;

        ComplexTravelSearchRequest complexReq = complexReqMapper.mapFrom(reqDto);
        int legsCount = complexReq.getComplexRouteLegs().size();

        List<TravelVariant> variants = new ArrayList<>();
        List<TravelVariant> newVariants = new ArrayList<>();

        SimpleTravelSearchRequest simpleReq0 = complexReqMapper.mapToLeg(complexReq, 0, new TravelVariant(0, new ArrayList<>()));
        List<Route> simpleRoutes0 = new ArrayList<>();
        findAndSetSimpleRouteVariants(simpleReq0, simpleRoutes0);

        for (Route route : simpleRoutes0) {
            TravelVariant variant = initVariantWithFirstRoot(route);
            variants.add(variant);
        }

        for (int legIndex = 1; legIndex < legsCount; legIndex++) {
            for (TravelVariant variant : variants) {
                SimpleTravelSearchRequest simpleReq = complexReqMapper.mapToLeg(complexReq, legIndex, variant);
                if (simpleReq == null) continue;

                List<Route> simpleRoutes = new ArrayList<>();
                findAndSetSimpleRouteVariants(simpleReq, simpleRoutes);
                for (Route route : simpleRoutes) {
                    TravelVariant newVariant = cloneTravelVariantAddingRoute(variant, route);
                    newVariants.add(newVariant);
                    if (legIndex == legsCount - 1 && newVariants.size() == maxResCount)
                        return initResponse(newVariants, page, limit);
                }
            }

            variants = newVariants;
            newVariants = new ArrayList<>();
        }

        return initResponse(variants, page, limit);
    }

    public SearchResponseDTO searchSimpleRoutes(SimpleTravelSearchRequestDTO simpleReqDTO, boolean needBackTickets, int page, int limit) {
        List<TravelVariant> variants = new ArrayList<>();
        SimpleTravelSearchRequest req = simpleReqMapper.mapFrom(simpleReqDTO);
        List<Route> routes = new ArrayList<>();
        findAndSetSimpleRouteVariants(req, routes);
        if (needBackTickets) {
            for (Route route : routes) {
                SimpleTravelSearchRequest reqBack = simpleReqMapper.mapFrom2(simpleReqDTO, route);
                List<Route> routesBack = new ArrayList<>();
                findAndSetSimpleRouteVariants(reqBack, routesBack);
                for (Route routeBack : routesBack) {
                    variants.add(new TravelVariant(
                            route.getTotalPrice() + routeBack.getTotalPrice(),
                            List.of(route, routeBack)
                    ));
                }
            }
        } else {
            variants = routes.stream()
                    .map(route -> new TravelVariant(route.getTotalPrice(), List.of(route)))
                    .toList();
        }

        return initResponse(variants, page, limit);
    }

    private void findAndSetSimpleRouteVariants(SimpleTravelSearchRequest req, List<Route> simpleRouteVariants) {
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
                findNextTicketCandidates(req, route, req.getNumberOfTransfers() - 1, simpleRouteVariants);
            }
        }
    }

    private void findNextTicketCandidates(SimpleTravelSearchRequest req, Route route, int leftNumberOfTransfers,
                                          List<Route> simpleRouteVariants) {
        if (leftNumberOfTransfers == 0)
            return;

        Ticket lastTicket = route.getTickets().get(route.getTickets().size() - 1);

        if (leftNumberOfTransfers == 1) {
            LocalDate departureDateStart = ticketService.max(lastTicket.getArrivalDate(), req.getArrivalDateStart());

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
                    lastTicket.getArrivalDateTime(),
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
                    findNextTicketCandidates(req, updatedRoute, leftNumberOfTransfers - 1, simpleRouteVariants);
                }
            }
        }
    }

    private boolean isSuitableFinishTicket(Ticket ticket, SimpleTravelSearchRequest req) {
        return ticket.getArrivalCity().equals(req.getArrivalCity()) &&
                ticket.getArrivalTime().isAfter(req.getArrivalTimeStart()) &&
                ticket.getArrivalTime().isBefore(req.getArrivalTimeFinish()) &&
                !ticket.getArrivalDate().isBefore(req.getArrivalDateStart()) &&
                !ticket.getArrivalDate().isAfter(req.getArrivalDateFinish());
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

        double additionalTransferDuration = calcTransferDurationInHours(route.getTickets().get(route.getTickets().size() - 1), ticket);
        updatedRoute.setTotalHours(route.getTotalHours() + additionalTransferDuration + ticket.getHours());

        updatedRoute.setTotalPrice(route.getTotalPrice() + ticket.getPrice());
        updatedRoute.setMaxFinishDatetime(route.getMaxFinishDatetime());

        //NOTE учитывая что Ticket - (по логике программы) неизменяемый объект, думаю, что можно просто копировать ссылки, а не делать глубокое копирование
        List<Ticket> list = new ArrayList<>(route.getTickets());
        list.add(ticket);
        updatedRoute.setTickets(list);
        return updatedRoute;
    }

    private TravelVariant initVariantWithFirstRoot(Route route) {
        return new TravelVariant(route.getTotalPrice(), new ArrayList<>(List.of(route)));
    }

    private SearchResponseDTO initResponse(List<TravelVariant> variants, int page, int limit) {
        List<TravelVariantDTO> list = variants.stream().map(travelVariantMapper::mapTo).toList();
        int toIndex = Math.max(Math.min((page + 1) * limit, list.size()) - 1, 0);
        int fromIndex = Math.max(0, toIndex + 1 - limit);

        return new SearchResponseDTO(
                variants.size(),
                list.subList(fromIndex, toIndex),
                fromIndex,
                toIndex
        );
    }

    private TravelVariant cloneTravelVariantAddingRoute(TravelVariant variant, Route route) {
        TravelVariant newVariant = new TravelVariant();

        List<Route> routes = new ArrayList<>(variant.getRoutes());
        routes.add(route);
        newVariant.setRoutes(routes);

        newVariant.setTotalPrice(variant.getTotalPrice() + route.getTotalPrice());
        return newVariant;
    }

    private LocalDateTime calcMaxFinishDatetime(Ticket ticket, SimpleTravelSearchRequest req) {
        return ticket.getDepartureDateTime().plusHours(req.getMaxTravelTime());
    }

    public double calcTransferDurationInHours(Ticket ticket1, Ticket ticket2) {
        return java.time.Duration.between(ticket1.getArrivalDateTime(), ticket2.getDepartureDateTime()).toMinutes() / 60.0;
    }
}