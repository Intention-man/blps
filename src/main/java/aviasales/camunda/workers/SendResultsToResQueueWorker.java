package aviasales.camunda.workers;

import aviasales.management.addition.messaging.TicketAdditionResultMessage;
import aviasales.management.addition.messaging.TicketValidationResultItem;
import aviasales.management.addition.messaging.TicketValidationStatus;
import aviasales.management.addition.validator_node.TicketAdditionValidateNodeService;
import lombok.AllArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Profile("worker")
@ExternalTaskSubscription("sendResultsToResQueue")
@AllArgsConstructor
public class SendResultsToResQueueWorker implements ExternalTaskHandler {
    private final TicketAdditionValidateNodeService validateNodeService;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        try {
            String jobId = task.getVariable("jobId");
            String validatedTicketDtoId = task.getVariable("ticketRefId");
            String statusStr = task.getVariable("status");
            String rejectionReason = task.getVariable("validationMsg");

            TicketValidationStatus status = TicketValidationStatus.valueOf(statusStr);

            TicketValidationResultItem resultItem = new TicketValidationResultItem(
                    validatedTicketDtoId,
                    status,
                    rejectionReason
            );

            TicketAdditionResultMessage message = new TicketAdditionResultMessage(
                    jobId,
                    Collections.singletonList(resultItem)
            );

            validateNodeService.sendManualValidationResult(message);

            service.complete(task);
        } catch (Exception e) {
            service.handleFailure(task, e.getMessage(), e.toString(), 0, 0);
        }
    }
}
