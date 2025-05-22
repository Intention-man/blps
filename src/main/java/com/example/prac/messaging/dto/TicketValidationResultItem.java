package com.example.prac.messaging.dto;

import com.example.prac.data.res.TicketDTO;

import java.io.Serializable;

public record TicketValidationResultItem(
        int originalIndex, // Индекс билета в исходном списке List<TicketDTO>
        // String clientTicketId, // Или если у TicketDTO есть какой-то временный ID от клиента
        TicketValidationStatus status, // APPROVED, CANCELED
        String rejectionReason, // Если CANCELED
        TicketDTO validatedTicketDto // Опционально: возвращаем DTO, если он был как-то изменен/обогащен при валидации
) implements Serializable {}