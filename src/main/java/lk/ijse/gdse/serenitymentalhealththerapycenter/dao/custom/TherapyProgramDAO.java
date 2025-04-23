package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.CrudDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgram;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapyProgram;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface TherapyProgramDAO extends CrudDAO<TherapyProgram> {

//    public boolean save(TherapyProgram entity);
//    public boolean update(TherapyProgram entity);
//    public boolean delete(String pk);
//    public List<TherapyProgram> getAll();
    public List<TherapyProgram> findByTherapyProgramName(String name);
    public Optional<TherapyProgram> findById(String pk);
//    public Optional<String> getLastPK();

}
