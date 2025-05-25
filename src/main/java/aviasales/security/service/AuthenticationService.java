package aviasales.security.service;


import aviasales.exception.IncorrectAdminCode;
import aviasales.exception.UserAlreadyExistsException;
import aviasales.security.data.*;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserXmlService userXmlService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationProvider jaasAuthenticationProvider;

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            if (userXmlService.findByUsername(request.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException("A user with the same username already exists");
            }

            Role role;
            if (request.getAdminCode() == null) {
                role = Role.USER;
            } else if (request.getAdminCode().equals(jwtService.SECRET)) {
                role = Role.ADMIN;
            } else {
                throw new IncorrectAdminCode("Invalid admin code");
            }

            var user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .build();

            userXmlService.saveUser(user);

            var token = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(token)
                    .role(role)
                    .build();
        } catch (JAXBException e) {
            throw new RuntimeException("Error accessing XML storage", e);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            jaasAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            var user = userXmlService.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var token = jwtService.generateToken(user);
            Role role = user.getRole();

            return AuthenticationResponse.builder()
                    .token(token)
                    .role(role)
                    .build();
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found", e);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        } catch (JAXBException e) {
            throw new RuntimeException("Error accessing XML storage", e);
        }
    }
}
