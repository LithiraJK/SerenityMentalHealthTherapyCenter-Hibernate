package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PatientsBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.DAOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PatientsDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientsBOImpl implements PatientsBO {

    private final PatientsDAO patientsDAO = (PatientsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public boolean savePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException {
        Patient patient = new Patient();
        patient.setName(patientDTO.getName());
        patient.setAddress(patientDTO.getAddress());
        patient.setGender(patientDTO.getGender());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setEmail(patientDTO.getEmail());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        return patientsDAO.save(patient);
    }

    @Override
    public List<PatientDTO> getAllPatients() throws SQLException, ClassNotFoundException {
        ArrayList<PatientDTO> patientDTOArrayList = new ArrayList<>();
        ArrayList<Patient> patients = patientsDAO.getAllData();

        for (Patient patient : patients) {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setPatientId(String.valueOf(patient.getPatientId()));
            patientDTO.setName(patient.getName());
            patientDTO.setAddress(patient.getAddress());
            patientDTO.setGender(patient.getGender());
            patientDTO.setDateOfBirth(patient.getDateOfBirth());
            patientDTO.setEmail(patient.getEmail());
            patientDTO.setPhoneNumber(patient.getPhoneNumber());
            patientDTOArrayList.add(patientDTO);
        }
        return patientDTOArrayList;
    }

    @Override
    public boolean deletePatient(String id) throws SQLException, ClassNotFoundException {
        Patient patient = new Patient();
        patient.setPatientId(Integer.parseInt(id));
        return patientsDAO.delete(patient);
    }

    @Override
    public boolean UpdatePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException {
        Patient patient = new Patient();
        patient.setPatientId(Integer.parseInt(patientDTO.getPatientId()));
        patient.setName(patientDTO.getName());
        patient.setAddress(patientDTO.getAddress());
        patient.setGender(patientDTO.getGender());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setEmail(patientDTO.getEmail());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        return patientsDAO.update(patient);
    }
}