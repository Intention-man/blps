package com.example.prac.data.req.complex;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ComplexRouteLeg {
    private String departureCity;
    private LocalDate departureDate;
    private LocalTime departureTimeStart;
    private LocalTime departureTimeFinish;
    private String arrivalCity;
    private LocalDate arrivalDate;
    private LocalTime arrivalTimeStart;
    private LocalTime arrivalTimeFinish;
}