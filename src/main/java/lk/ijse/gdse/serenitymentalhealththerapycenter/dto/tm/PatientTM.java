package lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientTM {
    private String patientId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String medicalHistory;
}
