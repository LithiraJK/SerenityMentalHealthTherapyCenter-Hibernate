package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;

import java.util.ArrayList;
import java.util.Optional;


public interface PatientBO extends SuperBO {

    public boolean savePatient(PatientDto dto);
    public boolean updatePatient(PatientDto dto);
    public boolean deletePatient(String pk);
    public ArrayList<PatientDto> getAllPatients();
    public ArrayList<PatientDto> findByPatientName(String name);
    public String getNextPatientPK();
    public PatientDto findPatientByID(String id);


}
