package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapistTM;

import java.sql.SQLException;
import java.util.List;

public interface TherapistsBO extends SuperBO {
    boolean saveTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException;

    List<TherapistDTO> getAllTherapists() throws SQLException, ClassNotFoundException;

    boolean DeleteTherapist(String id) throws SQLException, ClassNotFoundException;

    boolean updateTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException;
}
