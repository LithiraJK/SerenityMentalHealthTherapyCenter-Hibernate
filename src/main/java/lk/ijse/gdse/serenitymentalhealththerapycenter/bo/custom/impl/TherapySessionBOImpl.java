package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapySessionBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistAvailabilityDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapySessionDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapySessionBOImpl implements TherapySessionBO {

    private final TherapySessionDAO sessionDAO = new TherapySessionDAOImpl();
    private final TherapistDAO therapistDAO = new TherapistDAOImpl();
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final TherapyProgramDAO programDAO = new TherapyProgramDAOImpl();
    private final TherapistAvailabiltyDAO availabilityDAO = new TherapistAvailabiltyDAOImpl();

    @Override
    public boolean saveSession(TherapySessionDto dto) {
        Optional<Therapist> therapistOpt = therapistDAO.findById(dto.getTherapistId());
        Optional<Patient> patientOpt = patientDAO.findById(dto.getPatientId());
        Optional<TherapyProgram> programOpt = programDAO.findById(dto.getTherapyProgramId());

        if (therapistOpt.isEmpty() || patientOpt.isEmpty() || programOpt.isEmpty()) {
            return false;
        }

        // First split the availability slot
        TherapistAvailabiltyBOImpl availabilityBO = new TherapistAvailabiltyBOImpl();
        boolean slotSplit = availabilityBO.splitAvailabilitySlot(
                dto.getTherapistId(),
                dto.getSessionDate(),
                dto.getSessionTime(),
                dto.getDuration()
        );

        if (!slotSplit) {
            return false;
        }

        // Find the newly created unavailable slot (the session slot)
        Optional<TherapistAvailability> sessionAvailabilityOpt = findSessionAvailability(
                dto.getTherapistId(),
                dto.getSessionDate(),
                dto.getSessionTime(),
                dto.getDuration()
        );

        if (sessionAvailabilityOpt.isEmpty()) {
            return false;
        }

        // Create and save the therapy session
        TherapySession session = new TherapySession();
        session.setSession_id(dto.getSessionId());
        session.setTherapist(therapistOpt.get());
        session.setPatient(patientOpt.get());
        session.setTherapy_program(programOpt.get());
        session.setTherapistAvailability(sessionAvailabilityOpt.get());
        session.setDuration(dto.getDuration());
        session.setStatus(dto.getStatus());

        return sessionDAO.save(session);
    }

    private Optional<TherapistAvailability> findSessionAvailability(
            String therapistId, LocalDate date, LocalTime startTime, int duration) {
        LocalTime endTime = startTime.plusMinutes(duration);

        List<TherapistAvailability> slots = availabilityDAO.findByTherapist(therapistId);

        for (TherapistAvailability slot : slots) {
            if (slot.getAvailable_date().equals(date)
                    && slot.getStart_time().equals(startTime)
                    && slot.getEnd_time().equals(endTime)
                    && !slot.is_available()) {
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean updateSession(TherapySessionDto dto) {
        Optional<Therapist> therapistOpt = therapistDAO.findById(dto.getTherapistId());
        Optional<Patient> patientOpt = patientDAO.findById(dto.getPatientId());
        Optional<TherapyProgram> programOpt = programDAO.findById(dto.getTherapyProgramId());
        Optional<TherapistAvailability> availabilityOpt = availabilityDAO.findById(dto.getAvailabilityId());

        if (therapistOpt.isEmpty() || patientOpt.isEmpty() || programOpt.isEmpty() || availabilityOpt.isEmpty()) {
            return false;
        }

        TherapySession session = new TherapySession();
        session.setSession_id(dto.getSessionId());
        session.setTherapist(therapistOpt.get());
        session.setPatient(patientOpt.get());
        session.setTherapy_program(programOpt.get());
        session.setTherapistAvailability(availabilityOpt.get());
        session.setDuration(dto.getDuration());
        session.setStatus(dto.getStatus());

        return sessionDAO.update(session);
    }

    @Override
    public boolean deleteSession(String sessionId) {
        return sessionDAO.delete(sessionId);
    }

    @Override
    public ArrayList<TherapySessionDto> getAllSessions() {
        List<TherapySession> sessions = sessionDAO.getAll();
        ArrayList<TherapySessionDto> dtos = new ArrayList<>();

        for (TherapySession s : sessions) {
            dtos.add(new TherapySessionDto(
                    s.getSession_id(),
                    s.getTherapist().getTherapist_id(),
                    s.getPatient().getPatient_id(),
                    s.getTherapy_program().getProgram_id(),
                    s.getTherapistAvailability().getAvailability_id(),
                    s.getTherapistAvailability().getAvailable_date(),
                    s.getTherapistAvailability().getStart_time(),
                    s.getDuration(),
                    s.getStatus()
            ));
        }

        return dtos;
    }

    @Override
    public String getNextSessionPK() {
        Optional<String> lastPkOpt = sessionDAO.getLastPK();
        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            int num = Integer.parseInt(lastPk.replace("TS", ""));
            return String.format("TS%03d", num + 1);
        }
        return "TS001";
    }



}

