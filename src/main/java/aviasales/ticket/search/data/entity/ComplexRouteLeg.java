package aviasales.ticket.search.data.entity;

import aviasales.common.data.entity.City;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplexRouteLeg {
    private City departureCity;
    private LocalDate departureDate;
    private LocalTime departureTimeStart;
    private LocalTime departureTimeFinish;
    private City arrivalCity;
    private LocalTime arrivalTimeStart;
    private LocalTime arrivalTimeFinish;
}
