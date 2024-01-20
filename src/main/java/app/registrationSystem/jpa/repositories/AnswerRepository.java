package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Answer;
import app.registrationSystem.jpa.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a " +
            "LEFT JOIN a.question q " +
            "WHERE q.patient = :patient")
    Optional<List<Answer>> findByPatient(@Param("patient") Patient patient);
}
