package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TherapistBO  extends SuperBO {
    public boolean saveTherapist(TherapistDto dto);
    public boolean updateTherapist(TherapistDto dto);
    public boolean deleteTherapist(String pk);
    public ArrayList<TherapistDto> getAllTherapists();
    public ArrayList<TherapistDto> findByTherapistName(String name);
    public TherapistDto findByTherapistId(String id);
    public String getNextTherapistPK();
}
