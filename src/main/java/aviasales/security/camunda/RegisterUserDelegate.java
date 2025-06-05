package aviasales.security.camunda;

import aviasales.exception.IncorrectAdminCode;
import aviasales.exception.UserAlreadyExistsException;
import aviasales.security.config.ApplicationContextProvider;
import aviasales.security.data.AuthenticationResponse;
import aviasales.security.data.RegisterRequest;
import aviasales.security.service.AuthenticationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class RegisterUserDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        AuthenticationService authService = ApplicationContextProvider.getApplicationContext().getBean(AuthenticationService.class); // Получаем Spring Bean
        String username = (String) execution.getVariable("inputUsername");
        String password = (String) execution.getVariable("inputPassword");
        String adminCode = (String) execution.getVariable("inputAdminCode");

        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .adminCode(adminCode)
                .build();
        try {
            AuthenticationResponse response = authService.register(request);
            execution.setVariable("authenticationToken", response.getToken());
            execution.setVariable("userRole", response.getRole().name());
            execution.setVariable("registrationSuccessful", true);
        } catch (UserAlreadyExistsException e) {
            execution.setVariable("registrationSuccessful", false);
            execution.setVariable("registrationError", "DUPLICATE_USERNAME");
        } catch (IncorrectAdminCode e) {
            execution.setVariable("registrationSuccessful", false);
            execution.setVariable("registrationError", "INVALID_ADMIN_CODE");
        } catch (Exception e) {
            execution.setVariable("registrationSuccessful", false);
            execution.setVariable("registrationError", "GENERAL_ERROR");
        }
    }
}
