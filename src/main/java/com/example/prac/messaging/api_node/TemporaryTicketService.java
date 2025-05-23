package com.example.prac.messaging.api_node;

import com.example.prac.data.res.TicketDTO;
import com.example.prac.messaging.dto.TicketValidationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TemporaryTicketService {
    private final TemporaryTicketRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public String storeTicket(TicketDTO ticket, String jobId) {
        String ticketId = UUID.randomUUID().toString();
        TemporaryTicket tempTicket = new TemporaryTicket();
        tempTicket.setId(ticketId);
        try {
            tempTicket.setTicketJson(objectMapper.writeValueAsString(ticket));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ticket from jobId = " + jobId, e);
        }
        tempTicket.setJobId(jobId);
        tempTicket.setStatus(TicketValidationStatus.PENDING);
        tempTicket.setCreatedAt(LocalDateTime.now());
        tempTicket.setExpiresAt(LocalDateTime.now().plusHours(1));

        repository.save(tempTicket);
        return ticketId;
    }

    @Transactional
    public TicketDTO retrieveTicket(String ticketId) throws JsonProcessingException {
        return repository.findById(ticketId)
                .map(t -> {
                    try {
                        return objectMapper.readValue(t.getTicketJson(), TicketDTO.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to deserialize ticket", e);
                    }
                })
                .orElse(null);
    }

    @Transactional
    public void removeTicket(String ticketId) {
        repository.deleteById(ticketId);
    }

    @Transactional
    public void removeTicketsByJobId(String jobId) {
        repository.deleteByJobId(jobId);
    }

    @Transactional
    public void cleanupExpiredTickets() {
        repository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    @Transactional
    public List<TicketDTO> retrieveApprovedTickets(List<String> ticketIds) {
        List<TicketDTO> result = new ArrayList<>();
        for (String id : ticketIds) {
            try {
                TicketDTO ticket = retrieveTicket(id);
                if (ticket != null) {
                    result.add(ticket);
                }
            } catch (JsonProcessingException ignored) {
            }
        }
        return result;
    }
}
