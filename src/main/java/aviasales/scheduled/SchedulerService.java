package aviasales.scheduled;


import aviasales.scheduled.jobs.CleanupTicketsJob;
import aviasales.scheduled.jobs.GenerateTicketsJob;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SchedulerService {
    private final Scheduler quartzScheduler;

    public void enableOrDisableJob(String className, boolean setActive) throws Exception {
        Class<? extends Job> jobClass = getJobClassByName(className);
        JobKey jobKey = new JobKey(jobClass.getSimpleName());

        if (setActive) {
            if (quartzScheduler.checkExists(jobKey)) {
                return;
            }
            JobDetail jobDetail = TimerUtils.buildJobDetail(jobClass);
            Trigger trigger = TimerUtils.buildTrigger(jobClass, 60_000);
            quartzScheduler.scheduleJob(jobDetail, trigger);
        } else {
            if (quartzScheduler.checkExists(jobKey)) {
                quartzScheduler.deleteJob(jobKey);
            }
        }
    }

    private Class<? extends org.quartz.Job> getJobClassByName(String className) {
        return switch (className) {
            case "GenerateTicketsJob" -> GenerateTicketsJob.class;
            case "CleanupTicketsJob" -> CleanupTicketsJob.class;
            default -> throw new IllegalStateException("Unexpected job name: " + className);
        };
    }
}
