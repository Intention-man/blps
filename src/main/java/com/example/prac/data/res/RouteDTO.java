package com.example.prac.data.res;

import lombok.Data;

import java.util.List;

@Data
public class RouteDTO {
    private String departureCity;
    private String arrivalCity;
    private double totalHours;
    private int totalPrice;
    private List<TicketDTO> tickets;
}
