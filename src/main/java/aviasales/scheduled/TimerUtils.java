package aviasales.scheduled;


import org.quartz.*;

import java.util.Date;

public final class TimerUtils {
    private TimerUtils() {
    }

    public static JobDetail buildJobDetail(final Class<? extends Job> jobClass) {
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .build();
    }

    public static Trigger buildTrigger(final Class<? extends Job> jobClass, long intervalInMillis) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInMilliseconds(intervalInMillis)
                .repeatForever();

        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobClass.getSimpleName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis()))
                .build();
    }
}
