package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.DoctorResponse;
import app.registrationSystem.jpa.entities.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "id", source = "doctor.id")
    @Mapping(target = "firstName", source = "doctor.user.firstName")
    @Mapping(target = "lastName", source = "doctor.user.lastName")
    @Mapping(target = "email", source = "doctor.user.email")
    @Mapping(target = "specialization", source = "doctor.specialization.name")
    DoctorResponse convert(Doctor doctor);
}
