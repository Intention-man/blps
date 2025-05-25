package aviasales.management.addition.messaging;

import aviasales.data.ticket.TicketDTO;

import java.io.Serializable;

public record TicketWithId(
        String temporaryId,
        TicketDTO ticketDto
) implements Serializable {
}