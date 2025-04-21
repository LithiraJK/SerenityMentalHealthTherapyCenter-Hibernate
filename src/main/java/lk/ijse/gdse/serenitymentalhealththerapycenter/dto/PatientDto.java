package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientDto {

    private String patientId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String medicalHistory;

}
