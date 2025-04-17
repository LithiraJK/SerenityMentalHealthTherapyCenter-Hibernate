package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapySession;

import java.sql.SQLException;
import java.util.ArrayList;

public class TherapySessionDAOImpl implements TherapySessionDAO {
    @Override
    public ArrayList<TherapySession> getAllData() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(TherapySession Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(TherapySession Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean existId(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(TherapySession id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String getNewId() throws SQLException, ClassNotFoundException {
        return "";
    }

    @Override
    public ArrayList<TherapySession> search(TherapySession newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public TherapySession findById(TherapySession entity) throws SQLException {
        return null;
    }
}
