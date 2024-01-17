package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.Question;
import app.registrationSystem.jpa.entities.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<List<Question>> findBySpecialization(Specialization specialization);

    @Query("SELECT q FROM Question q " +
            "LEFT JOIN q.answer a " +
            "WHERE a IS NULL OR a.doctor = :doctor")
    Optional<List<Question>> findUnansweredQuestionsForDoctor(@Param("doctor") Doctor doctor);

    Optional<List<Question>> findByPatient(Patient patient);
}
