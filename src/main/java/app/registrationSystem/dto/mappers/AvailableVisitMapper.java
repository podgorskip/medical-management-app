package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.AvailableVisitResponse;
import app.registrationSystem.jpa.entities.AvailableVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Map;

@Mapper
public interface AvailableVisitMapper {

    AvailableVisitMapper INSTANCE = Mappers.getMapper(AvailableVisitMapper.class);

    @Mapping(target = "id", source = "availableVisit.id")
    @Mapping(target = "doctorFirstName", source = "availableVisit.doctor.user.firstName")
    @Mapping(target = "doctorLastName", source = "availableVisit.doctor.user.lastName")
    @Mapping(target = "doctorEmail", source = "availableVisit.doctor.user.email")
    @Mapping(target = "specialization", source = "availableVisit.doctor.specialization.name")
    AvailableVisitResponse convert(AvailableVisit availableVisit);

    Map<String, List<AvailableVisitResponse>> mapToResponseMap(Map<String, List<AvailableVisit>> input);

    List<AvailableVisitResponse> mapToResponseList(List<AvailableVisit> input);
}
