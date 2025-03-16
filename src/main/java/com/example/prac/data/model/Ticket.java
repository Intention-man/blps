package com.example.prac.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "aviasales_tickets") // Updated table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(name = "service_class", nullable = false)
    private ServiceClass serviceClass;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City departureCity;

    @Column(name = "departure_datetime", nullable = false)
    private LocalDateTime departureDatetime;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City arrivalCity;

    @Column(name = "arrival_datetime", nullable = false)
    private LocalDateTime arrivalDatetime;

    @Column(name = "price", nullable = false)
    private Integer price;
}