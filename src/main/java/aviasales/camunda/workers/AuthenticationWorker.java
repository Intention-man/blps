package aviasales.camunda.workers; // Создайте новый пакет для воркеров

import aviasales.security.data.AuthenticationRequest;
import aviasales.security.data.AuthenticationResponse;
import aviasales.security.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("worker")
@ExternalTaskSubscription("login_topic")
@AllArgsConstructor
public class AuthenticationWorker implements ExternalTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationWorker.class);
    private final AuthenticationService authenticationService;


    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOGGER.info("Worker for topic {} started.", externalTask.getTopicName());

        String username = externalTask.getVariable("username");
        String password = externalTask.getVariable("password");

        try {
            AuthenticationRequest authRequest = AuthenticationRequest.builder()
                    .username(username)
                    .password(password)
                    .build();

            AuthenticationResponse response = authenticationService.authenticate(authRequest);

            Map<String, Object> variables = new HashMap<>();
            // Добавляем переменные для отображения в форме результата
            variables.put("token", response.getToken());
            variables.put("userRole", response.getRole().name());
            variables.put("resultMessage", String.format("Добро пожаловать, %s! Вы успешно авторизованы.", username));
            variables.put("userInfo", String.format("Пользователь: %s\nРоль: %s", username, response.getRole().name()));
            variables.put("success", true);

            externalTaskService.complete(externalTask, variables);
            LOGGER.info("User {} authenticated successfully. Worker complete.", username);

        } catch (Exception e) {
            LOGGER.error("Authentication failed for user {}: {}", username, e.getMessage());

            Map<String, Object> errorVariables = new HashMap<>();
            errorVariables.put("errorMessage", "Ошибка входа: " + e.getMessage());
            errorVariables.put("errorDetails", "Проверьте правильность логина и пароля");
            errorVariables.put("success", false);

            externalTaskService.handleBpmnError(
                    externalTask,
                    "AUTH_FAILED",
                    "Ошибка аутентификации: " + e.getMessage(),
                    errorVariables
            );
        }
    }
}
