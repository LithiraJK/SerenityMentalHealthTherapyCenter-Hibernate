package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TherapistDAOImpl implements TherapistDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Therapist entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
            return true;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(Therapist entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Override
    public boolean delete(String pk) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Therapist therapist = session.find(Therapist.class, pk);
            // Therapist therapist = session.get(Therapist.class, pk);
            if (therapist!= null) {
                session.remove(therapist);
                transaction.commit();
                return true;
            }
            return false;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Therapist> getAll() {
        Session session = factoryConfiguration.getSession();
        List<Therapist> therapists = session.createQuery("FROM Therapist", Therapist.class).list();
        session.close();
        return therapists;
    }

    @Override
    public Optional<Therapist> findByName(String pk) {
        return Optional.empty();
    }

    @Override
    public List<Therapist> findByTherapistName(String name) {
        Session session = factoryConfiguration.getSession();
        List<Therapist> therapists = session.createQuery("FROM Therapist th WHERE th.name LIKE :name", Therapist.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();

        return therapists;
    }

    @Override
    public Optional<Therapist> findById(String id) {
        Session session = factoryConfiguration.getSession();
        Therapist therapist = null;
        try {
            therapist = session.get(Therapist.class, id);
        } finally {
            session.close();
        }
        return Optional.ofNullable(therapist);
    }



    @Override
    public Optional<String> getLastPK() {
        Session session = factoryConfiguration.getSession();
        String lastPk = session.createQuery("SELECT th.therapist_id FROM Therapist th ORDER BY th.therapist_id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();

        return Optional.ofNullable(lastPk);
    }


}
