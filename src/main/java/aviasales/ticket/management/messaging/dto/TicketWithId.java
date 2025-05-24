package aviasales.ticket.management.messaging.dto;

import aviasales.common.data.dto.TicketDTO;

import java.io.Serializable;

public record TicketWithId(
        String temporaryId,
        TicketDTO ticketDto
) implements Serializable {
}