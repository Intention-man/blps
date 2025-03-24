package com.example.prac.data.res;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TicketDTO {
    private Long id;
    private String airline;
    private String serviceClass;
    private int price;
    private int availableSeats;
    private String flightNumber;
    private String departureCity;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDateTime departureDateTime; // Новое поле
    private String arrivalCity;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private LocalDateTime arrivalDateTime; // Новое поле
}
