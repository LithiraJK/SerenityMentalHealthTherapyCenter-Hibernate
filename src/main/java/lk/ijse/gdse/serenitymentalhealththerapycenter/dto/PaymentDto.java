package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

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

public class PaymentDto {
    private String paymentId;
    private Patient patient;
    private TherapyProgram therapyProgram;
    private TherapySession therapySession;
    private BigDecimal amount;
    private LocalDate paymentDate;
}

