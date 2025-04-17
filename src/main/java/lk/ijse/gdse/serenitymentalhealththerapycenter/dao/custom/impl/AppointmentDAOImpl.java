package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.AppointmentDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Appointment;

import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDAOImpl implements AppointmentDAO {
    @Override
    public ArrayList<Appointment> getAllData() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Appointment Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Appointment Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean existId(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Appointment id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String getNewId() throws SQLException, ClassNotFoundException {
        return "";
    }

    @Override
    public ArrayList<Appointment> search(Appointment newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Appointment findById(Appointment entity) throws SQLException {
        return null;
    }
}
