package aviasales.search.mapper;

import aviasales.data.airline.AirlineService;
import aviasales.data.city.CityService;
import aviasales.data.ticket.ServiceClass;
import aviasales.data.ticket.Ticket;
import aviasales.search.data.dto.ComplexTravelSearchRequestDTO;
import aviasales.search.data.entity.ComplexRouteLeg;
import aviasales.search.data.entity.ComplexTravelSearchRequest;
import aviasales.search.data.entity.SimpleTravelSearchRequest;
import aviasales.search.service.DateTimeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ComplexTravelSearchRequestMapper {
    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final AirlineService airlineService;
    private DateTimeService dateTimeService;

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

        LocalDateTime minStartDatetime = LocalDateTime.of(leg.getDepartureDate(), leg.getDepartureTimeStart());
        if (legIndex > 0) {
            List<Ticket> lastRouteLastTicket = variant.getRoutes().get(legIndex - 1).getTickets();
            Ticket last = lastRouteLastTicket.get(lastRouteLastTicket.size() - 1);
            minStartDatetime = dateTimeService.max(minStartDatetime, last.getArrivalDateTime());

            LocalDateTime maxStartDatetime = LocalDateTime.of(leg.getDepartureDate(), leg.getDepartureTimeFinish());
            if (minStartDatetime.isAfter(maxStartDatetime))
                return null;
        }

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
                .departureTimeStart(leg.getDepartureTimeStart())
                .departureTimeFinish(leg.getDepartureTimeFinish())
                .arrivalDateStart(leg.getDepartureDate())
                .arrivalDateFinish(leg.getDepartureDate().plusDays(1))
                .arrivalTimeStart(leg.getArrivalTimeStart())
                .arrivalTimeFinish(leg.getArrivalTimeFinish())
                .minStartDatetime(minStartDatetime)
                .build();
    }
}
