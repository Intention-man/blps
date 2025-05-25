package aviasales.search.mapper;

import aviasales.search.data.entity.Route;
import lombok.*;

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
