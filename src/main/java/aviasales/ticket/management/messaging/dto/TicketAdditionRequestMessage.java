package aviasales.ticket.management.messaging.dto;

import java.io.Serializable;
import java.util.List;

public record TicketAdditionRequestMessage(
        String jobId,
        List<TicketWithId> tickets
) implements Serializable {
}