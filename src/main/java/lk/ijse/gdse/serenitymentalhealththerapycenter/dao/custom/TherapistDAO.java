package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;


import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.CrudDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.SuperDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgram;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;
import org.hibernate.Session;


import java.util.List;
import java.util.Optional;

public interface TherapistDAO extends CrudDAO<Therapist> {

//    public boolean save(Therapist entity);
//    public boolean update(Therapist entity);
//    public boolean delete(String pk);
//    public List<Therapist> getAll();
    public List<Therapist> findByTherapistName(String name);
//    public Optional<String> getLastPK();
    public Optional<Therapist> findById(String id);

}
