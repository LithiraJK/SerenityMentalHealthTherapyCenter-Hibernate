package lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapySessionTM {
    private String sessionId;
    private String therapistId;
    private String patientId;
    private String therapyProgramId;
    private String availabilityId;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private int duration;
    private String status;
}
