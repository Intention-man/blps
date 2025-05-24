package aviasales.ticket.search.data.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ComplexTravelSearchRequestDTO implements Serializable {
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

    @NotEmpty
    private List<@Valid ComplexRouteLegDTO> complexRouteLegs;
}
