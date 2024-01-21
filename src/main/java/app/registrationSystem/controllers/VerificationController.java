package app.registrationSystem.controllers;

import app.registrationSystem.dto.request.VerificationRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VerificationController {
    private final VerificationService verificationService;

    @RequiredPrivilege(value = Privilege.VERIFY_ACCOUNT)
    @GetMapping("/request-verification")
    public ResponseEntity<Response> requestVerification(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Response response = verificationService.requestVerification(customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.VERIFY_ACCOUNT)
    @PostMapping("/verify")
    public ResponseEntity<Response> verify(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody VerificationRequest verificationRequest) {
        Response response = verificationService.validateVerificationCode(customUserDetails.getUsername(), verificationRequest);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }


}
