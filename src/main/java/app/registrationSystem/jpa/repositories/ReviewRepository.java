package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Review;
import app.registrationSystem.jpa.entities.VisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByVisitHistory(VisitHistory visitHistory);
}
