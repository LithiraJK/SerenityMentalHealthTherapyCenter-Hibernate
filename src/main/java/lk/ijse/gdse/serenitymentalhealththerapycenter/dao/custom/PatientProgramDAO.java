package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.CrudDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgram;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PatientProgramDAO extends CrudDAO<PatientProgram> {

//    public boolean save(PatientProgram entity);
//    public boolean update(PatientProgram entity);
    public boolean delete(String patientId, String programId);
//    public List<PatientProgram> getAll();
    public List<PatientProgram> searchByName(String name);
//    public Optional<String> getLastPK();
    public List<PatientProgram> findByPatientId(String id);
    public List<PatientProgram> findByProgramId(String id);

    public Optional<PatientProgram> findById(String patientId, String programId);
    public boolean updateTherapyProgramFee(String patientId, String programId, BigDecimal newFee);


}
