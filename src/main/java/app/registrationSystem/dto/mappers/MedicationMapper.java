package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.MedicationResponse;
import app.registrationSystem.jpa.entities.Medication;
import app.registrationSystem.jpa.entities.Specialization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface MedicationMapper {
    MedicationMapper INSTANCE = Mappers.getMapper(MedicationMapper.class);

    @Mapping(target = "specializations", expression = "java(mapSpecializations(medication.getSpecializations()))")
    MedicationResponse convert(Medication medication);

    default List<String> mapSpecializations(Set<Specialization> specializations) {
        return specializations.stream()
                .map(Specialization::getName)
                .collect(Collectors.toList());
    }
}
