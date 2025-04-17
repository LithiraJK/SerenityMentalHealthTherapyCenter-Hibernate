package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Payment;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public ArrayList<Payment> getAllData() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Payment Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Payment Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean existId(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Payment id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String getNewId() throws SQLException, ClassNotFoundException {
        return "";
    }

    @Override
    public ArrayList<Payment> search(Payment newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Payment findById(Payment entity) throws SQLException {
        return null;
    }
}
