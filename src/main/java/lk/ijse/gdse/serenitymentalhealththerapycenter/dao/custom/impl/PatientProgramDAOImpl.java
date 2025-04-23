package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PatientProgramDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgram;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.PatientProgramId;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
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
    public boolean delete(String pk) {
        return false;
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
    public Optional<PatientProgram> findByName(String pk) {
        return Optional.empty();
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
    public List<PatientProgram> searchByName(String name) {
        return null;
    }

    public Optional<PatientProgram> findById(String patientId, String programId) {
        Session session = factoryConfiguration.getSession();
        try {
            PatientProgramId id = new PatientProgramId(patientId, programId);
            PatientProgram patientProgram = session.find(PatientProgram.class, id);
            return Optional.ofNullable(patientProgram);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Override
    public boolean updateTherapyProgramFee(String patientId, String programId, BigDecimal newFee) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            PatientProgramId id = new PatientProgramId(patientId, programId);
            PatientProgram patientProgram = session.find(PatientProgram.class, id);

            if (patientProgram != null) {
                // Set the new fee in the patient_program table (not in therapy_program)
                patientProgram.setProgram_fee(newFee);
                session.merge(patientProgram);
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



}
