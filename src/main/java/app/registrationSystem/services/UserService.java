package app.registrationSystem.services;

import app.registrationSystem.dto.request.PasswordChangeRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.dto.request.UserRegistrationRequest;
import app.registrationSystem.jpa.entities.User;
import app.registrationSystem.jpa.repositories.UserRepository;
import app.registrationSystem.security.Role;
import app.registrationSystem.utils.ValidationUtils;
import app.registrationSystem.utils.VerificationCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

/**
 * A class that handles operations related to User entity
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtils validationUtils;
    private final MailSenderService mailSenderService;

    /**
     * Allows to create a new User
     * @param userRequest request with the info about a user to be created
     * @param role Role of the user
     * @return user ID if successfully added a user, empty otherwise
     */
    @Transactional
    public Optional<User> createUser(UserRegistrationRequest userRequest, Role role) {

        if (validationUtils.isUsernameUnavailable(userRequest.getUsername())) {
            return Optional.empty();
        }

        User user = new User();

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(role);

        return Optional.of(userRepository.save(user));
    }

    /**
     * Removes a user
     * @param user user to be removed
     */
    @Transactional
    public void removeUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Retrieves a user by they username
     * @param username username of the user
     * @return User if present, empty otherwise
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Changes the user's credentials
     * @param username username of the account to have the credentials changed
     * @param userDTO DTO containing new credentials
     * @return ID of the updated account if successful
     */
    @Transactional
    public Response changeCredentials(String username, UserRegistrationRequest userDTO) {
        User user = userRepository.findByUsername(username).get();

        if (Objects.nonNull(userDTO.getFirstName())) { user.setFirstName(userDTO.getFirstName()); }
        if (Objects.nonNull(userDTO.getLastName())) { user.setLastName(userDTO.getLastName()); }
        if (Objects.nonNull(userDTO.getEmail())) { user.setEmail(userDTO.getEmail()); }
        if (Objects.nonNull(userDTO.getPhoneNumber())) { user.setPhoneNumber(userDTO.getPhoneNumber()); }
        if (Objects.nonNull(userDTO.getUsername())) {
            if (validationUtils.isUsernameUnavailable(userDTO.getUsername())) { return new Response(false, HttpStatus.CONFLICT, "Provided username is already taken"); }
            else { user.setUsername(userDTO.getUsername()); }
        }

        userRepository.save(user);

        return new Response(true, HttpStatus.OK,"Successfully updated credentials");
    }

    /**
     * Allows to change a user's password
     * @param username username of the user
     * @param passwordChangeRequest object containing the old password and a new password
     * @return Response if successfully changed the password
     */
    @Transactional
    public Response changePassword(String username, PasswordChangeRequest passwordChangeRequest) {
        User user = findByUsername(username).get();

        if (!passwordEncoder.matches(passwordChangeRequest.oldPassword(), user.getPassword())) {
            return new Response(false, HttpStatus.CONFLICT, "Provided password doesn't match the current one");
        }

        if (passwordEncoder.matches(passwordChangeRequest.newPassword(), user.getPassword())) {
            return new Response(false, HttpStatus.CONFLICT,"New password cannot be the same as the old one");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.newPassword()));
        userRepository.save(user);

        return new Response(true, HttpStatus.OK, "Successfully updated the password");
    }

    @Transactional
    public Response requestVerification(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No user of the provided ID found");

        sendVerificationCode(user.get());

        return new Response(true, HttpStatus.OK, "Correctly send the verification email");
    }

    /**
     * Allows to send an email with the account verification code
     * @param user User who requests the verification
     */
    private void sendVerificationCode(User user) {
        String message = "Account verification has been requested.\n"
                + "Enter verification code: " + VerificationCodeGenerator.generateVerificationCode();

        mailSenderService.sendNewMail(user.getEmail(), "Account verification", message);
    }

}
