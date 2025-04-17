package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDTO;

import java.sql.SQLException;
import java.util.List;

public interface TherapyProgramBO extends SuperBO {
    boolean saveProgram(TherapyProgramDTO therapyProgramDTO) throws SQLException, ClassNotFoundException;

    List<TherapyProgramDTO> getAllPrograms() throws SQLException, ClassNotFoundException;

    boolean deleteTherapyProgram(String id) throws SQLException, ClassNotFoundException;

    boolean updateProgram(TherapyProgramDTO therapyProgramDTO) throws SQLException, ClassNotFoundException;
}
