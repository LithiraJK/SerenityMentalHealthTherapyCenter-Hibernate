package lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TherapyProgramTM {
    private String programId;
    private String name;
    private String duration;
    private BigDecimal fee;
    private String description;
}
