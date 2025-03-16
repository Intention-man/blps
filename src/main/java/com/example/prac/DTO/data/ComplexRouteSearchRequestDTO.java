package com.example.prac.DTO.data;

import lombok.Data;

import java.util.List;

@Data
public class ComplexRouteSearchRequestDTO {
    private String serviceClass;
    private Integer passengerCount;
    private List<FlightLegDTO> flightLegs;
}
