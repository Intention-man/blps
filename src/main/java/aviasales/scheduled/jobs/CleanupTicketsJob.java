package aviasales.scheduled.jobs;

import aviasales.data.ticket.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
@Transactional
public class CleanupTicketsJob implements Job {
    private final TicketRepository ticketRepository;

    @Override
    public void execute(JobExecutionContext context) {
        ticketRepository.deleteByArrivalDateBefore(LocalDate.now());
    }
}
