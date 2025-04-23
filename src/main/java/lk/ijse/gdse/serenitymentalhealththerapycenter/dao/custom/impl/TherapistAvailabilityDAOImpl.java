package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistAvailabilityDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistAvailability;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class TherapistAvailabilityDAOImpl implements TherapistAvailabilityDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<TherapistAvailability> getAll() {
        Session session = factoryConfiguration.getSession();
        List<TherapistAvailability> list = session.createQuery(
                        "FROM TherapistAvailability", TherapistAvailability.class)
                .list();
        session.close();
        return list;
    }

    @Override
    public Optional<TherapistAvailability> findByName(String pk) {
        return Optional.empty();
    }

    @Override
    public boolean save(TherapistAvailability entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(TherapistAvailability entity) {
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
            session.close();
        }
    }

    @Override
    public boolean delete(String availabilityId) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapistAvailability availability = session.get(TherapistAvailability.class, availabilityId);
            if (availability != null) {
                session.remove(availability);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistAvailability> findByTherapistAndDate(String therapistId, LocalDate date) {
        Session session = factoryConfiguration.getSession();
        List<TherapistAvailability> availabilities = session.createQuery(
                        "FROM TherapistAvailability ta WHERE ta.therapist.therapist_id = :id AND ta.available_date = :date",
                        TherapistAvailability.class)
                .setParameter("id", therapistId)
                .setParameter("date", date)
                .getResultList();
        session.close();
        return availabilities;
    }

    @Override
    public List<TherapistAvailability> findByTherapistId(String therapistId) {
        Session session = factoryConfiguration.getSession();
        List<TherapistAvailability> list = session.createQuery(
                        "FROM TherapistAvailability ta WHERE ta.therapist.therapist_id = :id",
                        TherapistAvailability.class)
                .setParameter("id", therapistId)
                .list();
        session.close();
        return list;
    }

    @Override
    public List<TherapistAvailability> findByDate(LocalDate date) {
        Session session = factoryConfiguration.getSession();
        List<TherapistAvailability> list = session.createQuery(
                        "FROM TherapistAvailability ta WHERE ta.available_date = :date",
                        TherapistAvailability.class)
                .setParameter("date", date)
                .list();
        session.close();
        return list;
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = factoryConfiguration.getSession();
        String lastPk = session.createQuery(
                        "SELECT ta.availability_id FROM TherapistAvailability ta ORDER BY ta.availability_id DESC",
                        String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();
        return Optional.ofNullable(lastPk);
    }
}