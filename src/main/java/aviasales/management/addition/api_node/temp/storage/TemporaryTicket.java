package aviasales.management.addition.api_node.temp.storage;

import aviasales.management.addition.messaging.TicketValidationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "aviasales_temporary_tickets")
@Getter
@Setter
public class TemporaryTicket {
    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String ticketJson;

    private String jobId;

    @Enumerated(EnumType.STRING)
    private TicketValidationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
