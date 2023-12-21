package app.registrationSystem.controllers;

import app.registrationSystem.dto.UserDTO;
import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.IllnessService;
import app.registrationSystem.services.SpecializationService;
import app.registrationSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {
    private final IllnessService illnessService;
    private final SpecializationService specializationService;
    private final UserService userService;

    @GetMapping("/illnesses")
    public List<Illness> getAllIllnesses() {
        return illnessService.getAll();
    }

    @GetMapping("/specializations")
    public List<Specialization> getAllSpecializations() {
        return specializationService.getAll();
    }

    @RequiredPrivilege(value = Privilege.CHANGE_CREDENTIALS)
    @PatchMapping("/change-credentials")
    public ResponseEntity<String> changeCredentials(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody UserDTO user) {

        Optional<Long> id = userService.changeCredentials(customUserDetails.getUsername(), user);

        if (id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Updating credentials failed");
        }

        return ResponseEntity.ok("Correctly updated credentials");
    }

//    @PatchMapping("/change-password")
}
