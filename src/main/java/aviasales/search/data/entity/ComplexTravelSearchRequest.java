package aviasales.search.data.entity;

import aviasales.data.airline.Airline;
import aviasales.data.ticket.ServiceClass;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplexTravelSearchRequest {
    private Integer passengerCount;
    private ServiceClass serviceClass;
    private int maxPrice;
    private int maxTravelTime;
    private int numberOfTransfers;
    private List<Airline> availableAirlines;
    private List<ComplexRouteLeg> complexRouteLegs;
}
