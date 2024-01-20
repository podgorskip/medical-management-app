package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.AnswerResponse;
import app.registrationSystem.jpa.entities.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.time.format.DateTimeFormatter;

@Mapper
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    default AnswerResponse convert(Answer answer) {
        AnswerResponse answerResponse = new AnswerResponse();

        answerResponse.setDoctor(answer.getDoctor().getUser().fullName());
        answerResponse.setQuestion(answer.getQuestion().getQuestion());
        answerResponse.setDate(answer.getAnswerDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
        answerResponse.setAnswer(answer.getAnswer());
        answerResponse.setSpecialization(answer.getDoctor().getSpecialization().getName());

        return answerResponse;
    }
}
