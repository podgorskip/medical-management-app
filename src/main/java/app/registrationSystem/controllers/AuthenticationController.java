package app.registrationSystem.controllers;

import app.registrationSystem.dto.request.AuthenticationRequest;
import app.registrationSystem.dto.request.PatientRegistrationRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.CustomUserDetailsService;
import app.registrationSystem.security.JwtUtils;
import app.registrationSystem.services.PatientService;
import jakarta.validation.Valid;
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
    private final PatientService patientService;
    private final JwtUtils jwtUtils;

    @PostMapping("/auth/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthenticationRequest authenticationDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password()));

        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(authenticationDTO.username());

        if (Objects.nonNull(customUserDetails)) {
            return ResponseEntity.ok(jwtUtils.generateToken(authenticationDTO.username()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication unsuccessful");
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Response> register(@Valid @RequestBody PatientRegistrationRequest patientDTO) {
        Response response = patientService.addPatient(patientDTO);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

}
