package app.registrationSystem.services;

import app.registrationSystem.dto.request.PrescriptionRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Medication;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.Prescription;
import app.registrationSystem.jpa.repositories.PrescriptionRepository;
import app.registrationSystem.utils.PrescriptionCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class that handles operations related to Prescription entity
 */
@Service
@RequiredArgsConstructor
public class PrescriptionService {
    /**
     * Prescription expiration time in days
     */
    private static final Long EXPIRATION_TIME = 30L;
    private final PrescriptionRepository prescriptionRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final MedicationService medicationService;
    private final MailSenderService mailSenderService;

    /**
     * Allows for prescribing medications for patients by doctors
     * @param prescriptionRequest prescription containing info about the medication and the patient
     * @param doctorUsername username of the doctor who issues the prescription
     * @return Response containing info if successfully issued the prescription
     */
    @Transactional
    public Response addPrescription(PrescriptionRequest prescriptionRequest, String doctorUsername) {
        Optional<Patient> patient = patientService.getById(prescriptionRequest.getPatientID());

        if (patient.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No patient of the provided ID found: " + prescriptionRequest.getPatientID());

        Doctor doctor = doctorService.getByUsername(doctorUsername).get();

        Response response = validateMedicationRights(prescriptionRequest.getMedicationsID(), doctor);

        if (!response.success()) {
            return response;
        }

        Prescription prescription = new Prescription();

        prescription.setPatient(patient.get());
        prescription.setDoctor(doctor);
        prescription.setMedications(prescriptionRequest.getMedicationsID().stream().map(medicationService::getByID).map(Optional::get).collect(Collectors.toSet()));
        prescription.setIssueDate(LocalDateTime.now());
        prescription.setExpirationDate(LocalDateTime.now().plusDays(EXPIRATION_TIME));
        prescription.setDescription(prescriptionRequest.getDescription());
        prescription.setCode(PrescriptionCodeGenerator.generatePrescriptionCode());

        prescriptionRepository.save(prescription);

        String message = "You've got a new prescription\n" +
                "Issued by " + doctor.getUser().fullName() + '\n' +
                "Valid until " + prescription.getExpirationDate() +
                "Prescription code: " + prescription.getCode();

        mailSenderService.sendNewMail(patient.get().getUser().getEmail(), "New presscription", message);

        return new Response(true, HttpStatus.CREATED, "Correctly added a prescription");
    }

    /**
     * Returns prescriptions assigned to a patient of the provided username
     * @param username username of the patient to have prescriptions checked
     * @return list of assigned prescriptions
     */
    public List<Prescription> checkPrescriptions(String username) {
        Optional<Patient> patient = patientService.getByUsername(username);
        if (patient.isEmpty()) return Collections.emptyList();

        return patient.get().getPrescriptions();
    }

    /**
     * Returns prescriptions issued by the doctor of the provided username
     * @param username username of the doctor to have issued prescriptions checked
     * @return list of issued prescriptions
     */
    public List<Prescription> checkIssuedPrescriptions(String username) {
        Optional<Doctor> doctor = doctorService.getByUsername(username);

        if (doctor.isEmpty()) return Collections.emptyList();

        return doctor.get().getPrescriptions();
    }

    /**
     * Validates if medication of the IDs exist and if the doctor has a right to prescribe them
     * @param medications list of medication's ID
     * @param doctor Doctor who prescribes meds
     * @return Response containing info if successfully issued the prescription
     */
    private Response validateMedicationRights(List<Long> medications, Doctor doctor) {
        for (Long id : medications) {
            Optional<Medication> medication = medicationService.getByID(id);
            if (medication.isEmpty()) {
                return new Response(false, HttpStatus.NOT_FOUND, "No medication of the ID found: " + id);
            }

            if (!medication.get().getSpecializations().contains(doctor.getSpecialization())) {
                return new Response(false, HttpStatus.BAD_REQUEST, "Doctor of the provided ID " + doctor.getId() + " cannot prescribe medication " + id);
            }
        }
        return new Response(true, HttpStatus.OK, "Medication validation successful");
    }
}
