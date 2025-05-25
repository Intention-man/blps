package aviasales.management.addition.validator_node.req.status;


import aviasales.data.ticket.TicketDTO;
import aviasales.management.addition.messaging.TicketValidationStatus;

import java.io.Serializable;
import java.time.LocalDateTime;


public record TicketReqStatusDTO(
        Long id,
        String jobId,
        String ticketRefId,
        TicketDTO ticketDto,
        TicketValidationStatus status,
        String validationMsg,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}