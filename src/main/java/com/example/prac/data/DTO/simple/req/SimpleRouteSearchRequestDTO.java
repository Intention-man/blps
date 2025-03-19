package com.example.prac.data.DTO.simple.req;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SimpleRouteSearchRequestDTO {
    private Integer passengerCount;
    private String serviceClass;
    private int maxPrice;
    private int maxTravelHours;
    private int numberOfTransfers;
    private List<String> availableAirlines;
    private String departureCity;
    private LocalDate departureDateStart;
    private LocalDate departureDateFinish;
    private LocalTime departureTimeStart;
    private LocalTime departureTimeFinish;
    private String arrivalCity;
    private LocalDate arrivalDateStart;
    private LocalDate arrivalDateFinish;
    private LocalTime arrivalTimeStart;
    private LocalTime arrivalTimeFinish;
}
