package aviasales.scheduled.jobs;

import aviasales.management.generation.GenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
@DisallowConcurrentExecution
public class GenerateTicketsJob implements Job {
    private final GenerationService generationService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
            generationService.generateAndSaveLimitedTickets(LocalDate.now().plusDays(1));
            log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
        } catch (Exception e) {
            log.error("Job with error {}", e.getMessage());
        }
    }
}
