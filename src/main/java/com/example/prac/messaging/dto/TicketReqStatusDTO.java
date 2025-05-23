package com.example.prac.messaging.dto;


import java.time.LocalDateTime;

public record TicketReqStatusDTO(
        Long id,
        String jobId,
        String ticketRefId,
        TicketValidationStatus status,
        String validationMsg,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}