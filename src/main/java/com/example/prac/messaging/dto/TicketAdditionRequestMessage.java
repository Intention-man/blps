package com.example.prac.messaging.dto;

import com.example.prac.data.res.TicketDTO;

import java.io.Serializable;
import java.util.List;

public record TicketAdditionRequestMessage(
        String jobId,
        List<TicketDTO> ticketsToValidate
) implements Serializable {}