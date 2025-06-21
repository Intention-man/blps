package aviasales.camunda.workers;

import aviasales.exception.IncorrectAdminCode;
import aviasales.exception.UserAlreadyExistsException;
import aviasales.security.data.AuthenticationResponse;
import aviasales.security.data.RegisterRequest;
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
@ExternalTaskSubscription("registration_topic")
@AllArgsConstructor
public class RegistrationWorker implements ExternalTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationWorker.class);
    private final AuthenticationService authenticationService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String username = externalTask.getVariable("username");
        String password = externalTask.getVariable("password");
        String adminCode = externalTask.getVariable("admin_code");

        try {
            RegisterRequest regRequest = RegisterRequest.builder().username(username).password(password).adminCode(adminCode).build();
            AuthenticationResponse response = authenticationService.register(regRequest);

            Map<String, Object> variables = new HashMap<>();
            variables.put("authToken", response.getToken());
            variables.put("userRole", response.getRole().name());
            variables.put("auth_ok", "true");

            externalTaskService.complete(externalTask, variables);
            LOGGER.info("User {} registered successfully.", username);

        } catch (UserAlreadyExistsException e) {
            LOGGER.warn("Registration failed: user {} already exists.", username);

            Map<String, Object> errorVars = new HashMap<>();
            errorVars.put("errorMessage", e.getMessage());
            errorVars.put("auth_ok", "false");

            externalTaskService.handleBpmnError(
                    externalTask, "USER_ALREADY_EXISTS_ERROR", e.getMessage(), errorVars
            );
        } catch (IncorrectAdminCode e) {
            LOGGER.warn("Registration failed for user {}: incorrect admin code.", username);

            Map<String, Object> errorVars = new HashMap<>();
            errorVars.put("errorMessage", e.getMessage());
            errorVars.put("auth_ok", "false");

            externalTaskService.handleBpmnError(
                    externalTask, "INVALID_ADMIN_CODE_ERROR", e.getMessage(), errorVars
            );
        } catch (Exception e) {
            LOGGER.error("Technical failure during registration for user {}: {}", username, e.getMessage(), e);
            externalTaskService.handleFailure(
                    externalTask, "Техническая ошибка регистрации", e.getMessage(), 0, 5000L
            );
        }
    }
}