package aviasales.ticket.management.messaging.validator_node;

import aviasales.ticket.management.messaging.dto.TicketValidationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "aviasales_ticket_req_status")
@Getter
@Setter
@NoArgsConstructor
public class TicketReqStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobId;
    @Column(name = "ticketrefid")
    private String ticketRefId;
    @Enumerated(EnumType.STRING)
    private TicketValidationStatus status;
    @Column(columnDefinition = "TEXT")
    private String ticketDtoJson;
    @Column(columnDefinition = "TEXT")
    private String validationMsg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
