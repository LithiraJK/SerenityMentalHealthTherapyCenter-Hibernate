package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistAvailability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TherapistAvailabiltyDAO {
    public boolean save(TherapistAvailability entity);
    public boolean update(TherapistAvailability entity);
    public boolean delete(String pk);
    public List<TherapistAvailability> getAll();
    public List<TherapistAvailability> findByName(String name);
    public Optional<TherapistAvailability> findById(String pk);
    public Optional<String> getLastPK();
    public List<TherapistAvailability> findByTherapist(String therapistName);
    public List<TherapistAvailability> findAvailableSlotsByTherapist(String therapistId);


}
