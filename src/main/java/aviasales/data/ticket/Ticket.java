package aviasales.data.ticket;

import aviasales.data.airline.Airline;
import aviasales.data.city.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "aviasales_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    public static final int MAX_FLIGHT_DURATION = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(name = "service_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceClass serviceClass;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "departure_city_id", nullable = false)
    private City departureCity;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "departure_date_time", nullable = false)
    private LocalDateTime departureDateTime;

    @ManyToOne
    @JoinColumn(name = "arrival_city_id", nullable = false)
    private City arrivalCity;

    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "arrival_date_time", nullable = false)
    private LocalDateTime arrivalDateTime;

    @Column(name = "hours", nullable = false)
    @Max(MAX_FLIGHT_DURATION)
    private double hours;
}