package com.example.prac.data.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleTravelSearchRequest {
    private Long id;
    private Integer passengerCount;
    private ServiceClass serviceClass;
    private int maxPrice;
    private int maxTravelHours;
    @Max(value = 3)
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