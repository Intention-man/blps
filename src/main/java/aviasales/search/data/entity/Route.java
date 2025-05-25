package aviasales.search.data.entity;

import aviasales.data.city.City;
import aviasales.data.ticket.Ticket;
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