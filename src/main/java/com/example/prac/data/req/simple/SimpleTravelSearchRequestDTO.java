package com.example.prac.data.req.simple;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SimpleTravelSearchRequestDTO {
    private Integer passengerCount;
    private String serviceClass;
    private int maxPrice;
    private int maxTravelTime;
    private int numberOfTransfers;
    private List<String> availableAirlines;
    private String departureCity;
    private String arrivalCity;

    private LocalDate departureDateStart;
    private LocalDate departureDateFinish;
    private LocalTime departureTimeStart;
    private LocalTime departureTimeFinish;
    private LocalDate arrivalDateStart;
    private LocalDate arrivalDateFinish;
    private LocalTime arrivalTimeStart;
    private LocalTime arrivalTimeFinish;
    private LocalDate backDepartureDateStart;
    private LocalDate backDepartureDateFinish;
    private LocalTime backDepartureTimeStart;
    private LocalTime backDepartureTimeFinish;
    private LocalDate backArrivalDateStart;
    private LocalDate backArrivalDateFinish;
    private LocalTime backArrivalTimeStart;
    private LocalTime backArrivalTimeFinish;
}
