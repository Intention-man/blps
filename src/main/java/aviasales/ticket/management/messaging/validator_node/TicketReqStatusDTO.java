package aviasales.ticket.management.messaging.validator_node;


import aviasales.common.data.dto.TicketDTO;
import aviasales.ticket.management.messaging.dto.TicketValidationStatus;

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