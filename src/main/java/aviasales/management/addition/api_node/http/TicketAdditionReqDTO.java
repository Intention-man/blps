package aviasales.management.addition.api_node.http;

import aviasales.data.ticket.TicketDTO;
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
