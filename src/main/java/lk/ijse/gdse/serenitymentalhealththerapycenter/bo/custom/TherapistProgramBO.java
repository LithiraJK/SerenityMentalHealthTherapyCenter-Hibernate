package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistProgramDto;

import java.util.List;

public interface TherapistProgramBO {
    public boolean saveTherapistProgram(String therapistId, String programId);
    public boolean updateTherapistProgram(String therapistId, String programId);
    public boolean deleteTherapistProgram(String therapistId, String programId);
    public TherapistProgramDto findById(String therapistId, String programId);
    public List<TherapistProgramDto> getAllTherapistPrograms();
    public List<TherapistProgramDto> findByProgramName(String name);
    public List<TherapistProgramDto> getTherapistProgramsByTherapist(String id);

}
