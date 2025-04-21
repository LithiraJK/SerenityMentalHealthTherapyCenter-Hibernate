package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistDto {
    private String therapistId;
    private String name;
    private String email;
    private String phone;
    private String specialization;

}
