package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgram;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PatientProgramBO extends SuperBO {

    public boolean savePatientProgram(PatientProgramDto dto);
    public boolean updatePatientProgram(PatientProgramDto dto);
    public boolean deletePatientProgram(String patientName, String programName);
    public ArrayList<PatientProgramDto> getAllPatientPrograms();
    public PatientDto findByPatientName(String patientName);
    public TherapyProgramDto findByProgramName(String programName);
    public ArrayList<PatientProgramDto> search(String name, boolean isPatient);
    public String getNextPatientProgramPK();

    public List<PatientProgramDto> getProgramsByPatientId(String patientId);
    public List<PatientProgramDto> getPatientsByProgramId(String programId);

    public boolean updateTherapyProgramFeeOfPatient(String patientId, String programId, BigDecimal newFee);
    public PatientProgramDto searchPatientProgramFromBothIds(String patientId, String programId);

}
