package lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PatientTM {
    private String patientId;
    private String name;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
}
