package app.registrationSystem.services;

import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.repositories.IllnessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IllnessService {
    private final IllnessRepository illnessRepository;

    public List<Illness> getAll() {
        return illnessRepository.findAll();
    }

    public Optional<Illness> getByName(String illnessName) {
        return illnessRepository.findByName(illnessName);
    }

    public Optional<Illness> getById(Long id) {
        return illnessRepository.findById(id);
    }
}
