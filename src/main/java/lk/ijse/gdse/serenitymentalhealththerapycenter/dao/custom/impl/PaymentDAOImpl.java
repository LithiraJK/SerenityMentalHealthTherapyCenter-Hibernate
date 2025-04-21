package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();


    @Override
    public boolean save(Payment entity) {
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
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(Payment entity) {
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
            Payment payment = session.find(Payment.class, pk);
            if (payment != null) {
                session.remove(payment);
                transaction.commit();
                return true;
            }
            return false;
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
    public List<Payment> getAll() {
        Session session = factoryConfiguration.getSession();
        List<Payment> payments = session.createQuery("FROM Payment", Payment.class).list();
        session.close();
        return payments;
    }

    @Override
    public List<Payment> findByName(String name) {
        Session session = factoryConfiguration.getSession();
        List<Payment> payments = session.createQuery(
                        "FROM Payment p WHERE p.patient.name LIKE :name", Payment.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();
        return payments;
    }

    @Override
    public Optional<Payment> findById(String pk) {
        Session session = factoryConfiguration.getSession();
        Payment payment = session.find(Payment.class, pk);
        session.close();
        return Optional.ofNullable(payment);
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = factoryConfiguration.getSession();
        String lastPk = session.createQuery("SELECT p.id FROM Payment p ORDER BY p.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();
        return Optional.ofNullable(lastPk);
    }

    @Override
    public List<Payment> findByDate(LocalDate date) {
        Session session = factoryConfiguration.getSession();
        List<Payment> payments = session.createQuery(
                        "FROM Payment p WHERE DATE(p.paymentDate) = :date", Payment.class)
                .setParameter("date", date)
                .list();
        session.close();
        return payments;
    }


}
