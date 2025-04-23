package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistAvailabilityDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistAvailability;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TherapistAvailabilityBO extends SuperBO {

    public boolean saveTherapistAvailability(TherapistAvailabilityDto dto);
    public boolean updateTherapistAvailability(TherapistAvailabilityDto dto);
    public boolean deleteAvailability(String availabilityId);
    public List<TherapistAvailabilityDto> getAllAvailability();
    public List<TherapistAvailabilityDto> findByTherapistId(String therapistId);
    public List<TherapistAvailabilityDto> findByDate(LocalDate date);
    public List<TherapistAvailabilityDto> findByTherapistAndDate(String therapistId, LocalDate date);
    public boolean bookTimeSlot(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration);
    public String generateNextId();

    public boolean restoreTimeSlot(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration);

        // Add methods needed by the controller
        public String getNextPK();
        public ArrayList<TherapistAvailabilityDto> getAllAvailabilities();
        public ArrayList<TherapistAvailabilityDto> findByTherapist(String therapistName);
        public boolean saveAvailability(TherapistAvailabilityDto dto);
        public boolean updateAvailability(TherapistAvailabilityDto dto);

}
