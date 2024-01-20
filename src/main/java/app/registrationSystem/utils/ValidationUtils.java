package app.registrationSystem.utils;

import app.registrationSystem.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ValidationUtils {
    private final CustomUserDetailsService customUserDetailsService;

    public boolean isUsernameUnavailable(String username) {

        try {
            customUserDetailsService.loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException e) { return false; }

    }

}
