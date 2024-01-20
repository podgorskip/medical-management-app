package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.jpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    Optional<List<Doctor>> findBySpecialization(Specialization specialization);

    @Query("SELECT d FROM Doctor d " +
            "LEFT JOIN d.answers a " +
            "WHERE d.specialization = :specialization " +
            "GROUP BY d.id " +
            "ORDER BY COUNT(a) ASC")
    Optional<Doctor> findDoctorWithLeastQuestionsInSpecialization(@Param("specialization") Specialization specialization);
}
