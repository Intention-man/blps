package aviasales.search.data.entity;


import aviasales.data.airline.Airline;
import aviasales.data.city.City;
import aviasales.data.ticket.ServiceClass;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleTravelSearchRequest {
    private Long id;
    private ServiceClass serviceClass;
    private Integer passengerCount;
    private int maxPrice;
    private int maxTravelTime;
    @Min(value = 1)
    @Max(value = 3)
    private int numberOfTransfers;
    private List<Airline> availableAirlines;
    private City departureCity;
    private City arrivalCity;

    private LocalDate departureDateStart;
    private LocalDate departureDateFinish;
    private LocalTime departureTimeStart;
    private LocalTime departureTimeFinish;
    private LocalDate arrivalDateStart;
    private LocalDate arrivalDateFinish;
    private LocalTime arrivalTimeStart;
    private LocalTime arrivalTimeFinish;

    private LocalDateTime minStartDatetime;
}