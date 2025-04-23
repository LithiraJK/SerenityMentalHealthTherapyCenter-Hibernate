package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistAvailabilityDto {
    private String availabilityId;
    private String therapistId;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> availableSlots;
    private boolean isAvailable;
}
