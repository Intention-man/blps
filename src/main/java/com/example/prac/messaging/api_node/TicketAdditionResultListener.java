package com.example.prac.messaging.api_node;

import com.example.prac.data.res.TicketDTO;
import com.example.prac.messaging.dto.TicketAdditionResultMessage;
import com.example.prac.messaging.dto.TicketValidationResultItem;
import com.example.prac.messaging.dto.TicketValidationStatus;
import com.example.prac.service.TicketService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketAdditionResultListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketAdditionResultListener.class);

    @Autowired
    private TicketService ticketService;

    @JmsListener(destination = "ticket.addition.results.queue", containerFactory = "amqpJmsListenerContainerFactory") // Убедись, что используешь правильную фабрику
    @Transactional // Локальная транзакция для БД Узла 1
    public void handleAdditionResult(TicketAdditionResultMessage resultMessage) {
        LOGGER.info("Received ticket addition result for JobID: {}", resultMessage.jobId());

        // Здесь нужно получить исходные TicketDTO, например, из временного хранилища
        // List<TicketDTO> originalTickets = temporaryRequestStorage.getRequest(resultMessage.jobId()).getTicketsToValidate();

        List<TicketDTO> ticketsToAdd = new ArrayList<>();
        for (TicketValidationResultItem item : resultMessage.validationResults()) {
            if (item.status() == TicketValidationStatus.APPROVED) {
                // Если TicketDTO передается в item, используем его:
                if (item.validatedTicketDto() != null) {
                    ticketsToAdd.add(item.validatedTicketDto());
                } else {
                    // Иначе нужно найти исходный DTO по индексу.
                    // Это требует хранения исходного запроса.
                    // TicketDTO originalDto = originalTickets.get(item.originalIndex());
                    // ticketsToAdd.add(originalDto);
                    LOGGER.warn("No validatedTicketDto provided for approved item at index {} for JobID {}", item.originalIndex(), resultMessage.jobId());
                }
            } else {
                LOGGER.info("Ticket at index {} for JobID {} was CANCELED: {}", item.originalIndex(), resultMessage.jobId(), item.rejectionReason());
            }
        }

        if (!ticketsToAdd.isEmpty()) {
            ticketService.saveTickets(ticketsToAdd);
            LOGGER.info("Successfully added {} approved tickets for JobID: {}", ticketsToAdd.size(), resultMessage.jobId());
        } else {
            LOGGER.info("No tickets approved for addition for JobID: {}", resultMessage.jobId());
        }
    }
}
