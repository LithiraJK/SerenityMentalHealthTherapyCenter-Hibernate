package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapySessionDTO {
    private String therapySessionId;
    private Date date;
    private Timestamp startTime;
    private Timestamp endTime;
    private String description;
}
