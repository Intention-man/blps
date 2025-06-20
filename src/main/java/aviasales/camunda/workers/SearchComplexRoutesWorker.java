package aviasales.camunda.workers;

import aviasales.search.data.dto.ComplexTravelSearchRequestDTO;
import aviasales.search.data.dto.SearchResponseDTO;
import aviasales.search.service.DateTimeService;
import aviasales.search.service.TicketSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Component
@Profile("worker")
@ExternalTaskSubscription("searchComplexRoutes")
@RequiredArgsConstructor
public class SearchComplexRoutesWorker implements ExternalTaskHandler {
    private final TicketSearchService ticketSearchService;
    private final ObjectMapper objectMapper;
    private final DateTimeService dateTimeService;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        try {
            String parameters = task.getVariable("parameters");
            ComplexTravelSearchRequestDTO reqDto = objectMapper.readValue(parameters, ComplexTravelSearchRequestDTO.class);
            dateTimeService.validate(reqDto);
            SearchResponseDTO result = ticketSearchService.searchComplexRoutes(reqDto, 0, 10);
            Map<String, Object> vars = new HashMap<>();
            String prettyVariants = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result.getVariants());
            vars.put("variants", Variables.stringValue(prettyVariants));
            service.complete(task, vars);
        } catch (Exception ex) {
            service.handleFailure(task, ex.getMessage(), Arrays.toString(ex.getStackTrace()), 0, 0);
        }
    }
}
