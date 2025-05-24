package aviasales.ticket.management.messaging.api_node;

import aviasales.common.data.dto.TicketDTO;
import aviasales.common.service.TicketService;
import aviasales.ticket.management.data.dto.TicketAdditionReqDTO;
import aviasales.ticket.management.messaging.dto.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static aviasales.ticket.management.messaging.dto.QueueNames.*;


@Component
@AllArgsConstructor
public class TicketAdditionApiNodeService {
    private final JmsTemplate amqpJmsTemplate;
    private TicketService ticketService;
    private TemporaryTicketService temporaryTicketService;

    @Transactional
    public String storeAndSend(TicketAdditionReqDTO req) {
        String jobId = UUID.randomUUID().toString();
        List<TicketWithId> ticketsWithIds = new ArrayList<>();

        for (TicketDTO ticket : req.getTickets()) {
            String tempId = temporaryTicketService.storeTicket(ticket, jobId);
            ticketsWithIds.add(new TicketWithId(tempId, ticket));
        }

        TicketAdditionRequestMessage message = new TicketAdditionRequestMessage(jobId, ticketsWithIds);
        String queueName = req.isManualVerification() ? MANUAL_CHECK_REQ : AUTO_CHECK_REQ;
        amqpJmsTemplate.convertAndSend(queueName, message);
        return jobId;
    }

    @JmsListener(destination = CHECK_RES, containerFactory = "amqpJmsListenerContainerFactory")
    @Transactional
    public void handleAutoCheckResult(TicketAdditionResultMessage resultMessage) {
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
}
