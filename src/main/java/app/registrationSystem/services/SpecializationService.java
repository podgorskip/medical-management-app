package app.registrationSystem.services;

import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.jpa.repositories.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecializationService {
    private final SpecializationRepository specializationRepository;

    public List<Specialization> getAll() {
        return specializationRepository.findAll();
    }

    public Optional<Specialization> getById(Long id) {
        return specializationRepository.findById(id);
    }

    public Optional<Specialization> getByName(String name) {
        return specializationRepository.findByName(name);
    }
}
