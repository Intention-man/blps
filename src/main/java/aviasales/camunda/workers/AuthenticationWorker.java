package aviasales.camunda.workers;

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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        String username = externalTask.getVariable("username");
        String password = externalTask.getVariable("password");

        try {
            AuthenticationRequest authRequest = AuthenticationRequest.builder().username(username).password(password).build();
            AuthenticationResponse response = authenticationService.authenticate(authRequest);

            Map<String, Object> variables = new HashMap<>();
            variables.put("token", response.getToken());
            variables.put("userRole", response.getRole().name());
            variables.put("auth_ok", "true");

            externalTaskService.complete(externalTask, variables);
            LOGGER.info("User {} authenticated successfully.", username);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            LOGGER.warn("Authentication failed for user {}: {}", username, e.getMessage());

            Map<String, Object> errorVariables = new HashMap<>();
            errorVariables.put("errorMessage", "Неверный логин или пароль.");
            errorVariables.put("auth_ok", "false");

            externalTaskService.handleBpmnError(
                    externalTask,
                    "INVALID_CREDENTIALS_ERROR",
                    "Неверный логин или пароль",
                    errorVariables
            );
        } catch (Exception e) {
            LOGGER.error("Technical failure during authentication for user {}: {}", username, e.getMessage(), e);
            externalTaskService.handleFailure(
                    externalTask, "Техническая ошибка аутентификации", e.getMessage(), 0, 5000L
            );
        }
    }
}
