package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TherapistDTO {
    private int therapistId;
    private String therapistName;
    private String email;
    private String phone;
    private String address;
    private Date dateOfBirth;
    private String status;

    public TherapistDTO(String name, String email, String phone, String address, Date dob, String status) {
        this.therapistName = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dob;
        this.status = status;
    }
}
