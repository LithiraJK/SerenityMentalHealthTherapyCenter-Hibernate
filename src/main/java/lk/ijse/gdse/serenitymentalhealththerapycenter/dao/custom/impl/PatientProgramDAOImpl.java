package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PatientProgramDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgram;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgramId;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PatientProgramDAOImpl implements PatientProgramDAO {

    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(PatientProgram entity) {
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
    public boolean update(PatientProgram entity) {
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
    public boolean delete(String patientId, String programId) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Create a composite key (PatientProgramId) using patientId and programId
            PatientProgramId patientProgramId = new PatientProgramId(patientId, programId);

            // Find the PatientProgram entity using the composite key
            PatientProgram patientProgram = session.find(PatientProgram.class, patientProgramId);

            if (patientProgram != null) {
                session.remove(patientProgram);  // Remove the entity
                transaction.commit();  // Commit the transaction
                return true;  // Successfully deleted
            }
            return false;  // PatientProgram not found
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Override
    public List<PatientProgram> getAll() {
        Session session = factoryConfiguration.getSession();
        List<PatientProgram> patientProgram = session.createQuery("FROM PatientProgram", PatientProgram.class).list();
        session.close();
        return patientProgram;
    }


    @Override
    public List<PatientProgram> findByPatientId(String patient_id) {
        Session session = factoryConfiguration.getSession();
        List<PatientProgram> patientPrograms = session.createQuery("FROM PatientProgram pp WHERE pp.patient.id = :patient_id", PatientProgram.class)
                .setParameter("patient_id", patient_id)
                .list();

        session.close();
        return patientPrograms;
    }

    @Override
    public List<PatientProgram> findByProgramId(String program_id) {
        Session session = factoryConfiguration.getSession();
        List<PatientProgram> patientPrograms = session.createQuery("FROM PatientProgram pp WHERE pp.therapy_program.id = :program_id", PatientProgram.class)
                .setParameter("program_id", program_id)
                .list();

        session.close();
        return patientPrograms;
    }


    @Override
    public Optional<String> getLastPK() {
       return Optional.empty();
    }
    @Override
    public List<PatientProgram> findByName(String name) {
        return null;
    }

}
