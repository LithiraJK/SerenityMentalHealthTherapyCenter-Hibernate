package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PaymentBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PatientDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.PatientDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.TherapyProgramDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.TherapySessionDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PaymentDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Payment;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapyProgram;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapySession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final TherapyProgramDAO programDAO = new TherapyProgramDAOImpl();
    private final TherapySessionDAO sessionDAO = new TherapySessionDAOImpl();

    @Override
    public boolean savePayment(PaymentDto dto) {
        Optional<Patient> patientOpt = patientDAO.findById(dto.getPatient().getPatient_id());
        Optional<TherapyProgram> programOpt = programDAO.findById(dto.getTherapyProgram().getProgram_id());
        Optional<TherapySession> sessionOpt = Optional.empty();

        if (dto.getTherapySession() != null) {
            sessionOpt = sessionDAO.findById(dto.getTherapySession().getSession_id());
        }

        if (patientOpt.isEmpty() || programOpt.isEmpty()) return false;

        Payment payment = new Payment();
        payment.setPayment_id(dto.getPaymentId());
        payment.setPatient(patientOpt.get());
        payment.setTherapy_program(programOpt.get());
        payment.setTherapy_session(sessionOpt.orElse(null)); // nullable
        payment.setAmount(dto.getAmount());
        payment.setPayment_date(dto.getPaymentDate());

        return paymentDAO.save(payment);
    }

    @Override
    public boolean updatePayment(PaymentDto dto) {
        Optional<Patient> patientOpt = patientDAO.findById(dto.getPatient().getPatient_id());
        Optional<TherapyProgram> programOpt = programDAO.findById(dto.getTherapyProgram().getProgram_id());
        Optional<TherapySession> sessionOpt = Optional.empty();

        if (dto.getTherapySession() != null) {
            sessionOpt = sessionDAO.findById(dto.getTherapySession().getSession_id());
        }

        if (patientOpt.isEmpty() || programOpt.isEmpty()) return false;

        Payment payment = new Payment();
        payment.setPayment_id(dto.getPaymentId());
        payment.setPatient(patientOpt.get());
        payment.setTherapy_program(programOpt.get());
        payment.setTherapy_session(sessionOpt.orElse(null));
        payment.setAmount(dto.getAmount());
        payment.setPayment_date(dto.getPaymentDate());

        return paymentDAO.update(payment);
    }

    @Override
    public boolean deletePayment(String paymentId) {
        return paymentDAO.delete(paymentId);
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
        List<Payment> payments = paymentDAO.findByName(name);
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
                ? sessionDAO.findById(sessionId)
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
