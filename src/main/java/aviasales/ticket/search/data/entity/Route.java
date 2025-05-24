package aviasales.ticket.search.data.entity;

import aviasales.common.data.entity.City;
import aviasales.common.data.entity.Ticket;
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