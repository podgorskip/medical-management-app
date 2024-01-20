package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Illness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IllnessRepository extends JpaRepository<Illness, Long> {
    Optional<Illness> findByName(String name);
}
