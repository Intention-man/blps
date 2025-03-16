package com.example.prac.DTO.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Long id;
    private String airline;
    private String flightNumber;
    private String departureCity;
    private LocalDateTime departureTime;
    private String arrivalCity;
    private LocalDateTime arrivalTime;
    private Double price;
}