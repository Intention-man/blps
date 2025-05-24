package aviasales.ticket.search.data.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SimpleTravelSearchRequestDTO implements Serializable {
    @NotNull
    @Min(1)
    private Integer passengerCount;

    @NotBlank
    private String serviceClass;

    @Min(0)
    private int maxPrice;

    @Min(1)
    private int maxTravelTime;

    @Min(1)
    @Max(3)
    private int numberOfTransfers;

    @NotEmpty
    private List<@NotBlank String> availableAirlines;

    @NotBlank
    private String departureCity;

    @NotBlank
    private String arrivalCity;

    @NotNull
    private LocalDate departureDateStart;

    @NotNull
    private LocalDate departureDateFinish;

    @NotNull
    private LocalTime departureTimeStart;

    @NotNull
    private LocalTime departureTimeFinish;

    @NotNull
    private LocalDate arrivalDateStart;

    @NotNull
    private LocalDate arrivalDateFinish;

    @NotNull
    private LocalTime arrivalTimeStart;

    @NotNull
    private LocalTime arrivalTimeFinish;

    private LocalDate backDepartureDateStart;
    private LocalDate backDepartureDateFinish;
    private LocalTime backDepartureTimeStart;
    private LocalTime backDepartureTimeFinish;
    private LocalDate backArrivalDateStart;
    private LocalDate backArrivalDateFinish;
    private LocalTime backArrivalTimeStart;
    private LocalTime backArrivalTimeFinish;
}