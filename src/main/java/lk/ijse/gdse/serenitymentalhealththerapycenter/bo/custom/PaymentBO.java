package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PaymentDto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;


public interface PaymentBO extends SuperBO {

    public boolean savePayment(PaymentDto dto);
    public boolean updatePayment(PaymentDto dto);
    public boolean deletePayment(String paymentId);
    public ArrayList<PaymentDto> getAllPayments();
    public ArrayList<PaymentDto> searchByPatientName(String name);
    public ArrayList<PaymentDto> searchByDate(LocalDate date);
    public String getNextPaymentPK();


    public PaymentDto constructPaymentDto(String paymentId, String patientId, String programId, String sessionId, BigDecimal amount, LocalDate date);

}
