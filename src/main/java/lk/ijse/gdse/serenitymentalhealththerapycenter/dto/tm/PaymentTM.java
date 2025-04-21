package lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm;


import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapyProgram;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.TherapySession;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentTM {
    private String paymentId;
    private String patientId;
    private String therapyProgramId;
    private String therapySessionId;  // Nullable for upfront payments
    private BigDecimal amount;
    private LocalDate paymentDate;

    public PaymentTM(boolean isSearch, String paymentId, String patient, String program, String session, BigDecimal amount, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.patientId = patient;
        this.therapyProgramId = program;
        this.therapySessionId = session != null ? session : "N/A";
        this.amount = BigDecimal.valueOf(amount.doubleValue());
        this.paymentDate = paymentDate;
    }

}
