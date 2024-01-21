package app.registrationSystem.services;

import app.registrationSystem.dto.request.VerificationRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.User;
import app.registrationSystem.jpa.entities.VerificationCode;
import app.registrationSystem.jpa.repositories.VerificationCodeRepository;
import app.registrationSystem.utils.VerificationCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {
    @Value("${verification.code}")
    private Long codeExpirationTime;

    private final UserService userService;
    private final MailSenderService mailSenderService;
    private final VerificationCodeRepository verificationCodeRepository;

    /**
     * Allows to request the account verification
     * @param username username of the account to verify
     * @return Response if successfully sent the code
     */
    public Response requestVerification(String username) {
        Optional<User> user = userService.findByUsername(username);

        if (user.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No user of the provided ID found");

        sendVerificationCode(user.get(), generateCode(user.get()));

        return new Response(true, HttpStatus.OK, "Correctly send the verification email");
    }

    /**
     * Validates the verification code sent via email
     * @param username username of the account to be verified
     * @param verificationRequest verification code
     * @return Response if successfully verified the account
     */
    public Response validateVerificationCode(String username, VerificationRequest verificationRequest) {
        Optional<User> user = userService.findByUsername(username);

        if (user.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No user of the provided username found");

        Optional<VerificationCode> verificationCode = verificationCodeRepository.findByUser(user.get());

        if (verificationCode.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "Verification hasn't been requested");

        if (!isVerificationCodeValid(user.get(), verificationCode.get(), verificationRequest.getCode()))
            return new Response(false, HttpStatus.NOT_FOUND, "Verification failed");

        marKVerified(user.get(), verificationCode.get());

        return new Response(true, HttpStatus.OK, "Successfully verified the account");
    }

    /**
     * Allows to send an email with the account verification code
     * @param user User who requests the verification
     */
    private void sendVerificationCode(User user, String code) {
        String message = "Account verification has been requested.\n"
                + "Enter verification code: " + code;

        mailSenderService.sendNewMail(user.getEmail(), "Account verification", message);
    }

    /**
     * Generates the verification code
     * @param user User who requested the verification
     * @return verification code
     */
    private String generateCode(User user) {
        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setUser(user);
        verificationCode.setCode(VerificationCodeGenerator.generateVerificationCode());
        verificationCode.setIssueDate(LocalDateTime.now());

        verificationCodeRepository.save(verificationCode);

        return verificationCode.getCode();
    }

    /**
     * Checks if the input verification code matches the one generated and if it is not expired
     * @param user User who requested the verification
     * @param verificationCode verification code saved in the database
     * @param code input verification code
     * @return true is successfully verified, false otherwise
     */
    private boolean isVerificationCodeValid(User user, VerificationCode verificationCode, String code) {
        return verificationCode.getIssueDate().until(LocalDateTime.now(), ChronoUnit.MILLIS) < codeExpirationTime && verificationCode.getCode().equals(code);
    }

    /**
     * Marks the user as verified and removes the verification code from database
     * @param user User who has been verified
     * @param verificationCode verification code saved in the database
     */
    private void marKVerified(User user, VerificationCode verificationCode) {
        user.setVerified(true);
        verificationCodeRepository.delete(verificationCode);
    }


}
