package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;



import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapySessionDto {
    private String sessionId;
    private String patientId;
    private String therapyProgramId;
    private String therapistId;
    private String availabilityId;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private int duration;
    private String status;
}
