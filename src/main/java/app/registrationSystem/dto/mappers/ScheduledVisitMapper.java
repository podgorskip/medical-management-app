package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.ScheduledVisitResponse;
import app.registrationSystem.jpa.entities.ScheduledVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScheduledVisitMapper {

    ScheduledVisitMapper INSTANCE = Mappers.getMapper(ScheduledVisitMapper.class);

    @Mapping(target = "id", source = "scheduledVisit.id")
    @Mapping(target = "patientFirstName", source = "scheduledVisit.patient.user.firstName")
    @Mapping(target = "patientLastName", source = "scheduledVisit.patient.user.lastName")
    @Mapping(target = "patientEmail", source = "scheduledVisit.patient.user.email")
    @Mapping(target = "doctorFirstName", source = "scheduledVisit.doctor.user.firstName")
    @Mapping(target = "doctorLastName", source = "scheduledVisit.doctor.user.lastName")
    @Mapping(target = "doctorEmail", source = "scheduledVisit.doctor.user.email")
    @Mapping(target = "specialization", source = "scheduledVisit.specialization.name")
    ScheduledVisitResponse convert(ScheduledVisit scheduledVisit);
}
