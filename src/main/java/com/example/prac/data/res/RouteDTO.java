package com.example.prac.data.res;

import lombok.Data;

import java.util.List;

@Data
public class RouteDTO {
    private String departureCity;
    private String arrivalCity;
    private int totalHours;
    private List<TicketDTO> tickets;
}
