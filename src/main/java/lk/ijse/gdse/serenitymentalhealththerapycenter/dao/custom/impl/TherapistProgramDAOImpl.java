package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapistProgramDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistProgram;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapistProgramId;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TherapistProgramDAOImpl implements TherapistProgramDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(TherapistProgram entity) {
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
            if (session != null) session.close();
        }
    }

    @Override
    public boolean update(TherapistProgram entity) {
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
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(String pk) {
        return false;
    }

    @Override
    public boolean delete(String therapistId, String programId) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapistProgramId id = new TherapistProgramId(therapistId, programId);
            TherapistProgram therapistProgram = session.find(TherapistProgram.class, id);
            if (therapistProgram != null) {
                session.remove(therapistProgram);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<TherapistProgram> getAll() {
        Session session = factoryConfiguration.getSession();
        List<TherapistProgram> programs = session.createQuery("FROM TherapistProgram", TherapistProgram.class).list();
        session.close();
        return programs;
    }

    @Override
    public Optional<TherapistProgram> findByName(String pk) {
        return Optional.empty();
    }

    @Override
    public List<TherapistProgram> findByProgramName(String name) {
        Session session = factoryConfiguration.getSession();
        List<TherapistProgram> programs = session.createQuery(
                        "FROM TherapistProgram tp WHERE tp.therapy_program.name LIKE :name", TherapistProgram.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();
        return programs;
    }

    @Override
    public List<TherapistProgram> findByTherapist(String id) {
        Session session = factoryConfiguration.getSession();
        List<TherapistProgram> programs = session.createQuery(
                        "FROM TherapistProgram tp WHERE tp.therapist.therapist_id LIKE :therapist_id", TherapistProgram.class)
                .setParameter("therapist_id", "%" + id + "%")
                .list();
        session.close();
        return programs;
    }

    @Override
    public Optional<TherapistProgram> findById(String therapistId, String programId) {
        Session session = factoryConfiguration.getSession();
        TherapistProgram program = null;

        try {
            TherapistProgramId id = new TherapistProgramId(therapistId, programId);
            program = session.get(TherapistProgram.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return Optional.ofNullable(program);
    }


    @Override
    public Optional<String> getLastPK() {
        return Optional.empty();
    }

    @Override
    public List<TherapistProgram> findByTherapistId(String therapistId) {
        Session session = factoryConfiguration.getSession();
        List<TherapistProgram> results = null;

        try {
            results = session.createQuery(
                            "FROM TherapistProgram tp WHERE tp.therapist.therapist_id = :therapistId", TherapistProgram.class)
                    .setParameter("therapistId", therapistId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return results;
    }

    @Override
    public List<TherapistProgram> findByProgramId(String programId) {
        Session session = factoryConfiguration.getSession();
        List<TherapistProgram> results = null;

        try {
            results = session.createQuery(
                            "FROM TherapistProgram tp WHERE tp.therapy_program.program_id = :programId", TherapistProgram.class)
                    .setParameter("programId", programId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return results;
    }


}
