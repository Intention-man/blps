package aviasales.management.addition.api_node;

import aviasales.data.city.CityService;
import aviasales.data.ticket.*;
import aviasales.management.addition.api_node.http.TicketAdditionReqDTO;
import aviasales.management.addition.api_node.temp.storage.TemporaryTicketService;
import aviasales.management.addition.messaging.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static aviasales.management.addition.messaging.QueueNames.*;


@Component
@AllArgsConstructor
public class TicketAdditionApiNodeService {
    private final JmsTemplate amqpJmsTemplate;
    private TemporaryTicketService temporaryTicketService;
    private TicketRepository ticketRepository;
    private CityService cityService;
    private TicketMapper ticketMapper;

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
            addTickets(ticketsToAdd);
        }

        approvedTicketIds.forEach(temporaryTicketService::removeTicket);
    }

    @Transactional
    public void addTickets(List<TicketDTO> ticketsDTO) {
        List<Ticket> newTickets = new ArrayList<>();
        for (TicketDTO ticketDTO : ticketsDTO) {
            cityService.ensureCityExists(ticketDTO.getDepartureCity());
            cityService.ensureCityExists(ticketDTO.getArrivalCity());
            newTickets.add(ticketMapper.mapFrom(ticketDTO));
        }

        ticketRepository.saveAll(newTickets);
    }
}
