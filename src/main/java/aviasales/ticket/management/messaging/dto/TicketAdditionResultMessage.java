package aviasales.ticket.management.messaging.dto;

import java.io.Serializable;
import java.util.List;

public record TicketAdditionResultMessage(
        String jobId,
        List<TicketValidationResultItem> validationResults
) implements Serializable {
}