package aviasales.management.addition.validator_node;

import aviasales.data.ticket.TicketDTO;
import aviasales.management.addition.messaging.*;
import aviasales.management.addition.validator_node.req.status.TicketReqStatus;
import aviasales.management.addition.validator_node.req.status.TicketReqStatusDTO;
import aviasales.management.addition.validator_node.req.status.TicketReqStatusMapper;
import aviasales.management.addition.validator_node.req.status.TicketReqStatusRepository;
import aviasales.management.addition.validator_node.validation.TicketValidator;
import aviasales.management.addition.validator_node.validation.ValidationResult;
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

import static aviasales.management.addition.messaging.QueueNames.*;


@Component
@AllArgsConstructor
public class TicketAdditionValidateNodeService {
    private final TicketReqStatusRepository ticketReqStatusRepository;
    private final JmsTemplate amqpJmsTemplate;
    private final TicketValidator ticketValidator;
    private final ObjectMapper objectMapper;
    private final TicketReqStatusMapper ticketReqStatusMapper;

    @JmsListener(destination = AUTO_CHECK_REQ)
    @Transactional
    public void autoHandleRequest(TicketAdditionRequestMessage requestMessage) {
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

            reqStatus.setCreatedAt(LocalDateTime.now());

            ValidationResult validation = ticketValidator.validate(ticketDto);
            reqStatus.setValidationMsg(validation.explanation());
            reqStatus.setStatus(validation.isValid() ? TicketValidationStatus.APPROVED : TicketValidationStatus.CANCELED);

            results.add(new TicketValidationResultItem(tempId, reqStatus.getStatus(), reqStatus.getValidationMsg()));
            ticketReqStatusRepository.save(reqStatus);
        }

        TicketAdditionResultMessage result = new TicketAdditionResultMessage(
                requestMessage.jobId(),
                results
        );
        amqpJmsTemplate.convertAndSend(CHECK_RES, result);
    }

    @JmsListener(destination = MANUAL_CHECK_REQ)
    @Transactional
    public void saveRequestForManuallyHandling(TicketAdditionRequestMessage requestMessage) {
        for (TicketWithId ticketWithId : requestMessage.tickets()) {
            String tempId = ticketWithId.temporaryId();
            TicketDTO ticketDto = ticketWithId.ticketDto();

            TicketReqStatus reqStatus = new TicketReqStatus();
            reqStatus.setJobId(requestMessage.jobId());
            reqStatus.setTicketRefId(tempId);

            try {
                reqStatus.setTicketDtoJson(objectMapper.writeValueAsString(ticketDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize aviasales.data.ticket with tempId = " + tempId, e);
            }

            reqStatus.setStatus(TicketValidationStatus.PENDING);
            reqStatus.setCreatedAt(LocalDateTime.now());
            ticketReqStatusRepository.save(reqStatus);
        }
    }

    public List<TicketReqStatusDTO> manuallyValidation(String jobId) {
        List<TicketReqStatus> pendingRequests = ticketReqStatusRepository
                .findByJobIdAndStatus(jobId, TicketValidationStatus.PENDING);

        return pendingRequests.stream()
                .map(ticketReqStatusMapper::mapTo)
                .toList();
    }

    @Transactional
    public void sendManualValidationResult(TicketAdditionResultMessage resultMessage) {
        resultMessage.validationResults().forEach(item ->
                ticketReqStatusRepository.updateStatusByTicketRefId(item.validatedTicketDtoId(), item.status()));

        amqpJmsTemplate.convertAndSend(CHECK_RES, resultMessage);
    }
}