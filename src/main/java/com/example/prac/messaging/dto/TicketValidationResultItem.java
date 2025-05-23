package com.example.prac.messaging.dto;

import java.io.Serializable;

public record TicketValidationResultItem(
        String validatedTicketDtoId,
        TicketValidationStatus status,
        String rejectionReason
) implements Serializable {
}