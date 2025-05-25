package aviasales.scheduled.jobs;

import aviasales.management.generation.GenerationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Transactional
public class GenerateTicketsJob implements Job {
    private final GenerationService generationService;

    @Override
    public void execute(JobExecutionContext context) {
        generationService.generateAndSaveLimitedTickets(LocalDate.now().plusDays(1));
    }
}