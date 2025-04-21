package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapySessionDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface TherapySessionBO {

    public boolean saveSession(TherapySessionDto dto);
    public boolean updateSession(TherapySessionDto dto);
    public boolean deleteSession(String sessionId);
    public ArrayList<TherapySessionDto> getAllSessions();
    public String getNextSessionPK();

}
