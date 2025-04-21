package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;

import java.util.ArrayList;

public interface PatientProgramBO {

    public boolean savePatientProgram(PatientProgramDto dto);
    public boolean updatePatientProgram(PatientProgramDto dto);
    public boolean deletePatientProgram(String patientName, String programName);
    public ArrayList<PatientProgramDto> getAllPatientPrograms();
    public PatientDto findByPatientName(String patientName);
    public TherapyProgramDto findByProgramName(String programName);
    public ArrayList<PatientProgramDto> search(String name, boolean isPatient);
    public String getNextPatientProgramPK();
}
