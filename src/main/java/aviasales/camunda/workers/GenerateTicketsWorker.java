package aviasales.camunda.workers;

import aviasales.management.generation.GenerationService;
import lombok.AllArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@Profile("worker")
@ExternalTaskSubscription("generateTickets")
@AllArgsConstructor
public class GenerateTicketsWorker implements ExternalTaskHandler {
    private final GenerationService generationService;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        generationService.generateAndSaveLimitedTickets(LocalDate.now().plusDays(1));
        service.complete(task);
    }
}