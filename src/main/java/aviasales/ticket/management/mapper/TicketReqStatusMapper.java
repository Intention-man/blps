package aviasales.ticket.management.mapper;

import aviasales.common.data.dto.TicketDTO;
import aviasales.ticket.management.messaging.validator_node.TicketReqStatus;
import aviasales.ticket.management.messaging.validator_node.TicketReqStatusDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TicketReqStatusMapper {
    private final ObjectMapper objectMapper;

    public TicketReqStatusDTO mapTo(TicketReqStatus status) {
        try {
            TicketDTO ticketDto = objectMapper.readValue(status.getTicketDtoJson(), TicketDTO.class);

            return new TicketReqStatusDTO(
                    status.getId(),
                    status.getJobId(),
                    status.getTicketRefId(),
                    ticketDto,
                    status.getStatus(),
                    status.getValidationMsg(),
                    status.getCreatedAt(),
                    status.getUpdatedAt()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize aviasales.ticket DTO for request: " + status.getId(), e);
        }
    }
}