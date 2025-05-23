package com.example.prac.messaging.validator_node;

import com.example.prac.data.res.TicketDTO;
import com.example.prac.messaging.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TicketAdditionValidateNodeService {
    private final TicketReqStatusRepository ticketReqStatusRepository;
    private final JmsTemplate amqpJmsTemplate;
    private final TicketValidator ticketValidator;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = "ticket.addition.auto.check.requests.queue")
    @Transactional
    public void handleAdditionRequest(TicketAdditionRequestMessage requestMessage) {
        List<TicketValidationResultItem> results = new ArrayList<>();

        for (TicketWithId ticketWithId : requestMessage.tickets()) {
            String tempId = ticketWithId.temporaryId();
            TicketDTO ticketDto = ticketWithId.ticketDto();

            TicketReqStatus reqStatus = new TicketReqStatus();
            reqStatus.setJobId(requestMessage.jobId());
            reqStatus.setTicketRefId(tempId);

            try {
                reqStatus.setTicketDtoJson(objectMapper.writeValueAsString(ticketDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize ticket with tempId = " + tempId, e);
            }

            reqStatus.setStatus(TicketValidationStatus.PENDING);
            reqStatus.setCreatedAt(LocalDateTime.now());
            ticketReqStatusRepository.save(reqStatus);

            ValidationResult validation = ticketValidator.validate(ticketDto);
            reqStatus.setValidationMsg(validation.explanation());

            if (validation.isValid()) {
                reqStatus.setStatus(TicketValidationStatus.APPROVED);
            } else {
                reqStatus.setStatus(TicketValidationStatus.CANCELED);
            }

            results.add(new TicketValidationResultItem(
                    tempId,
                    reqStatus.getStatus(),
                    validation.explanation()
            ));
            ticketReqStatusRepository.save(reqStatus);
        }

        TicketAdditionResultMessage result = new TicketAdditionResultMessage(
                requestMessage.jobId(),
                results
        );
        amqpJmsTemplate.convertAndSend("ticket.addition.auto.check.results.queue", result);
    }

    // TODO метод на сохранение заявок на добавление билетов с автоматической проверкой заявок
}