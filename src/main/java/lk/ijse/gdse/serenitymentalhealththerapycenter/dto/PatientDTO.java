package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private String patientId;
    private String name;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;

    public PatientDTO(String name, String address, String gender, String dateOfBirth, String email, String phone) {
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phone;
    }
}
