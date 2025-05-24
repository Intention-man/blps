package aviasales.security.config;

import jakarta.xml.bind.JAXBException;
import org.springframework.security.crypto.password.PasswordEncoder;
import aviasales.security.data.User;
import aviasales.security.service.UserXmlService;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class XmlLoginModule implements LoginModule {
    private Subject subject;
    private CallbackHandler callbackHandler;
    private User authenticatedUser;
    private UserXmlService userXmlService;
    private boolean loginSucceeded;
    private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.userXmlService = (UserXmlService) options.get("userXmlService");
        this.passwordEncoder = (PasswordEncoder) options.get("passwordEncoder");
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCallback = new NameCallback("Username:");
        PasswordCallback passwordCallback = new PasswordCallback("Password:", false);

        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            String username = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());

            Optional<User> user = userXmlService.findByUsername(username);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                this.authenticatedUser = user.get();
                this.loginSucceeded = true;
                return true;
            }
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException("Authentication failed: " + e.getMessage());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean commit() {
        if (loginSucceeded && authenticatedUser != null) {
            subject.getPrincipals().add(authenticatedUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean abort() {
        logout();
        return true;
    }

    @Override
    public boolean logout() {
        subject.getPrincipals().remove(authenticatedUser);
        authenticatedUser = null;
        loginSucceeded = false;
        return true;
    }
}
