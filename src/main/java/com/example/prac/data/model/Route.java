package com.example.prac.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
    private Long id;
    private City departureCity;
    private City arrivalCity;
    private double totalHours;
    private int totalPrice;
    private List<Ticket> tickets;
}