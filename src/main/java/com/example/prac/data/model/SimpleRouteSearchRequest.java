package com.example.prac.data.model;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleRouteSearchRequest {
    private Long id;
    private Integer passengerCount;
    private ServiceClass serviceClass;
    private int maxPrice;
    private int maxTravelHours;
    private int numberOfTransfers;
    private List<Airline> availableAirlines;
    private City departureCity;
    private LocalDate departureDateStart;
    private LocalDate departureDateFinish;
    private LocalTime departureTimeStart;
    private LocalTime departureTimeFinish;
    private City arrivalCity;
    private LocalDate arrivalDateStart;
    private LocalDate arrivalDateFinish;
    private LocalTime arrivalTimeStart;
    private LocalTime arrivalTimeFinish;
}