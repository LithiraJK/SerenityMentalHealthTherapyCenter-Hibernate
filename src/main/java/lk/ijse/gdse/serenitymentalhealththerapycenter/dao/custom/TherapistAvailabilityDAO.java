package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.CrudDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistAvailability;

import java.time.LocalDate;
import java.util.List;

public interface TherapistAvailabilityDAO extends CrudDAO<TherapistAvailability> {
    public List<TherapistAvailability> findByTherapistAndDate(String therapistId, LocalDate date);
    public List<TherapistAvailability> findByTherapistId(String therapistId);
    public List<TherapistAvailability> findByDate(LocalDate date);


}
