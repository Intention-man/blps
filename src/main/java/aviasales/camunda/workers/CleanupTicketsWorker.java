package aviasales.camunda.workers;

import aviasales.data.ticket.TicketRepository;
import jakarta.transaction.Transactional;
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
@ExternalTaskSubscription("clearTickets")
@AllArgsConstructor
public class CleanupTicketsWorker implements ExternalTaskHandler {
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public void execute(ExternalTask task, ExternalTaskService service) {
        ticketRepository.deleteByArrivalDateBefore(LocalDate.now());
    }
}
