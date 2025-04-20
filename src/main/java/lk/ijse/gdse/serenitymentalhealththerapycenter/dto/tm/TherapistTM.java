package lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TherapistTM {
    private int therapistId;
    private String therapistName;
    private String email;
    private String address;
    private String phone;
    private Date dateOfBirth;
    private String status;
}
