package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private String appointmentId;
    private Date reservationDate;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
}
