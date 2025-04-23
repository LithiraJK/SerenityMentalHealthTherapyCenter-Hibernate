package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.DAOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapistBOImpl implements TherapistBO {

    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public boolean saveTherapist(TherapistDto dto) {
        Therapist therapist = new Therapist();
        therapist.setTherapist_id(dto.getTherapistId());
        therapist.setName(dto.getName());
        therapist.setEmail(dto.getEmail());
        therapist.setPhone(dto.getPhone());
        therapist.setSpecialization(dto.getSpecialization());

        return therapistDAO.save(therapist);
    }

    @Override
    public boolean updateTherapist(TherapistDto dto) {
        Therapist therapist = new Therapist();
        therapist.setTherapist_id(dto.getTherapistId());
        therapist.setName(dto.getName());
        therapist.setEmail(dto.getEmail());
        therapist.setPhone(dto.getPhone());
        therapist.setSpecialization(dto.getSpecialization());

        return therapistDAO.update(therapist);
    }

    @Override
    public boolean deleteTherapist(String pk) {
        return therapistDAO.delete(pk);
    }

    @Override
    public ArrayList<TherapistDto> getAllTherapists() {
        List<Therapist> therapists = therapistDAO.getAll();

        ArrayList<TherapistDto> therapistDtos = new ArrayList<>();
        for (Therapist therapist : therapists) {
            TherapistDto therapistDto = new TherapistDto();
            therapistDto.setTherapistId(therapist.getTherapist_id());
            therapistDto.setName(therapist.getName());
            therapistDto.setEmail(therapist.getEmail());
            therapistDto.setPhone(therapist.getPhone());
            therapistDto.setSpecialization(therapist.getSpecialization());
            therapistDtos.add(therapistDto);
        }
        return therapistDtos;
    }

    @Override
    public ArrayList<TherapistDto> findByTherapistName(String name) {
        List<Therapist> therapists = therapistDAO.findByTherapistName(name);
        ArrayList<TherapistDto> therapistDtos = new ArrayList<>();

        for (Therapist therapist : therapists) {
            TherapistDto therapistDto = new TherapistDto();
            therapistDto.setTherapistId(therapist.getTherapist_id());
            therapistDto.setName(therapist.getName());
            therapistDto.setEmail(therapist.getEmail());
            therapistDto.setPhone(therapist.getPhone());
            therapistDto.setSpecialization(therapist.getSpecialization());
            therapistDtos.add(therapistDto);
        }

        return therapistDtos;
    }

    @Override
    public TherapistDto findByTherapistId(String id) {
        Optional<Therapist> therapistOpt = therapistDAO.findById(id);
        if (therapistOpt.isEmpty()) {
            return null;
        }
        Therapist therapist = therapistOpt.get();

        TherapistDto therapistDto = new TherapistDto();
        therapistDto.setTherapistId(therapist.getTherapist_id());
        therapistDto.setName(therapist.getName());
        therapistDto.setEmail(therapist.getEmail());
        therapistDto.setPhone(therapist.getPhone());
        therapistDto.setSpecialization(therapist.getSpecialization());

        return therapistDto;
    }

    @Override
    public String getNextTherapistPK() {
        Optional<String> lastPkOpt = therapistDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            String numericPart = lastPk.substring(1);
            int currentId = Integer.parseInt(numericPart);
            int nextId = currentId + 1;

            return String.format("T%03d", nextId);
        }

        return "T001";
    }

}
