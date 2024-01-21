package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.User;
import app.registrationSystem.jpa.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByUser(User user);
}
