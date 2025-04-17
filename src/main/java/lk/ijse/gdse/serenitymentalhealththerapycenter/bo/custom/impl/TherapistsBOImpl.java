package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistsBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.DAOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TherapistsBOImpl implements TherapistsBO {

    private final TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public boolean saveTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException {

        Therapist therapist = new Therapist();
        therapist.setTherapistId(therapistDTO.getTherapistId());
        therapist.setTherapistName(therapistDTO.getTherapistName());
        therapist.setEmail(therapistDTO.getEmail());
        therapist.setPhone(therapistDTO.getPhone());
        therapist.setAddress(therapistDTO.getAddress());
        therapist.setDateOfBirth(therapistDTO.getDateOfBirth().toString());
        therapist.setStatus(therapistDTO.getStatus());

        return therapistDAO.save(therapist);
    }

    @Override
    public List<TherapistDTO> getAllTherapists() throws SQLException, ClassNotFoundException {
        ArrayList<TherapistDTO> therapistDTOArrayList = new ArrayList<>();
        ArrayList<Therapist>therapists=therapistDAO.getAllData();

        for (Therapist therapist : therapists) {
            TherapistDTO therapistDTO = new TherapistDTO();
            therapistDTO.setTherapistName(therapist.getTherapistName());
            therapistDTO.setAddress(therapist.getAddress());
            therapistDTO.setDateOfBirth(Date.valueOf(therapist.getDateOfBirth()));
            therapistDTO.setEmail(therapist.getEmail());
            therapistDTO.setPhone(therapist.getPhone());
            therapistDTO.setTherapistId(Integer.parseInt(String.valueOf(therapist.getTherapistId())));
            therapistDTO.setStatus(therapist.getStatus());
            therapistDTOArrayList.add(therapistDTO);
        }
        return therapistDTOArrayList;
    }

    @Override
    public boolean DeleteTherapist(String id) throws SQLException, ClassNotFoundException {
        Therapist therapist = new Therapist();
        therapist.setTherapistId(Integer.parseInt(id));
        return therapistDAO.delete(therapist);
    }

    @Override
    public boolean updateTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException {
        Therapist therapist = new Therapist();
        therapist.setTherapistId(therapistDTO.getTherapistId());
        therapist.setTherapistName(therapistDTO.getTherapistName());
        therapist.setEmail(therapistDTO.getEmail());
        therapist.setPhone(therapistDTO.getPhone());
        therapist.setAddress(therapistDTO.getAddress());
        therapist.setDateOfBirth(therapistDTO.getDateOfBirth().toString());
        therapist.setStatus(therapistDTO.getStatus());

        return therapistDAO.update(therapist);
    }
}
