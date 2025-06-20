package aviasales.camunda.workers;

import aviasales.ytsaurus.YTsaurusService;
import lombok.AllArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("worker")
@ExternalTaskSubscription("addStatistics")
@AllArgsConstructor
public class AddStatisticsTopicWorker implements ExternalTaskHandler {
    private final YTsaurusService ytService;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        try {
            ytService.addStatistics();
            service.complete(task);
        } catch (Exception e) {
            service.handleFailure(task, e.getMessage(), e.toString(), 0, 0);
        }
    }

}
