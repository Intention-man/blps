package com.example.prac.config;

import com.example.prac.data.auth.User;
import com.example.prac.service.UserXmlService;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.jaas.AuthorityGranter;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
public class JaasAuthorityGranter implements AuthorityGranter {
    private final UserXmlService userXmlService;

    @Override
    public Set<String> grant(Principal principal) {
        try {
            if (principal instanceof User) {
                return Collections.singleton(((User) principal).getRole().name());
            }

            return userXmlService.findByUsername(principal.getName())
                    .map(user -> Collections.singleton(user.getRole().name()))
                    .orElse(Collections.emptySet());

        } catch (JAXBException e) {
            throw new RuntimeException("Error accessing user data", e);
        }
    }
}
