package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PaymentBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.DAOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PaymentDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PaymentBOImpl implements PaymentBO {

    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    PatientProgramDAO patientProgramDAO = (PatientProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT_PROGRAM);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);
    TherapySessionDAO sessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);


    @Override
    public boolean savePayment(PaymentDto dto) {
        boolean isCompleted = false;

        // Get the session from the FactoryConfiguration instance
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Get the Patient and TherapyProgram based on DTO
            Optional<Patient> patientOpt = patientDAO.findById(dto.getPatient().getPatient_id());
            Optional<TherapyProgram> programOpt = programDAO.findById(dto.getTherapyProgram().getProgram_id());
            Optional<TherapySession> sessionOpt = Optional.empty();

            if (dto.getTherapySession() != null) {
                sessionOpt = sessionDAO.findBySessionId(dto.getTherapySession().getSession_id());
            }

            // If patient or program is not found, return false
            if (patientOpt.isEmpty() || programOpt.isEmpty()) return false;

            // Create new Payment entity
            Payment payment = new Payment();
            payment.setPayment_id(dto.getPaymentId());
            payment.setPatient(patientOpt.get());
            payment.setTherapy_program(programOpt.get());
            payment.setTherapy_session(sessionOpt.orElse(null)); // nullable
            payment.setAmount(dto.getAmount());
            payment.setPayment_date(dto.getPaymentDate());

            // Save the payment
            if (paymentDAO.save(payment)) {
                isCompleted = true;

                // Find PatientProgram based on the patient and program IDs
                Optional<PatientProgram> patientProgram = patientProgramDAO.findById(patientOpt.get().getPatient_id(), programOpt.get().getProgram_id());
                if (patientProgram.isPresent()) {
                    BigDecimal oldFee = patientProgram.get().getProgram_fee();
                    BigDecimal newFee = oldFee.subtract(dto.getAmount());

                    // Update the fee for the program
                    patientProgramDAO.updateTherapyProgramFee(patientOpt.get().getPatient_id(), programOpt.get().getProgram_id(), newFee);
                }

                // Commit the transaction if everything is successful
                transaction.commit();
            } else {
                isCompleted = false;
            }

        } catch (Exception e) {
            // If there's any exception, roll back the transaction
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            // Close the session
            if (session != null) session.close();
        }

        return isCompleted;
    }



    @Override
    public boolean updatePayment(PaymentDto dto) {
        boolean isCompleted = false;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Optional<Patient> patientOpt = patientDAO.findById(dto.getPatient().getPatient_id());
            Optional<TherapyProgram> programOpt = programDAO.findById(dto.getTherapyProgram().getProgram_id());
            Optional<TherapySession> sessionOpt = Optional.empty();

            if (dto.getTherapySession() != null) {
                sessionOpt = sessionDAO.findBySessionId(dto.getTherapySession().getSession_id());
            }

            // If patient or program is not found, return false
            if (patientOpt.isEmpty() || programOpt.isEmpty()) {
                return false;
            }

            // Get the old payment details to check the old payment amount
            Optional<Payment> existingPaymentOpt = paymentDAO.findById(dto.getPaymentId());
            if (existingPaymentOpt.isEmpty()) {
                return false;
            }

            Payment existingPayment = existingPaymentOpt.get();

            // Update Payment entity with new values
            existingPayment.setPatient(patientOpt.get());
            existingPayment.setTherapy_program(programOpt.get());
            existingPayment.setTherapy_session(sessionOpt.orElse(null)); // nullable
            existingPayment.setAmount(dto.getAmount());
            existingPayment.setPayment_date(dto.getPaymentDate());

            // Save the updated payment
            if (paymentDAO.update(existingPayment)) {
                isCompleted = true;

                // Adjust the program fee if the payment amount has changed
                Optional<PatientProgram> patientProgramOpt = patientProgramDAO.findById(patientOpt.get().getPatient_id(), programOpt.get().getProgram_id());
                if (patientProgramOpt.isPresent()) {
                    PatientProgram patientProgram = patientProgramOpt.get();
                    BigDecimal oldFee = patientProgram.getProgram_fee();

                    // Check if the payment amount has changed
                    BigDecimal paymentDifference = dto.getAmount().subtract(existingPayment.getAmount());
                    if (paymentDifference.compareTo(BigDecimal.ZERO) != 0) {
                        // Update the program fee with the new payment difference
                        BigDecimal newFee = oldFee.subtract(paymentDifference);
                        patientProgramDAO.updateTherapyProgramFee(patientOpt.get().getPatient_id(), programOpt.get().getProgram_id(), newFee);
                    }
                }

                transaction.commit();
            } else {
                isCompleted = false;
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return isCompleted;
    }


    @Override
    public boolean deletePayment(String paymentId) {
        boolean isCompleted = false;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Optional<Payment> paymentOpt = paymentDAO.findById(paymentId);
            if (paymentOpt.isEmpty()) {
                return false; // Payment not found, return false
            }

            Payment payment = paymentOpt.get();
            Patient patient = payment.getPatient();
            TherapyProgram program = payment.getTherapy_program();

            Optional<PatientProgram> patientProgramOpt = patientProgramDAO.findById(patient.getPatient_id(), program.getProgram_id());
            if (patientProgramOpt.isPresent()) {
                PatientProgram patientProgram = patientProgramOpt.get();
                BigDecimal oldFee = patientProgram.getProgram_fee();

                // Adjust the program fee by adding the payment amount back
                BigDecimal newFee = oldFee.add(payment.getAmount());

                // Update the program fee in PatientProgram
                patientProgramDAO.updateTherapyProgramFee(patient.getPatient_id(), program.getProgram_id(), newFee);
            }

            // Delete the payment
            if (paymentDAO.delete(paymentId)) {
                isCompleted = true;
                // Commit the transaction if everything is successful
                transaction.commit();
            } else {
                isCompleted = false;
            }

        } catch (Exception e) {
            // If there's any exception, roll back the transaction
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            // Close the session
            if (session != null) session.close();
        }

        return isCompleted;
    }




    @Override
    public ArrayList<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentDAO.getAll();
        ArrayList<PaymentDto> dtos = new ArrayList<>();

        for (Payment p : payments) {
            PaymentDto dto = new PaymentDto(
                    p.getPayment_id(),
                    p.getPatient(),
                    p.getTherapy_program(),
                    p.getTherapy_session(),
                    p.getAmount(),
                    p.getPayment_date()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public ArrayList<PaymentDto> searchByPatientName(String name) {
        List<Payment> payments = paymentDAO.findByPatientName(name);
        ArrayList<PaymentDto> dtos = new ArrayList<>();

        for (Payment p : payments) {
            PaymentDto dto = new PaymentDto(
                    p.getPayment_id(),
                    p.getPatient(),
                    p.getTherapy_program(),
                    p.getTherapy_session(),
                    p.getAmount(),
                    p.getPayment_date()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public ArrayList<PaymentDto> searchByDate(LocalDate date) {
        List<Payment> payments = paymentDAO.findByDate(date);
        ArrayList<PaymentDto> dtos = new ArrayList<>();

        for (Payment p : payments) {
            PaymentDto dto = new PaymentDto(
                    p.getPayment_id(),
                    p.getPatient(),
                    p.getTherapy_program(),
                    p.getTherapy_session(),
                    p.getAmount(),
                    p.getPayment_date()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public String getNextPaymentPK() {
        Optional<String> lastPK = paymentDAO.getLastPK();
        if (lastPK.isPresent()) {
            String pk = lastPK.get().replace("PAY-", "");
            int next = Integer.parseInt(pk) + 1;
            return String.format("PAY-%04d", next);
        }
        return "PAY-0001";
    }


    @Override
    public PaymentDto constructPaymentDto(String paymentId, String patientId, String programId, String sessionId, BigDecimal amount, LocalDate date) {
        Optional<Patient> patientOpt = patientDAO.findById(patientId);
        Optional<TherapyProgram> programOpt = programDAO.findById(programId);
        Optional<TherapySession> sessionOpt = (sessionId != null && !sessionId.isEmpty())
                ? sessionDAO.findBySessionId(sessionId)
                : Optional.empty();

        if (patientOpt.isEmpty() || programOpt.isEmpty()) {
            throw new RuntimeException("Patient or Program not found.");
        }

        Patient patient = patientOpt.get();
        TherapyProgram program = programOpt.get();
        TherapySession session = sessionOpt.orElse(null);

        return new PaymentDto(paymentId, patient, program, session, amount, date);
    }


}
