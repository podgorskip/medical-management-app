package app.registrationSystem.controllers;

import app.registrationSystem.dto.AuthenticationDTO;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.CustomUserDetailsService;
import app.registrationSystem.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @PostMapping("/auth/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationDTO authenticationDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password()));

        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(authenticationDTO.username());

        if (Objects.nonNull(customUserDetails)) {
            return ResponseEntity.ok(jwtUtils.generateToken(authenticationDTO.username()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication unsuccessful");
    }

}
