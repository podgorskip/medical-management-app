package app.registrationSystem.services;

import app.registrationSystem.dto.request.AnswerRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Answer;
import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.Question;
import app.registrationSystem.jpa.repositories.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * A class that handles operations related to Answer entity
 */
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final DoctorService doctorService;
    private final MailSenderService mailSenderService;
    private final PatientService patientService;

    /**
     * Allows to answer a specified question
     * @param id id of the question to be answered
     * @param username username of a doctor to answer the question
     * @param answerRequest answer body
     * @return Response containing info if action was performed successfully
     */
    public Response answerQuestion(Long id, String username, AnswerRequest answerRequest) {
        Optional<Question> question = questionService.getById(id);

        if (question.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No question of the provided id found");

        Doctor doctor = doctorService.getByUsername(username).get();

        if (!question.get().getSpecialization().equals(doctor.getSpecialization()))
            return new Response(false, HttpStatus.BAD_REQUEST, "Doctor's and question's specialization doesn't match");

        Answer answer = createAnswer(doctor, question.get(), answerRequest.getAnswer());
        answerRepository.save(answer);

        sendAnswerNotification(question.get(), answer);

        return new Response(true, HttpStatus.OK, "Correctly submitted the answer");
    }

    /**
     * Allows to retrieve all answers for questions asked by the patient of the provided username
     * @param username username of the patient to have answers checked
     * @return list of answers if present, empty otherwise
     */
    public Optional<List<Answer>> getAnswers(String username) {
        Optional<Patient> patient = patientService.getByUsername(username);

        if (patient.isEmpty())
            return Optional.empty();

        return answerRepository.findByPatient(patient.get());
    }

    /**
     * Auxiliary method for answers creation
     * @param doctor Doctor instance of who answers the question
     * @param question Question instance for which the answers is created
     * @param answerRequest body of the answer
     * @return fully populated Answer instance
     */
    private Answer createAnswer(Doctor doctor, Question question, String answerRequest) {
        Answer answer = new Answer();

        answer.setAnswer(answerRequest);
        answer.setDoctor(doctor);
        answer.setAnswerDate(LocalDateTime.now());
        answer.setQuestion(question);

        return answer;
    }

    /**
     * Auxiliary method for notifying via email when the question is answered
     * @param question question that was asked
     * @param answer answer which were given
     */
    private void sendAnswerNotification(Question question, Answer answer) {
        String message = "You've got a new answer from a specialist " + answer.getDoctor().getUser().fullName() + "\n" +
                "regarding the question: " + question.getQuestion() + " from " + question.getQuestionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));

        mailSenderService.sendNewMail(question.getPatient().getUser().getEmail(), "New answer", message);
    }
}
