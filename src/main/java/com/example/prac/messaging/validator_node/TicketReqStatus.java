package com.example.prac.messaging.validator_node;

import com.example.prac.messaging.dto.TicketValidationStatus;
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
    private String ticketRefId; // Может быть originalIndex + jobId
    @Enumerated(EnumType.STRING)
    private TicketValidationStatus status;
    @Column(columnDefinition = "TEXT")
    private String ticketDtoJson;
    @Column(columnDefinition = "TEXT")
    private String validationMsg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
