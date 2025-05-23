package com.example.prac.messaging.dto;

import com.example.prac.data.res.TicketDTO;

import java.io.Serializable;

public record TicketWithId(
        String temporaryId,
        TicketDTO ticketDto
) implements Serializable {}