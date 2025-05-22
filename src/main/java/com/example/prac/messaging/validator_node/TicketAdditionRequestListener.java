package com.example.prac.messaging.validator_node;

import com.example.prac.data.res.TicketDTO;
import com.example.prac.messaging.dto.TicketAdditionRequestMessage;
import com.example.prac.messaging.dto.TicketAdditionResultMessage;
import com.example.prac.messaging.dto.TicketValidationResultItem;
import com.example.prac.messaging.dto.TicketValidationStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TicketAdditionRequestListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketAdditionRequestListener.class);
    private final TicketReqStatusRepository ticketReqStatusRepository;
    private final JmsTemplate amqpJmsTemplate;
    private final TicketValidator ticketValidator;

    public TicketAdditionRequestListener(TicketReqStatusRepository ticketReqStatusRepository,
                                         @Qualifier("amqpJmsTemplate") JmsTemplate amqpJmsTemplate,
                                         TicketValidator ticketValidator) {
        this.ticketReqStatusRepository = ticketReqStatusRepository;
        this.amqpJmsTemplate = amqpJmsTemplate;
        this.ticketValidator = ticketValidator;
    }

    @JmsListener(destination = "ticket.addition.requests.queue", containerFactory = "amqpJmsListenerContainerFactory") // Используем правильную фабрику
    @Transactional
    public TicketAdditionResultMessage handleAdditionRequest(TicketAdditionRequestMessage requestMessage) {
        LOGGER.info("Received ticket addition request for JobID: {}", requestMessage.jobId());
        List<TicketValidationResultItem> results = new ArrayList<>();
        int index = 0;
        for (TicketDTO ticketDto : requestMessage.ticketsToValidate()) {
            String ticketRefId = requestMessage.jobId() + "_" + index;

            TicketReqStatus reqStatus = new TicketReqStatus();
            reqStatus.setJobId(requestMessage.jobId());
            reqStatus.setTicketRefId(ticketRefId);
            reqStatus.setStatus(TicketValidationStatus.ACTIVE);
            reqStatus.setCreatedAt(LocalDateTime.now());
//            try {
//                reqStatus.setTicketDtoJson(new ObjectMapper().writeValueAsString(ticketDto));
//            } catch (JsonProcessingException e) {
//                LOGGER.warn("Could not serialize TicketDTO to JSON for logging for ticketRefId: {}", ticketRefId, e);
//            }
            ticketReqStatusRepository.save(reqStatus);

            ValidationResult validation = ticketValidator.validate(ticketDto); // ticketValidator.validate должен вернуть объект с boolean и причиной отказа

            if (validation.isValid()) {
                reqStatus.setStatus(TicketValidationStatus.APPROVED);
                results.add(new TicketValidationResultItem(index, TicketValidationStatus.APPROVED, null, ticketDto)); // Передаем DTO обратно
            } else {
                reqStatus.setStatus(TicketValidationStatus.CANCELED);
                reqStatus.setValidationMsg(validation.rejectionReason());
                results.add(new TicketValidationResultItem(index, TicketValidationStatus.CANCELED, validation.rejectionReason(), null));
            }
            reqStatus.setUpdatedAt(LocalDateTime.now());
            ticketReqStatusRepository.save(reqStatus);
            index++;
        }

        TicketAdditionResultMessage resultMessage = new TicketAdditionResultMessage(requestMessage.jobId(), results);
        amqpJmsTemplate.convertAndSend("ticket.addition.results.queue", resultMessage);
        LOGGER.info("Processed ticket addition request for JobID: {} and sent results.", requestMessage.jobId());
        return resultMessage;
    }
}