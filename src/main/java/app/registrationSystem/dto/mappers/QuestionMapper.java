package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.QuestionResponse;
import app.registrationSystem.jpa.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.time.format.DateTimeFormatter;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    default QuestionResponse convert(Question question) {
        QuestionResponse questionResponse = new QuestionResponse();

        questionResponse.setId(question.getId());
        questionResponse.setPatient(question.getPatient().getUser().fullName());
        questionResponse.setQuestion(question.getQuestion());
        questionResponse.setSpecialization(question.getSpecialization().getName());
        questionResponse.setDate(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm").format(question.getQuestionDate()));
        return questionResponse;
    }
}
