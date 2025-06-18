package aviasales.camunda.workers;

import aviasales.management.addition.validator_node.TicketAdditionValidateNodeService;
import aviasales.management.addition.validator_node.req.status.TicketReqStatusDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile("worker")
@ExternalTaskSubscription("getRequests")
@AllArgsConstructor
public class GetRequestsWorker implements ExternalTaskHandler {
    private final TicketAdditionValidateNodeService validateNodeService;
    private final ObjectMapper objectMapper;


    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        String jobId = task.getVariable("jobId");
        try {
            // Получаем единственную заявку (по условию!)
            List<TicketReqStatusDTO> tickets = validateNodeService.manuallyValidation(jobId);
            if (tickets.isEmpty()) {
                service.handleBpmnError(task, "NO_PENDING_TICKET", "Нет заявок для джоба: " + jobId);
                return;
            }
            TicketReqStatusDTO ticket = tickets.get(0);

            // Можно передавать DTO как json (TicketDTO) или отдельными полями
            String ticketDtoJson = objectMapper.writeValueAsString(ticket.ticketDto());

            Map<String, Object> variables = new HashMap<>();
            variables.put("jobId", ticket.jobId());
            variables.put("ticketRefId", ticket.ticketRefId());
            variables.put("ticketDtoJson", ticketDtoJson);
            variables.put("status", ticket.status().name());
            variables.put("validationMsg", ticket.validationMsg());
            service.complete(task, variables);
        } catch (Exception e) {
            service.handleFailure(task, e.getMessage(), e.toString(), 0, 0);
        }
    }
}
