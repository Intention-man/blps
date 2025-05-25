package aviasales.scheduled.config;

import aviasales.scheduled.jobs.GenerateTicketsJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail generateTicketsJobDetail() {
        return JobBuilder.newJob(GenerateTicketsJob.class)
                .withIdentity("generateTicketsJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger generateTicketsTrigger(JobDetail generateTicketsJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(generateTicketsJobDetail)
                .withIdentity("generateTicketsTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1))
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            @Qualifier("dataSource") DataSource dataSource,
            PlatformTransactionManager transactionManager) {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setAutoStartup(true);
        factory.setOverwriteExistingJobs(true);
        factory.setWaitForJobsToCompleteOnShutdown(true);

        return factory;
    }
}
