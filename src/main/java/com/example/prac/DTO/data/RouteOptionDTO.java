package com.example.prac.DTO.data;

import lombok.Data;

import java.util.List;

@Data
public class RouteOptionDTO {
    private List<TicketDTO> tickets;
    private Double totalPrice;
}