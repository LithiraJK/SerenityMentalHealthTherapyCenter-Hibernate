package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PatientProgramDto {
    private String patientId;
    private String patientName;
    private String programId;
    private String programName;
    private LocalDate registrationDate;
    private String paymentId;
    private BigDecimal leftToPay;
}
