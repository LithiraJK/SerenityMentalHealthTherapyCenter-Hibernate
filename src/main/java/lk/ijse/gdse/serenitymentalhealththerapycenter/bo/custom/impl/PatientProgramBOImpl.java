package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PatientProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.DAOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PatientDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PatientProgramDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.PatientDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.PatientProgramDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.TherapyProgramDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientProgramBOImpl implements PatientProgramBO {

    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    PatientProgramDAO patientProgramDAO = (PatientProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT_PROGRAM);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    TherapyProgramDAO therapyProgramDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);


//    @Override
//    public boolean savePatientProgram(PatientProgramDto dto) {
//        PatientProgramId patientProgramId = new PatientProgramId(dto.getPatientId(), dto.getProgramId());
//        Patient patient = patientDAO.findById(dto.getPatientId()).orElse(new Patient());
//        TherapyProgram program = therapyProgramDAO.findById(dto.getProgramId()).orElse(new TherapyProgram());
//        Payment payment = null; // complete this after completing the PaymentDAOImpl
//
//        PatientProgram patientProgram = new PatientProgram();
//        patientProgram.setId(patientProgramId);
//        patientProgram.setPatient(patient);
//        patientProgram.setTherapy_program(program);
//        patientProgram.setRegistration_date(dto.getRegistrationDate());
//        patientProgram.setPayment(payment);
//
//        return patientProgramDAO.save(patientProgram);
//    }

    @Override
    public boolean savePatientProgram(PatientProgramDto dto) {
        Optional<Patient> patientOpt = patientDAO.findById(dto.getPatientId());
        Optional<TherapyProgram> programOpt = therapyProgramDAO.findById(dto.getProgramId());
        Optional<Payment> paymentOtp = paymentDAO.findById(dto.getPaymentId());

        if (patientOpt.isEmpty() || programOpt.isEmpty()) {
            return false;
        }

        Payment payment = null;
        BigDecimal leftToPay = programOpt.get().getFee();

        if (!paymentOtp.isEmpty()) {
            payment = paymentOtp.get();
            leftToPay = leftToPay.subtract(payment.getAmount());
        }

        Patient patient = patientOpt.get();
        TherapyProgram program = programOpt.get();


        PatientProgram patientProgram = new PatientProgram();
        patientProgram.setId(new PatientProgramId(patient.getPatient_id(), program.getProgram_id()));
        patientProgram.setPatient(patient);
        patientProgram.setTherapy_program(program);
        patientProgram.setRegistration_date(dto.getRegistrationDate());
        patientProgram.setPayment(payment);
        patientProgram.setProgram_fee(leftToPay);

        return patientProgramDAO.save(patientProgram);
    }


    @Override
    public boolean updatePatientProgram(PatientProgramDto dto) {
        // complete the update method later
        Optional<Patient> patientOpt = patientDAO.findById(dto.getPatientId());
        Optional<TherapyProgram> programOpt = therapyProgramDAO.findById(dto.getProgramId());

        Optional<Payment> paymentOtp = Optional.empty();
        if (dto.getPaymentId() != null) {
            paymentOtp = paymentDAO.findById(dto.getPaymentId());
        }

        if (patientOpt.isEmpty() || programOpt.isEmpty()) {
            return false;
        }

        Payment payment = null;
        if (paymentOtp.isPresent()) {
            payment = paymentOtp.get();
        }

        Patient patient = patientOpt.get();
        TherapyProgram program = programOpt.get();

        PatientProgram patientProgram = new PatientProgram();
        patientProgram.setId(new PatientProgramId(patient.getPatient_id(), program.getProgram_id()));
        patientProgram.setPatient(patient);
        patientProgram.setTherapy_program(program);
        patientProgram.setRegistration_date(dto.getRegistrationDate());
        patientProgram.setPayment(payment);

        // can not update left to pay

        return patientProgramDAO.update(patientProgram);
    }



    @Override
    public boolean deletePatientProgram(String patientName, String programName) {
        String patientId = patientDAO.findByPatientName(patientName).getFirst().getPatient_id();
        String programId = therapyProgramDAO.findByTherapyProgramName(programName).getFirst().getProgram_id();
        return patientProgramDAO.delete(patientId, programId);
    }

    @Override
    public ArrayList<PatientProgramDto> getAllPatientPrograms() {
        List<PatientProgram> patientPrograms = patientProgramDAO.getAll();
        ArrayList<PatientProgramDto> patientProgramDtos = new ArrayList<>();

        for (PatientProgram patientProgram : patientPrograms) {
            PatientProgramDto dto = new PatientProgramDto();
            dto.setPatientId(patientProgram.getPatient().getPatient_id());
            dto.setPatientName(patientProgram.getPatient().getName());
            dto.setProgramId(patientProgram.getTherapy_program().getProgram_id());
            dto.setProgramName(patientProgram.getTherapy_program().getName());
            dto.setLeftToPay(patientProgram.getProgram_fee());

//            dto.setPaymentId(patientProgram.getPayment().getPayment_id());
            if (patientProgram.getPayment() != null) {
                dto.setPaymentId(patientProgram.getPayment().getPayment_id());
            } else {
                dto.setPaymentId(null); // or leave it unset
            }

            dto.setRegistrationDate(patientProgram.getRegistration_date());
            patientProgramDtos.add(dto);
        }
        return patientProgramDtos;
    }



    @Override
    public PatientDto findByPatientName(String patientName) {
        List<Patient> patients = patientDAO.findByPatientName(patientName);
        if (patients.isEmpty()) return null;

        Patient patient = patients.get(0);
        PatientDto patientDto = new PatientDto();
        patientDto.setPatientId(patient.getPatient_id());
        patientDto.setName(patient.getName());
        patientDto.setEmail(patient.getEmail());
        patientDto.setPhone(patient.getPhone());
        patientDto.setAddress(patient.getAddress());
        patientDto.setMedicalHistory(patient.getMedical_history());

        return patientDto;
    }

    @Override
    public TherapyProgramDto findByProgramName(String programName) {
        List<TherapyProgram> programs = therapyProgramDAO.findByTherapyProgramName(programName);
        if (programs.isEmpty()) return null;

        TherapyProgram program = programs.get(0);
        TherapyProgramDto programDto = new TherapyProgramDto();
        programDto.setProgramId(program.getProgram_id());
        programDto.setName(program.getName());
        programDto.setDescription(program.getDescription());
        programDto.setDuration(program.getDuration());

        return programDto;
    }

    @Override
    public ArrayList<PatientProgramDto> search(String name, boolean isPatient) {
        ArrayList<PatientProgramDto> patientProgramDtos = new ArrayList<>();
        List<PatientProgram> patientPrograms;

        if (isPatient) {
            List<Patient> patients = patientDAO.findByPatientName(name);
            if (patients.isEmpty()) return patientProgramDtos;

            String patientId = patients.get(0).getPatient_id();
            patientPrograms = patientProgramDAO.findByPatientId(patientId);
        } else {
            List<TherapyProgram> programs = therapyProgramDAO.findByTherapyProgramName(name);
            if (programs.isEmpty()) return patientProgramDtos;

            String programId = programs.get(0).getProgram_id();
            patientPrograms = patientProgramDAO.findByProgramId(programId);
        }

        for (PatientProgram patientProgram : patientPrograms) {
            PatientProgramDto dto = new PatientProgramDto();
            dto.setPatientId(patientProgram.getPatient().getPatient_id());
            dto.setPatientName(patientProgram.getPatient().getName());
            dto.setProgramId(patientProgram.getTherapy_program().getProgram_id());
            dto.setProgramName(patientProgram.getTherapy_program().getName());
            dto.setLeftToPay(patientProgram.getProgram_fee());

//            dto.setPaymentId(patientProgram.getPayment().getPayment_id());
            if (patientProgram.getPayment() != null) {
                dto.setPaymentId(patientProgram.getPayment().getPayment_id());
            } else {
                dto.setPaymentId("No Payment");
            }

            dto.setRegistrationDate(patientProgram.getRegistration_date());
            patientProgramDtos.add(dto);
        }

        return patientProgramDtos;
    }



    @Override
    public String getNextPatientProgramPK() {
        return null;
    }

    @Override
    public List<PatientProgramDto> getProgramsByPatientId(String patientId) {
        List<PatientProgram> patientPrograms = patientProgramDAO.findByPatientId(patientId);
        List<PatientProgramDto> dtos = new ArrayList<>();

        for (PatientProgram pp : patientPrograms) {
            PatientProgramDto dto = new PatientProgramDto();
            dto.setPatientId(pp.getPatient().getPatient_id());
            dto.setPatientName(pp.getPatient().getName());
            dto.setProgramId(pp.getTherapy_program().getProgram_id());
            dto.setProgramName(pp.getTherapy_program().getName());
            dto.setLeftToPay(pp.getTherapy_program().getFee());
            dto.setRegistrationDate(pp.getRegistration_date());

            if (pp.getPayment() != null) {
                dto.setPaymentId(pp.getPayment().getPayment_id());
            } else {
                dto.setPaymentId("No Payment");
            }

            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<PatientProgramDto> getPatientsByProgramId(String programId) {
        List<PatientProgram> patientPrograms = patientProgramDAO.findByProgramId(programId);
        List<PatientProgramDto> dtos = new ArrayList<>();

        for (PatientProgram pp : patientPrograms) {
            PatientProgramDto dto = new PatientProgramDto();
            dto.setPatientId(pp.getPatient().getPatient_id());
            dto.setPatientName(pp.getPatient().getName());
            dto.setProgramId(pp.getTherapy_program().getProgram_id());
            dto.setProgramName(pp.getTherapy_program().getName());
            dto.setLeftToPay(pp.getTherapy_program().getFee());
            dto.setRegistrationDate(pp.getRegistration_date());

            if (pp.getPayment() != null) {
                dto.setPaymentId(pp.getPayment().getPayment_id());
            } else {
                dto.setPaymentId("No Payment");
            }

            dtos.add(dto);
        }

        return dtos;
    }


    @Override
    public boolean updateTherapyProgramFeeOfPatient(String patientId, String programId, BigDecimal newFee) {
        return patientProgramDAO.updateTherapyProgramFee(patientId, programId, newFee);
    }

    @Override
    public PatientProgramDto searchPatientProgramFromBothIds(String patientId, String programId) {
        Optional<PatientProgram> optionalPP = patientProgramDAO.findById(patientId, programId);

        if (optionalPP.isPresent()) {
            PatientProgram patientProgram = optionalPP.get();
            PatientProgramDto dto = new PatientProgramDto();

            dto.setPatientId(patientProgram.getPatient().getPatient_id());
            dto.setPatientName(patientProgram.getPatient().getName());
            dto.setProgramId(patientProgram.getTherapy_program().getProgram_id());
            dto.setProgramName(patientProgram.getTherapy_program().getName());
            dto.setLeftToPay(patientProgram.getTherapy_program().getFee());

            if (patientProgram.getPayment() != null) {
                dto.setPaymentId(patientProgram.getPayment().getPayment_id());
            } else {
                dto.setPaymentId("No Payment");
            }

            dto.setRegistrationDate(patientProgram.getRegistration_date());

            return dto;
        }

        return null;
    }


}
