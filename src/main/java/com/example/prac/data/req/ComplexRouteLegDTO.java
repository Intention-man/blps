package com.example.prac.data.req;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ComplexRouteLegDTO {
    private String departureCity;
    private LocalDate departureDate;
    private LocalTime departureTimeStart;
    private LocalTime departureTimeFinish;
    private String arrivalCity;
    private LocalTime arrivalTimeStart;
    private LocalTime arrivalTimeFinish;
}