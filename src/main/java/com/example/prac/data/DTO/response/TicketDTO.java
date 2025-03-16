package com.example.prac.data.DTO.response;

import lombok.Data;

@Data
public class TicketDTO {
    private Long id;
    private String airline;
    private String serviceClass;
    private String flightNumber;
    private String departureCity;
    private String departureDatetime;
    private String arrivalCity;
    private String arrivalDatetime;
    private int price;
}