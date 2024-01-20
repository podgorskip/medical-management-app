package app.registrationSystem.services;

import app.registrationSystem.dto.request.QuestionRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.Question;
import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.jpa.repositories.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A class that handles operations related to Question entity
 */
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final SpecializationService specializationService;
    private final MailSenderService mailSenderService;

    /**
     * Retrieves questions by ID
     * @param id id of the question
     * @return Question if there is a question of the provided ID, empty otherwise
     */
    public Optional<Question> getById(Long id) {
        return questionRepository.findById(id);
    }

    /**
     * Retrieves questions by a specialization
     * @param specialization Specialization instance
     * @return list of Questions if there exist for the specified specialization, empty otherwise
     */
    public Optional<List<Question>> getBySpecialization(Specialization specialization) {
        return questionRepository.findBySpecialization(specialization);
    }

    /**
     * Allows to ask a question which is then assigned to a doctor with the least number of questions waiting to answer
     * @param questionRequest question request containing info about question
     * @param username username of the patient who asked the question
     * @return Response containing info if successfully submitted the question
     */
    @Transactional
    public Response askQuestion(QuestionRequest questionRequest, String username) {
        Optional<Specialization> specialization = specializationService.getById(questionRequest.getSpecializationID());

        if (specialization.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No specialization found");

        Optional<Patient> patient = patientService.getByUsername(username);

        if (patient.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No patient of the username found");

        Question question = new Question();
        question.setPatient(patient.get());
        question.setQuestion(questionRequest.getQuestion());
        question.setQuestionDate(LocalDateTime.now());
        question.setSpecialization(specialization.get());

        questionRepository.save(question);

        Response response = assignQuestion(question);

        if (!response.success())
            return response;

        return assignQuestion(question);
    }

    /**
     * Retrieves questions waiting to be answered by a doctor
     * @param username username of the doctor to have checked waiting questions
     * @return list of Questions to be answered if present, empty otherwise
     */
    public Optional<List<Question>> getQuestionsToAnswer(String username) {
        return questionRepository.findUnansweredQuestionsForDoctor(doctorService.getByUsername(username).get());
    }

    /**
     * Retrieves questions asked by a patient
     * @param username username of the patient to have asked questions checked
     * @return list of Questions if present, empty otherwise
     */
    public Optional<List<Question>> getAskedQuestions(String username) {
        return questionRepository.findByPatient(patientService.getByUsername(username).get());
    }

    /**
     * Allows to assign a question to the doctor of the demanded specialization with the least number of questions waiting
     * @param question Question to be assigned
     * @return Response is successfully assigned the question
     */
    private Response assignQuestion(Question question) {
        Optional<List<Doctor>> doctors = doctorService.getBySpecialization(question.getSpecialization());

        if (doctors.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No doctors of the provided specialization found");

        sendQuestionNotification(doctorService.getWithLeastQuestionsAssigned(question.getSpecialization()).get());

        return new Response(true, HttpStatus.CREATED, "Correctly submitted the question");
    }

    /**
     * Auxiliary method for notifying via email when a new question is asked
     * @param doctor Doctor who should have the notification sent
     */
    private void sendQuestionNotification(Doctor doctor) {
        String message = "You've got a new question.\nCheck out the panel to answer.";

        mailSenderService.sendNewMail(doctor.getUser().getEmail(), "New question", message);
    }
}
