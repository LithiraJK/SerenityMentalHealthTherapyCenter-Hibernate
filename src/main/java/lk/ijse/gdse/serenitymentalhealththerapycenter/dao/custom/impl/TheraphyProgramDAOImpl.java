package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TheraphyProgramDAOImpl implements TherapyProgramDAO {
    @Override
    public ArrayList<TherapyProgram> getAllData() throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<TherapyProgram> therapyPrograms = new ArrayList<>();

        try {
            List<TherapyProgram> therapyProgramList = session.createQuery("FROM TherapyProgram ", TherapyProgram.class).list();
            therapyPrograms.addAll(therapyProgramList);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return therapyPrograms;
    }

    @Override
    public boolean save(TherapyProgram therapyProgram) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(therapyProgram);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(TherapyProgram therapyProgram) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(therapyProgram);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean existId(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(TherapyProgram id) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.remove(id);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public String getNewId() throws SQLException, ClassNotFoundException {
        return "";
    }

    @Override
    public ArrayList<TherapyProgram> search(TherapyProgram newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public TherapyProgram findById(TherapyProgram therapyProgram) throws SQLException {
        return null;
    }
}
