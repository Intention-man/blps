package com.example.prac.data.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
    private City departureCity;
    private City arrivalCity;
    private double totalHours;
    private int totalPrice;
    private LocalDateTime maxFinishDatetime;
    private List<Ticket> tickets;
}