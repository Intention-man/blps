package com.example.prac.data.DTO.complex.req;

import lombok.Data;

import java.util.List;

@Data
public class ComplexRouteSearchRequest {
    private Integer passengerCount;
    private String serviceClass;
    private int maxPrice;
    private int maxTravelHours;
    private int numberOfTransfers;
    private List<String> availableAirlines;
    private List<ComplexRouteLeg> flightLegs;
}
