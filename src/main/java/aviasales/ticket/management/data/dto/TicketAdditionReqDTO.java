package aviasales.ticket.management.data.dto;

import aviasales.common.data.dto.TicketDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TicketAdditionReqDTO {
    @NotNull
    private List<TicketDTO> tickets;
    @NotNull
    private boolean manualVerification;
}
