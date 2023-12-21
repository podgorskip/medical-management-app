package app.registrationSystem.services;

import app.registrationSystem.dto.DoctorDTO;
import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.User;
import app.registrationSystem.jpa.repositories.DoctorRepository;
import app.registrationSystem.security.Role;
import app.registrationSystem.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final SpecializationService specializationService;
    private final ValidationUtils validationUtils;

    /**
     * Retrieves doctors by their ID
     * @param id ID of the doctor
     * @return Doctor object if successful
     */
    public Optional<Doctor> getById(Long id) {
        return doctorRepository.findById(id);
    }

    /**
     * Removes the doctor's account
     * @param id ID of the doctor to have the account removed
     * @return ID of the removed account if successful
     */
    public Optional<Long> removeDoctor(Long id) {
        Optional<Doctor> doctor = getById(id);

        if (doctor.isEmpty()) {
            return Optional.empty();
        }

        userService.removeUser(doctor.get().getUser());
        doctorRepository.delete(doctor.get());

        return Optional.of(id);
    }

    /**
     * Creates a new doctor account
     * @param doctorDTO DTO containing info about the doctor
     * @return doctor ID if added successfully
     */
    public Optional<Long> addDoctor(DoctorDTO doctorDTO) {

        Optional<User> user = userService.createUser(doctorDTO, Role.DOCTOR);

        if (user.isEmpty()) return Optional.empty();

        Doctor doctor = new Doctor();
        doctor.setUser(user.get());

        if (specializationService.getById(doctorDTO.getSpecialization()).isPresent()) {
            doctor.setSpecialization(specializationService.getById(doctorDTO.getSpecialization()).get());
        }

        return Optional.of(doctorRepository.save(doctor).getId());
    }

    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }
}
