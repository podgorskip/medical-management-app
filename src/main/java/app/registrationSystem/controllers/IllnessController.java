package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.IllnessMapper;
import app.registrationSystem.dto.request.IllnessesRequest;
import app.registrationSystem.dto.response.IllnessResponse;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.IllnessService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IllnessController {
    private final IllnessService illnessService;

    @RequiredPrivilege(value = Privilege.ADD_ILLNESS)
    @PostMapping("/patient/add-illnesses/{username}")
    public ResponseEntity<Response> addIllnesses(@NotNull @PathVariable("username") String username, @NotNull @RequestBody IllnessesRequest illnesses) {
        Response response = illnessService.assignIllnessesToPatient(username, illnesses.getIllnesses());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.CHECK_ASSIGNED_ILLNESSES)
    @GetMapping("/patient/my-illnesses")
    public ResponseEntity<Set<IllnessResponse>> checkAssignedIllnesses(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<Set<Illness>> illnesses = illnessService.checkAssignedIllnesses(customUserDetails.getUsername());

        if (illnesses.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(illnesses.get().stream().map(IllnessMapper.INSTANCE::convert).collect(Collectors.toSet()));
    }
}
