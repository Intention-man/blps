package aviasales.camunda.workers;

import aviasales.security.data.AuthenticationResponse;
import aviasales.security.data.RegisterRequest;
import aviasales.security.service.AuthenticationService;
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
@ExternalTaskSubscription("registration_topic")
public class RegistrationWorker implements ExternalTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationWorker.class);
    private final AuthenticationService authenticationService; // или RegistrationService если есть отдельный

    public RegistrationWorker(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOGGER.info("Registration worker started for task: {}", externalTask.getId());

        String username = externalTask.getVariable("username");
        String password = externalTask.getVariable("password");
        String adminCode = externalTask.getVariable("admin_code");

        try {
            RegisterRequest regRequest = RegisterRequest.builder()
                    .username(username)
                    .password(password)
                    .adminCode(adminCode)
                    .build();

            AuthenticationResponse response = authenticationService.register(regRequest);

            Map<String, Object> variables = new HashMap<>();
            variables.put("authToken", response.getToken());
            variables.put("userRole", response.getRole().name());
            variables.put("resultMessage", String.format("Регистрация успешна! Добро пожаловать, %s!", username));
            variables.put("userInfo", String.format("Новый пользователь: %s\nРоль: %s", username, response.getRole().name()));
            variables.put("success", true);

            externalTaskService.complete(externalTask, variables);
            LOGGER.info("User {} registered successfully.", username);

        } catch (Exception e) {
            LOGGER.error("Registration failed for user {}: {}", username, e.getMessage());

            Map<String, Object> errorVariables = new HashMap<>();
            errorVariables.put("errorMessage", "Ошибка регистрации: " + e.getMessage());
            errorVariables.put("errorDetails", "Пользователь уже существует или неверный код администратора");
            errorVariables.put("success", false);

            externalTaskService.handleBpmnError(
                    externalTask,
                    "REGISTRATION_FAILED",
                    "Ошибка регистрации: " + e.getMessage(),
                    errorVariables
            );
        }
    }
}