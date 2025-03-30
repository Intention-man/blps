package com.example.prac.data.req;

import lombok.Data;

import java.util.List;

@Data
public class ComplexTravelSearchRequestDTO {
    private Integer passengerCount;
    private String serviceClass;
    private int maxPrice;
    private int maxTravelTime;
    private int numberOfTransfers;
    private List<String> availableAirlines;
    private List<ComplexRouteLegDTO> complexRouteLegs;
}
