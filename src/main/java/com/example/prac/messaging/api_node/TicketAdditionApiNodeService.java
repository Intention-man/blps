package com.example.prac.messaging.api_node;

import com.example.prac.data.res.TicketDTO;
import com.example.prac.messaging.dto.*;
import com.example.prac.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class TicketAdditionApiNodeService {
    private TicketService ticketService;
    private TemporaryTicketService temporaryTicketService;
    private final JmsTemplate amqpJmsTemplate;

    @Transactional
    public String storeAndSend(List<TicketDTO> ticketsDto) {
        String jobId = UUID.randomUUID().toString();
        List<TicketWithId> ticketsWithIds = new ArrayList<>();

        for (TicketDTO ticket : ticketsDto) {
            String tempId = temporaryTicketService.storeTicket(ticket, jobId);
            ticketsWithIds.add(new TicketWithId(tempId, ticket));
        }

        TicketAdditionRequestMessage message = new TicketAdditionRequestMessage(jobId, ticketsWithIds);
        amqpJmsTemplate.convertAndSend("ticket.addition.auto.check.requests.queue", message);
        return jobId;
    }

    @JmsListener(destination = "ticket.addition.auto.check.results.queue", containerFactory = "amqpJmsListenerContainerFactory")
    @Transactional
    public void handleAdditionResult(TicketAdditionResultMessage resultMessage) {
        List<String> approvedTicketIds = new ArrayList<>();

        for (TicketValidationResultItem item : resultMessage.validationResults()) {
            if (item.status() == TicketValidationStatus.APPROVED && item.validatedTicketDtoId() != null) {
                approvedTicketIds.add(item.validatedTicketDtoId());
            } else {
                temporaryTicketService.removeTicket(item.validatedTicketDtoId());
            }
        }

        List<TicketDTO> ticketsToAdd = temporaryTicketService.retrieveApprovedTickets(approvedTicketIds);

        if (!ticketsToAdd.isEmpty()) {
            ticketService.saveTickets(ticketsToAdd);
        }

        approvedTicketIds.forEach(temporaryTicketService::removeTicket);
    }

    // TODO метод на получение результатов ручной проверки билетов и сохранение в БД
}
