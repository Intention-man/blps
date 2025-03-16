package com.example.prac.service.auth;

import com.example.prac.DTO.auth.AuthenticationRequest;
import com.example.prac.DTO.auth.AuthenticationResponse;
import com.example.prac.DTO.auth.RegisterRequest;
import com.example.prac.errorHandler.UserAlreadyExistsException;
import com.example.prac.model.authEntity.Role;
import com.example.prac.model.authEntity.User;
import com.example.prac.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        boolean userExists = userRepository.existsByUsername(request.getUsername());

        if (userExists) {
            throw new UserAlreadyExistsException("A user with the same username already exists");
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var token = jwtService.generateToken(user);
        Role role = user.getRole();
        return AuthenticationResponse.builder()
                .token(token)
                .role(role)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var token = jwtService.generateToken(user);
        Role role = user.getRole();

        return AuthenticationResponse.builder()
                .token(token)
                .role(role)
                .build();
    }

    public boolean isTokenValid(String token) {
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);

        if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            return jwtService.isTokenValid(jwt, userDetails);
        }
        return false;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}