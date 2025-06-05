package aviasales.security.camunda;

import aviasales.security.config.ApplicationContextProvider;
import aviasales.security.data.AuthenticationRequest;
import aviasales.security.data.AuthenticationResponse;
import aviasales.security.service.AuthenticationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthenticateUserDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        AuthenticationService authService = ApplicationContextProvider.getApplicationContext().getBean(AuthenticationService.class);
        String username = (String) execution.getVariable("inputUsername");
        String password = (String) execution.getVariable("inputPassword");

        AuthenticationRequest request = new AuthenticationRequest(username, password);
        try {
            AuthenticationResponse response = authService.authenticate(request);
            execution.setVariable("authenticationToken", response.getToken());
            execution.setVariable("userRole", response.getRole().name());
            execution.setVariable("authenticationSuccessful", true);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            execution.setVariable("authenticationSuccessful", false);
            execution.setVariable("authenticationError", "INVALID_CREDENTIALS");
        } catch (Exception e) {
            execution.setVariable("authenticationSuccessful", false);
            execution.setVariable("authenticationError", "GENERAL_ERROR");
        }
    }
}
