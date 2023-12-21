package app.registrationSystem.controllers;

import app.registrationSystem.dto.PasswordChangeRequest;
import app.registrationSystem.dto.UpdateResponse;
import app.registrationSystem.dto.UserDTO;
import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.IllnessService;
import app.registrationSystem.services.SpecializationService;
import app.registrationSystem.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<UpdateResponse> changeCredentials(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody UserDTO user) {

        UpdateResponse updateResponse = userService.changeCredentials(customUserDetails.getUsername(), user);

        if (!updateResponse.success()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateResponse);
        }

        return ResponseEntity.ok(updateResponse);
    }

    @RequiredPrivilege(value = Privilege.CHANGE_CREDENTIALS)
    @PatchMapping("/change-password")
    public ResponseEntity<UpdateResponse> changePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {

        UpdateResponse updateResponse = userService.changePassword(customUserDetails.getUsername(), passwordChangeRequest);

        if (!updateResponse.success()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateResponse);
        }

        return ResponseEntity.ok(updateResponse);
    }

}
