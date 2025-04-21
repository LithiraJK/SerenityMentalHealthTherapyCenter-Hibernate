package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;

import java.util.ArrayList;

public interface TherapyProgramBO {

    public boolean saveTherapyProgram(TherapyProgramDto dto);
    public boolean updateTherapyProgram(TherapyProgramDto dto);
    public boolean deleteTherapyProgram(String programId);
    public ArrayList<TherapyProgramDto> getAllTherapyPrograms();
    public ArrayList<TherapyProgramDto> findTherapyProgramByName(String name);
    public String getNextTherapyProgramPK();
    public TherapyProgramDto findTherapyProgramByID(String id);

}
