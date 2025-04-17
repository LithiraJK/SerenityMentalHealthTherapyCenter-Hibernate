package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TherapistDAOImpl implements TherapistDAO {
    @Override
    public ArrayList<Therapist> getAllData() throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<Therapist> therapists = new ArrayList<>();

        try {
            List<Therapist> therapistList = session.createQuery("FROM Therapist ", Therapist.class).list();
            therapists.addAll(therapistList);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return therapists;
    }

    @Override
    public boolean save(Therapist therapist) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(therapist);
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
    public boolean update(Therapist therapist) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(therapist);
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
    public boolean delete(Therapist id) throws SQLException, ClassNotFoundException {
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
    public ArrayList<Therapist> search(Therapist newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Therapist findById(Therapist entity) throws SQLException {
        return null;
    }
}
