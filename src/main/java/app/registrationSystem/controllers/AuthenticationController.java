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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Map<String,String>> authenticate(@RequestBody @Valid AuthenticationRequest authenticationDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password()));

        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(authenticationDTO.username());

        if (Objects.nonNull(customUserDetails)) {
            Map<String, String> map = new HashMap<>();
            map.put("token", jwtUtils.generateToken(authenticationDTO.username()));
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(map);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Response> register(@Valid @RequestBody PatientRegistrationRequest patientDTO) {
        Response response = patientService.addPatient(patientDTO);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

}
