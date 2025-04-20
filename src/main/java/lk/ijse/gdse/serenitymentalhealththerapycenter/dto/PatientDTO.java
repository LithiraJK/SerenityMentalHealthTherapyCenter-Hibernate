package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private String patientId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String dateOfBirth;

    public PatientDTO(String name, String email, String phone, String address, String gender, String dateOfBirth) {
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phone;
    }
}