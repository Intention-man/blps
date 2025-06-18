package aviasales.camunda.workers;

import aviasales.data.ticket.TicketDTO;
import aviasales.management.addition.api_node.TicketAdditionApiNodeService;
import aviasales.management.addition.api_node.http.TicketAdditionReqDTO;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@ExternalTaskSubscription(topicName = "storeAndSendTempTickets",
        variableNames = {
                "airline", "serviceClass", "price", "availableSeats", "flightNumber",
                "departureCity", "departureDate", "departureTime", "arrivalCity",
                "arrivalDate", "arrivalTime", "hours", "manualVerification"
        })
public class SaveAndSendTempTicketsWorker implements ExternalTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveAndSendTempTicketsWorker.class);

    private final TicketAdditionApiNodeService ticketService;

    public SaveAndSendTempTicketsWorker(TicketAdditionApiNodeService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOGGER.info("Worker for topic 'storeAndSendTempTickets' started for process instance {}.", externalTask.getProcessInstanceId());

        try {
            // 1. Собираем TicketDTO из отдельных переменных процесса
            // Важно правильно преобразовывать типы
            TicketDTO ticketDto = new TicketDTO();

            ticketDto.setAirline(externalTask.getVariable("airline"));
            ticketDto.setServiceClass(externalTask.getVariable("serviceClass"));

            // Camunda может вернуть число как Long или Double, приводим к нужному типу
            Number priceNum = externalTask.getVariable("price");
            ticketDto.setPrice(priceNum.intValue());

            Number seatsNum = externalTask.getVariable("availableSeats");
            ticketDto.setAvailableSeats(seatsNum != null ? seatsNum.intValue() : 0);

            ticketDto.setFlightNumber(externalTask.getVariable("flightNumber"));
            ticketDto.setDepartureCity(externalTask.getVariable("departureCity"));
            ticketDto.setArrivalCity(externalTask.getVariable("arrivalCity"));

            // Camunda возвращает даты и время как строки, их нужно распарсить
            Date departureDate = externalTask.getVariable("departureDate");
            ticketDto.setDepartureDate(departureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            String departureTimeStr = externalTask.getVariable("departureTime");
            ticketDto.setDepartureTime(departureTimeStr != null ? LocalTime.parse(departureTimeStr) : null);

            Date arrivalDate = externalTask.getVariable("arrivalDate");
            ticketDto.setArrivalDate(arrivalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            String arrivalTimeStr = externalTask.getVariable("arrivalTime");
            ticketDto.setArrivalTime(arrivalTimeStr != null ? LocalTime.parse(arrivalTimeStr) : null);

            Number hoursNum = externalTask.getVariable("hours");
            ticketDto.setHours(hoursNum != null ? hoursNum.doubleValue() : 0.0);

            String manualVerification = externalTask.getVariable("manualVerification");
            boolean isManual = manualVerification.equals("true");

            LOGGER.info("Successfully constructed TicketDTO: {}", ticketDto);

            // 2. Создаем request DTO, который ожидает ваш сервис
            TicketAdditionReqDTO requestDto = new TicketAdditionReqDTO();
            requestDto.setTickets(List.of(ticketDto)); // Создаем список из одного билета
            requestDto.setManualVerification(isManual);

            // 3. Вызываем ваш существующий бизнес-метод
            LOGGER.info("Calling storeAndSend service with manualVerification = {}", isManual);
            String jobId = ticketService.storeAndSend(requestDto);
            LOGGER.info("Service returned JobID: {}", jobId);

            // 4. Завершаем задачу Camunda и возвращаем jobId в процесс для следующего шага
            externalTaskService.complete(externalTask, Collections.singletonMap("jobId", jobId));

            LOGGER.info("Worker for topic 'storeAndSendTempTickets' completed successfully for JobID: {}", jobId);

        } catch (Exception e) {
            LOGGER.error("An error occurred in SaveAndSendTicketsWorker for process instance {}: {}",
                    externalTask.getProcessInstanceId(), e.getMessage(), e);

            // Отправляем BPMN-ошибку, чтобы процесс мог ее обработать
            externalTaskService.handleBpmnError(
                    externalTask,
                    "SAVE_TICKETS_ERROR",
                    "Failed to save and send tickets: " + e.getMessage()
            );
        }
    }
}
