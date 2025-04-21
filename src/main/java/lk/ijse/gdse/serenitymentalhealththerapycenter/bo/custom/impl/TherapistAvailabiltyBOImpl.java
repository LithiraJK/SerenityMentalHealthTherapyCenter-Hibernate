package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistAvailabiltyBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistAvailabiltyDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.TherapistAvailabiltyDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.TherapistDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistAvailabilityDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistAvailability;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapistAvailabiltyBOImpl implements TherapistAvailabiltyBO {


    private TherapistAvailabiltyDAO availabilityDAO = new TherapistAvailabiltyDAOImpl();
    private TherapistDAO therapistDAO = new TherapistDAOImpl();

    @Override
    public boolean saveAvailability(TherapistAvailabilityDto dto) {
        Optional<Therapist> therapistOpt = therapistDAO.findById(dto.getTherapistId());
        if (therapistOpt.isEmpty()) {
            return false;
        }
        Therapist therapist = therapistOpt.get();
        TherapistAvailability availability = new TherapistAvailability(
                dto.getAvailabilityId(),
                therapist,
                dto.getAvailableDate(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.isAvailable()
        );

        return availabilityDAO.save(availability);
    }

    @Override
    public boolean updateAvailability(TherapistAvailabilityDto dto) {
        Optional<Therapist> therapistOpt = therapistDAO.findById(dto.getTherapistId());
        if (therapistOpt.isEmpty()) {
            return false;
        }
        Therapist therapist = therapistOpt.get();
        TherapistAvailability availability = new TherapistAvailability(
                dto.getAvailabilityId(),
                therapist,
                dto.getAvailableDate(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.isAvailable()
        );

        return availabilityDAO.update(availability);
    }

    @Override
    public boolean deleteAvailability(String pk) {
        return availabilityDAO.delete(pk);
    }

    @Override
    public ArrayList<TherapistAvailabilityDto> getAllAvailabilities() {
        List<TherapistAvailability> availabilities = availabilityDAO.getAll();
        ArrayList<TherapistAvailabilityDto> dtos = new ArrayList<>();

        for (TherapistAvailability a : availabilities) {
            dtos.add(new TherapistAvailabilityDto(
                    a.getAvailability_id(),
                    a.getTherapist().getTherapist_id(),
                    a.getAvailable_date(),
                    a.getStart_time(),
                    a.getEnd_time(),
                    a.is_available()
            ));
        }
        return dtos;
    }

    @Override
    public ArrayList<TherapistAvailabilityDto> findByTherapist(String name) {
        List<TherapistAvailability> availabilities = availabilityDAO.findByTherapist(name);
        ArrayList<TherapistAvailabilityDto> dtos = new ArrayList<>();

        for (TherapistAvailability a : availabilities) {
            dtos.add(new TherapistAvailabilityDto(
                    a.getAvailability_id(),
                    a.getTherapist().getTherapist_id(),
                    a.getAvailable_date(),
                    a.getStart_time(),
                    a.getEnd_time(),
                    a.is_available()
            ));
        }
        return dtos;
    }

    @Override
    public String getNextPK() {
        Optional<String> lastPkOpt = availabilityDAO.getLastPK();
        if (lastPkOpt.isEmpty()) {
            return "TA001";
        }

        String lastPk = lastPkOpt.get();
        int num = Integer.parseInt(lastPk.replace("TA", ""));
        return String.format("TA%03d", num + 1);
    }

    @Override
    public ArrayList<TherapistAvailabilityDto> findAvailableSlotsByTherapist(String therapistId) {
        List<TherapistAvailability> availabilityList = availabilityDAO.findAvailableSlotsByTherapist(therapistId);

        ArrayList<TherapistAvailabilityDto> dtoList = new ArrayList<>();
        for (TherapistAvailability availability : availabilityList) {
            dtoList.add(new TherapistAvailabilityDto(
                    availability.getAvailability_id(),
                    availability.getTherapist().getTherapist_id(),
                    availability.getAvailable_date(),
                    availability.getStart_time(),
                    availability.getEnd_time(),
                    availability.is_available()
            ));
        }

        return dtoList;
    }

    @Override
    public boolean splitAvailabilitySlot(String therapistId, LocalDate date, LocalTime sessionStart, int sessionDuration) {
        Optional<Therapist> therapistOpt = therapistDAO.findById(therapistId);
        if (therapistOpt.isEmpty()) {
            return false;
        }

        Therapist therapist = therapistOpt.get();
        TherapistAvailability originalSlot = findSlotContaining(therapistId, date, sessionStart);
        if (originalSlot == null || !originalSlot.is_available()) {
            return false;
        }

        LocalTime sessionEnd = sessionStart.plusMinutes(sessionDuration);
        LocalTime originalStart = originalSlot.getStart_time();
        LocalTime originalEnd = originalSlot.getEnd_time();

        // Validate session fits in the slot
        if (sessionStart.isBefore(originalStart) || sessionEnd.isAfter(originalEnd)) {
            return false;
        }

        // Begin transaction-like operations
        try {
            // Case 1: Session exactly matches the slot
            if (sessionStart.equals(originalStart) && sessionEnd.equals(originalEnd)) {
                originalSlot.set_available(false);
                return availabilityDAO.update(originalSlot);
            }

            // Case 2: Session at start of slot
            else if (sessionStart.equals(originalStart)) {
                // Create booked session slot
                TherapistAvailability bookedSlot = new TherapistAvailability(
                        getNextPK(),
                        therapist,
                        date,
                        sessionStart,
                        sessionEnd,
                        false
                );

                // Update original slot to be after session
                originalSlot.setStart_time(sessionEnd);

                return availabilityDAO.save(bookedSlot) && availabilityDAO.update(originalSlot);
            }

            // Case 3: Session at end of slot
            else if (sessionEnd.equals(originalEnd)) {
                // Update original slot to be before session
                originalSlot.setEnd_time(sessionStart);

                // Create booked session slot
                TherapistAvailability bookedSlot = new TherapistAvailability(
                        getNextPK(),
                        therapist,
                        date,
                        sessionStart,
                        sessionEnd,
                        false
                );

                return availabilityDAO.update(originalSlot) && availabilityDAO.save(bookedSlot);
            }

            // Case 4: Session in middle of slot
            else {
                // Create booked session slot
                TherapistAvailability bookedSlot = new TherapistAvailability(
                        getNextPK(),
                        therapist,
                        date,
                        sessionStart,
                        sessionEnd,
                        false
                );

                // Create new available slot after session
                TherapistAvailability afterSlot = new TherapistAvailability(
                        getNextPK(),
                        therapist,
                        date,
                        sessionEnd,
                        originalEnd,
                        true
                );

                // Update original slot to be before session
                originalSlot.setEnd_time(sessionStart);

                return availabilityDAO.update(originalSlot)
                        && availabilityDAO.save(bookedSlot)
                        && availabilityDAO.save(afterSlot);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public TherapistAvailability findSlotContaining(String therapistId, LocalDate date, LocalTime sessionStart) {
        List<TherapistAvailability> slots = availabilityDAO.findAvailableSlotsByTherapist(therapistId);

        for (TherapistAvailability slot : slots) {
            if (slot.getAvailable_date().equals(date)
                    && !slot.getStart_time().isAfter(sessionStart)
                    && slot.getEnd_time().isAfter(sessionStart)) {
                return slot;
            }
        }
        return null;
    }





}
