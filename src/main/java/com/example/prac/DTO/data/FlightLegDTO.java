package com.example.prac.DTO.data;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlightLegDTO {
    private String departureCity;
    private String destinationCity;
    private LocalDate departureDateStart;
    private LocalDate departureDateEnd;
}