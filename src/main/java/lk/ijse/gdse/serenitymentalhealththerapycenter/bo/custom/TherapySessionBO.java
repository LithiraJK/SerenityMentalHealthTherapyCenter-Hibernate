package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapySessionDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TherapySessionBO extends SuperBO {
    boolean save(TherapySessionDto dto);
    boolean update(TherapySessionDto dto);
    boolean delete(String sessionId);
    List<TherapySessionDto> getAll();
    TherapySessionDto findBySessionId(String sessionId);
    List<TherapySessionDto> findByPatientId(String patientId);
    String getNextSessionPK();

}
