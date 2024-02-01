package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.VisitHistoryResponse;
import app.registrationSystem.jpa.entities.ScheduledVisit;
import app.registrationSystem.jpa.entities.VisitHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VisitHistoryMapper {
    VisitHistoryMapper INSTANCE = Mappers.getMapper(VisitHistoryMapper.class);

    default VisitHistoryResponse convert(VisitHistory visitHistory) {
        VisitHistoryResponse visitHistoryResponse = new VisitHistoryResponse();

        visitHistoryResponse.setId(visitHistory.getId());
        visitHistoryResponse.setPatient(visitHistory.getPatient().getUser().fullName());
        visitHistoryResponse.setDoctor(visitHistory.getDoctor().getUser().fullName());
        visitHistoryResponse.setSpecialization(visitHistory.getSpecialization().getName());
        visitHistoryResponse.setDate(visitHistory.getDate());

        return visitHistoryResponse;
    }

    default VisitHistoryResponse convert(ScheduledVisit scheduledVisit) {
        VisitHistoryResponse visitHistoryResponse = new VisitHistoryResponse();

        visitHistoryResponse.setId(scheduledVisit.getId());
        visitHistoryResponse.setPatient(scheduledVisit.getPatient().getUser().fullName());
        visitHistoryResponse.setDoctor(scheduledVisit.getDoctor().getUser().fullName());
        visitHistoryResponse.setSpecialization(scheduledVisit.getSpecialization().getName());
        visitHistoryResponse.setDate(scheduledVisit.getDate());

        return visitHistoryResponse;
    }
}
