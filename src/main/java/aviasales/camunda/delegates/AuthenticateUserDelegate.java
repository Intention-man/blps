package aviasales.camunda.delegates;

import aviasales.security.data.AuthenticationRequest;
import aviasales.security.data.AuthenticationResponse;
import aviasales.security.service.AuthenticationService;
import aviasales.security.service.UserXmlService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("AuthenticateUserDelegate")
@RequiredArgsConstructor
public class AuthenticateUserDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateUserDelegate.class);
    private final AuthenticationService authenticationService;
    private final UserXmlService userXmlService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("AuthenticateUserDelegate started for process instance {}", execution.getProcessInstanceId());

        String username = (String) execution.getVariable("username");
        String password = (String) execution.getVariable("password");

        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build();

        try {
            AuthenticationResponse response = authenticationService.authenticate(authRequest);
            execution.setVariable("authToken", response.getToken());
            execution.setVariable("userRole", response.getRole().name());
            userXmlService.findByUsername(username)
                    .ifPresent(user -> execution.setVariable("authenticatedUserId", user.getId()));
            LOGGER.info("User {} authenticated successfully. Token generated.", username);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            LOGGER.warn("Authentication failed for user {}: {}", username, e.getMessage());
            throw new BpmnError("AUTH_FAILED", "Ошибка аутентификации: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected technical error during authentication for user {}", username, e);
            throw new BpmnError("AUTH_TECH_ERROR", "Техническая ошибка: " + e.getMessage());
        }
    }
}
