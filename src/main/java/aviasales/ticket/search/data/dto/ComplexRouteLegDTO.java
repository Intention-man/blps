package aviasales.ticket.search.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ComplexRouteLegDTO implements Serializable {
    @NotBlank
    private String departureCity;

    @NotNull
    private LocalDate departureDate;

    @NotNull
    private LocalTime departureTimeStart;

    @NotNull
    private LocalTime departureTimeFinish;

    @NotBlank
    private String arrivalCity;

    @NotNull
    private LocalTime arrivalTimeStart;

    @NotNull
    private LocalTime arrivalTimeFinish;
}