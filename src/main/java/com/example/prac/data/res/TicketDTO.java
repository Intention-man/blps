package com.example.prac.data.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TicketDTO implements Serializable {
    private String airline;
    private String serviceClass;
    private int price;
    private int availableSeats;
    private String flightNumber;
    private String departureCity;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private String arrivalCity;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private double hours;
}