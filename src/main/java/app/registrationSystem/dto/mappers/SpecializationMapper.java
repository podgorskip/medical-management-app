package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.SpecializationResponse;
import app.registrationSystem.jpa.entities.Specialization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpecializationMapper {

    SpecializationMapper INSTANCE = Mappers.getMapper(SpecializationMapper.class);

    SpecializationResponse convert(Specialization specialization);
}
