package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.BOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistAvailabilityBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapySessionBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.DAOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapySessionDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.*;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapySessionBOImpl implements TherapySessionBO {

    TherapySessionDAO therapySessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    TherapyProgramDAO therapyProgramDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    TherapistAvailabilityBO therapistAvailabilityBO = (TherapistAvailabilityBO) BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST_AVAILABILITY);

    @Override
    public boolean save(TherapySessionDto dto) {
        boolean isCompleted = false;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Retrieve the entities from their respective DAOs
            Optional<Therapist> therapistOpt = therapistDAO.findById(dto.getTherapistId());
            Optional<Patient> patientOpt = patientDAO.findById(dto.getPatientId());
            Optional<TherapyProgram> programOpt = therapyProgramDAO.findById(dto.getTherapyProgramId());

//            Optional<TherapistAvailability> availabilityOpt = availabilityDAO.findById(dto.getAvailabilityId());

            // Check if any of the required entities are not found
            if (therapistOpt.isEmpty() || patientOpt.isEmpty() || programOpt.isEmpty()) {
                return false;
            }

            // Create the TherapySession entity
            TherapySession therapySession = new TherapySession();
            therapySession.setSession_id(dto.getSessionId());
            therapySession.setTherapist(therapistOpt.get());
            therapySession.setPatient(patientOpt.get());
            therapySession.setTherapy_program(programOpt.get());
            therapySession.setTherapistAvailability(null); // Set null because we can not get the availability object
            therapySession.setSession_date(dto.getSessionDate());
            therapySession.setStart_time(dto.getSessionTime());
            therapySession.setDuration(dto.getDuration());
            therapySession.setStatus(dto.getStatus());


            // Convert the duration (in minutes) to a Duration object
            Duration sessionDuration = Duration.ofMinutes(dto.getDuration());

            // Attempt to book the time slot
            boolean success = therapistAvailabilityBO.bookTimeSlot(
                    dto.getTherapistId(),
                    dto.getSessionDate(),
                    dto.getSessionTime(),
                    sessionDuration
            );

            if (success) {
                if (therapySessionDAO.save(therapySession)) {
                    isCompleted = true;
                    transaction.commit();
                }
            } else {
                isCompleted = false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return isCompleted;
    }

//    @Override
//    public boolean save(TherapySessionDto dto) {
//        boolean isCompleted = false;
//
//        Session session = FactoryConfiguration.getInstance().getSession();
//        Transaction transaction = session.beginTransaction();
//
//        try {
//            // Retrieve required entities
//            Optional<Therapist> therapistOpt = therapistDAO.findById(dto.getTherapistId());
//            Optional<Patient> patientOpt = patientDAO.findById(dto.getPatientId());
//            Optional<TherapyProgram> programOpt = therapyProgramDAO.findById(dto.getTherapyProgramId());
//
//            if (therapistOpt.isEmpty() || patientOpt.isEmpty() || programOpt.isEmpty()) {
//                return false;
//            }
//
//            // Create and save the TherapySession with null availability
//            TherapySession therapySession = new TherapySession();
//            therapySession.setSession_id(dto.getSessionId());
//            therapySession.setTherapist(therapistOpt.get());
//            therapySession.setPatient(patientOpt.get());
//            therapySession.setTherapy_program(programOpt.get());
//            therapySession.setTherapistAvailability(null); // initially null
//            therapySession.setSession_date(dto.getSessionDate());
//            therapySession.setStart_time(dto.getSessionTime());
//            therapySession.setDuration(dto.getDuration());
//            therapySession.setStatus(dto.getStatus());
//
//            boolean saved = therapySessionDAO.save(therapySession);
//
//            if (!saved) {
//                transaction.rollback();
//                return false;
//            }
//
//            // Try to book a slot
//            Duration sessionDuration = Duration.ofMinutes(dto.getDuration());
//
//            Optional<TherapistAvailability> bookedAvailabilityOpt = therapistAvailabiltyBO.bookTimeSlot(
//                    dto.getTherapistId(),
//                    dto.getSessionDate(),
//                    dto.getSessionTime(),
//                    sessionDuration
//            );
//
//            if (bookedAvailabilityOpt.isPresent()) {
//                // Now update the session with the booked availability
//                therapySession.setTherapistAvailability(bookedAvailabilityOpt.get());
//                therapySessionDAO.update(therapySession); // assumes you have this method
//
//                transaction.commit();
//                isCompleted = true;
//
//            } else {
//                transaction.rollback();
//                isCompleted = false;
//            }
//
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            e.printStackTrace();
//        } finally {
//            if (session != null) session.close();
//        }
//
//        return isCompleted;
//    }




    @Override
    public boolean update(TherapySessionDto dto) {
        // Fetch related entities
        Optional<Therapist> therapistOpt = therapistDAO.findById(dto.getTherapistId());
        Optional<Patient> patientOpt = patientDAO.findById(dto.getPatientId());
        Optional<TherapyProgram> programOpt = therapyProgramDAO.findById(dto.getTherapyProgramId());
        Optional<TherapySession> optionalSession = therapySessionDAO.findBySessionId(dto.getSessionId());

        // Return false if any essential entity is missing
        if (therapistOpt.isEmpty() || patientOpt.isEmpty() || programOpt.isEmpty() || optionalSession.isEmpty()) {
            return false;
        }

        // Load existing session
        TherapySession therapySession = optionalSession.get();

        // Update entity fields
        therapySession.setTherapist(therapistOpt.get());
        therapySession.setPatient(patientOpt.get());
        therapySession.setTherapy_program(programOpt.get());

        if (dto.getAvailabilityId() != null) {
//            therapySession.setTherapistAvailability(new TherapistAvailability(dto.getAvailabilityId()));
            therapySession.setTherapistAvailability(null);
        } else {
            therapySession.setTherapistAvailability(null); // Optional: reset if null
        }

        therapySession.setSession_date(dto.getSessionDate());
        therapySession.setStart_time(dto.getSessionTime());
        therapySession.setDuration(dto.getDuration());
        therapySession.setStatus(dto.getStatus());

        // Save the updated session
        return therapySessionDAO.update(therapySession);
    }


//
//    @Override
//    public boolean delete(String sessionId) {
//        return therapySessionDAO.delete(sessionId);
//    }

    @Override
    public boolean delete(String sessionId) {
        Optional<TherapySession> optionalSession = therapySessionDAO.findBySessionId(sessionId);

        if (optionalSession.isEmpty()) return false;
        TherapySession session = optionalSession.get();

        boolean restored = therapistAvailabilityBO.restoreTimeSlot(
                session.getTherapist().getTherapist_id(),
                session.getSession_date(),
                session.getStart_time(),
                Duration.ofMinutes(session.getDuration())
        );

        if (!restored) return false;
        return therapySessionDAO.delete(sessionId);
    }


    @Override
    public ArrayList<TherapySessionDto> getAll() {
        List<TherapySession> sessions = therapySessionDAO.getAll();
        ArrayList<TherapySessionDto> sessionDtos = new ArrayList<>();

        for (TherapySession session : sessions) {
            TherapySessionDto dto = new TherapySessionDto();
            dto.setSessionId(session.getSession_id());
            dto.setTherapistId(session.getTherapist().getTherapist_id());
            dto.setPatientId(session.getPatient().getPatient_id());
            dto.setTherapyProgramId(session.getTherapy_program().getProgram_id());
            dto.setAvailabilityId(session.getTherapistAvailability() != null
                    ? session.getTherapistAvailability().getAvailability_id() : null);
            dto.setSessionDate(session.getSession_date());
            dto.setSessionTime(session.getStart_time());
            dto.setDuration(session.getDuration());
            dto.setStatus(session.getStatus());

            sessionDtos.add(dto);
        }

        return sessionDtos;
    }


    @Override
    public TherapySessionDto findBySessionId(String sessionId) {
        Optional<TherapySession> optional = therapySessionDAO.findBySessionId(sessionId);
        if (optional.isEmpty()) return null;

        TherapySession session = optional.get();
        TherapySessionDto dto = new TherapySessionDto();
        dto.setSessionId(session.getSession_id());
        dto.setTherapistId(session.getTherapist().getTherapist_id());
        dto.setPatientId(session.getPatient().getPatient_id());
        dto.setTherapyProgramId(session.getTherapy_program().getProgram_id());
        dto.setAvailabilityId(session.getTherapistAvailability() != null
                ? session.getTherapistAvailability().getAvailability_id() : null);
        dto.setSessionDate(session.getSession_date());
        dto.setSessionTime(session.getStart_time());
        dto.setDuration(session.getDuration());
        dto.setStatus(session.getStatus());

        return dto;
    }


    @Override
    public ArrayList<TherapySessionDto> findByPatientId(String patientId) {
        List<TherapySession> sessions = therapySessionDAO.findByPatientId(patientId);
        ArrayList<TherapySessionDto> sessionDtos = new ArrayList<>();

        for (TherapySession session : sessions) {
            TherapySessionDto dto = new TherapySessionDto();
            dto.setSessionId(session.getSession_id());
            dto.setTherapistId(session.getTherapist().getTherapist_id());
            dto.setPatientId(session.getPatient().getPatient_id());
            dto.setTherapyProgramId(session.getTherapy_program().getProgram_id());
            dto.setAvailabilityId(session.getTherapistAvailability() != null
                    ? session.getTherapistAvailability().getAvailability_id() : null);
            dto.setSessionDate(session.getSession_date());
            dto.setSessionTime(session.getStart_time());
            dto.setDuration(session.getDuration());
            dto.setStatus(session.getStatus());

            sessionDtos.add(dto);
        }

        return sessionDtos;
    }


    @Override
    public String getNextSessionPK() {
        Optional<String> lastPkOpt = therapySessionDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            String numericPart = lastPk.substring(2);
            int currentId = Integer.parseInt(numericPart);
            int nextId = currentId + 1;
            return String.format("TS%03d", nextId);
        }

        return "TS001";
    }



}
