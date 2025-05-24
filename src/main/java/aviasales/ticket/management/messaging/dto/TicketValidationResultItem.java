package aviasales.ticket.management.messaging.dto;

import java.io.Serializable;

public record TicketValidationResultItem(
        String validatedTicketDtoId,
        TicketValidationStatus status,
        String rejectionReason
) implements Serializable {
}