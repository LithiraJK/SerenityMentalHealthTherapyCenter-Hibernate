package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDTO;

import java.sql.SQLException;
import java.util.List;

public interface PatientsBO extends SuperBO {
    boolean savePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException;

    List<PatientDTO> getAllPatients() throws SQLException, ClassNotFoundException;

    boolean deletePatient(String id) throws SQLException, ClassNotFoundException;

    boolean UpdatePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException;


}
