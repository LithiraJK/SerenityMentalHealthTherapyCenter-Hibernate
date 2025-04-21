package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistAvailabilityDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistAvailability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TherapistAvailabiltyBO {
    public boolean saveAvailability(TherapistAvailabilityDto dto);
    public boolean updateAvailability(TherapistAvailabilityDto dto);
    public boolean deleteAvailability(String pk);
    public ArrayList<TherapistAvailabilityDto> getAllAvailabilities();
    public ArrayList<TherapistAvailabilityDto> findByTherapist(String name);
    public String getNextPK();

    public ArrayList<TherapistAvailabilityDto> findAvailableSlotsByTherapist(String therapistId);
    public boolean splitAvailabilitySlot(String therapistId, LocalDate date, LocalTime sessionStart, int sessionDuration);
    public TherapistAvailability findSlotContaining(String therapistId, LocalDate date, LocalTime sessionStart);


}

