package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.PrescriptionResponse;
import app.registrationSystem.jpa.entities.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PrescriptionMapper {
    PrescriptionMapper INSTANCE = Mappers.getMapper(PrescriptionMapper.class);

    default PrescriptionResponse convert(Prescription prescription) {
        PrescriptionResponse prescriptionResponse = new PrescriptionResponse();

        prescriptionResponse.setPatient(prescription.getPatient().getUser().fullName());
        prescriptionResponse.setMedications(prescription.getMedications()
                .stream().map(medication -> medication.getName() + "[" + medication.getDose() + "]").toList());
        prescriptionResponse.setCode(prescription.getCode());
        prescriptionResponse.setDescription(prescription.getDescription());
        prescriptionResponse.setIssueDate(prescription.getIssueDate());
        prescriptionResponse.setExpirationDate(prescription.getExpirationDate());
        prescriptionResponse.setDoctor(prescription.getDoctor().getUser().fullName());

        return prescriptionResponse;
    }

}
