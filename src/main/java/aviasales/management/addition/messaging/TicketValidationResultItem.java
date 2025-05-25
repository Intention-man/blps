package aviasales.management.addition.messaging;

import java.io.Serializable;

public record TicketValidationResultItem(
        String validatedTicketDtoId,
        TicketValidationStatus status,
        String rejectionReason
) implements Serializable {
}