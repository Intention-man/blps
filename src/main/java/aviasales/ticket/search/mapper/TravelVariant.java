package aviasales.ticket.search.mapper;

import lombok.*;
import aviasales.ticket.search.data.entity.Route;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelVariant {
    private int totalPrice;
    private List<Route> routes;
}
