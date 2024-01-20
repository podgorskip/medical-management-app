package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Optional<Specialization> findByName(String name);
}
