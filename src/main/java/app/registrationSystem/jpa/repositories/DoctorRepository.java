package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.jpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    Optional<List<Doctor>> findBySpecialization(Specialization specialization);
}
