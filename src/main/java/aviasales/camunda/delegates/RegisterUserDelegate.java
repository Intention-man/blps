package aviasales.camunda.delegates;

import aviasales.exception.IncorrectAdminCode;
import aviasales.exception.UserAlreadyExistsException;
import aviasales.security.data.RegisterRequest;
import aviasales.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("RegisterUserDelegate")
@RequiredArgsConstructor
public class RegisterUserDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserDelegate.class);
    private final AuthenticationService authenticationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("RegisterUserDelegate started for process instance {}", execution.getProcessInstanceId());

        String username = (String) execution.getVariable("username");
        String password = (String) execution.getVariable("password");
        String adminCode = (String) execution.getVariable("adminCode");

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username(username)
                .password(password)
                .adminCode(adminCode)
                .build();

        try {
            authenticationService.register(registerRequest);

            LOGGER.info("User {} registered successfully.", username);

        } catch (UserAlreadyExistsException | IncorrectAdminCode e) {
            LOGGER.warn("Registration failed for user {}: {}", username, e.getMessage());
            // ----- ВЫБРАСЫВАЕМ BPMN ОШИБКУ -----
            // Код 'REG_FAILED' должен совпадать с кодом в Boundary Error Event!
            throw new BpmnError("REG_FAILED", "Ошибка регистрации: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected technical error during registration for user {}", username, e);
            throw new BpmnError("REG_TECH_ERROR", "Техническая ошибка: " + e.getMessage());
        }
    }
}
