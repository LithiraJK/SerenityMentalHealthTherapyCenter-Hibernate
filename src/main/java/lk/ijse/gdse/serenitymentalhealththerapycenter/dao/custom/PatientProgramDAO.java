package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgram;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface PatientProgramDAO {

    public boolean save(PatientProgram entity);
    public boolean update(PatientProgram entity);
    public boolean delete(String patientId, String programId);
    public List<PatientProgram> getAll();
    public List<PatientProgram> findByName(String name);
    public Optional<String> getLastPK();
    public List<PatientProgram> findByPatientId(String id);
    public List<PatientProgram> findByProgramId(String id);


}
