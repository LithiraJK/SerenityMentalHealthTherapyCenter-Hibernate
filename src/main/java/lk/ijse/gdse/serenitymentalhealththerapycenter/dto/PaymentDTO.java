package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String paymentId;
    private String status;
    private Double amount;
    private Date paymentDate;
}
