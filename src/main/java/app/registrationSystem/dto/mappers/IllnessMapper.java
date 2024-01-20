package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.IllnessResponse;
import app.registrationSystem.jpa.entities.Illness;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IllnessMapper {

    IllnessMapper INSTANCE = Mappers.getMapper(IllnessMapper.class);

    @Mapping(target = "specialization", source = "illness.specialization.name")
    IllnessResponse convert(Illness illness);
}
