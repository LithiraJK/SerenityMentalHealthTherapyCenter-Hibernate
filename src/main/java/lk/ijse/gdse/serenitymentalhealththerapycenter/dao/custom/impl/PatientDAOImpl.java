package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PatientDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PatientDAOImpl implements PatientDAO {

    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Patient entity) {
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
    public boolean update(Patient entity) {
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
            Patient patient = session.find(Patient.class, pk);
            if (patient!= null) {
                session.remove(patient);
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
    public List<Patient> getAll() {
        Session session = factoryConfiguration.getSession();
        List<Patient> patient = session.createQuery("FROM Patient", Patient.class).list();
        session.close();
        return patient;
    }

    @Override
    public Optional<Patient> findByName(String pk) {
        return Optional.empty();
    }

    @Override
    public List<Patient> findByPatientName(String name) {
        Session session = factoryConfiguration.getSession();
        List<Patient> patients = session.createQuery("FROM Patient p WHERE p.name LIKE :name", Patient.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();

        return patients;
    }

    @Override
    public Optional<Patient> findById(String pk) {
        Session session = factoryConfiguration.getSession();
        Patient patient = session.get(Patient.class, pk);
        session.close();
        return Optional.ofNullable(patient);
    }


    @Override
    public Optional<String> getLastPK() {
        Session session = factoryConfiguration.getSession();
        String lastPk = session.createQuery("SELECT p.patient_id FROM Patient p ORDER BY p.patient_id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();

        return Optional.ofNullable(lastPk);
    }


}
