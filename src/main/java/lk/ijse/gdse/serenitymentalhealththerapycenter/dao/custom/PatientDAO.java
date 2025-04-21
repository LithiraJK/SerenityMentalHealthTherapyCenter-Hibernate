package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.SuperDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import org.hibernate.Session;


import java.util.List;
import java.util.Optional;

public interface PatientDAO{

    public boolean save(Patient entity);
    public boolean update(Patient entity);
    public boolean delete(String pk);
    public List<Patient> getAll();
    public List<Patient> findByName(String name);
    public Optional<Patient> findById(String pk);
    public Optional<String> getLastPK();

}
