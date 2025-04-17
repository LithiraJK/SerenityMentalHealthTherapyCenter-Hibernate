package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TherapyProgramDTO {
    private int programId;
    private String programName;
    private String duration;
    private double programFee;

    public TherapyProgramDTO(String programName, String programDuration, String programFee) {
            this.programName = programName;
            this.programFee = Double.parseDouble(programFee);
            this.duration = programDuration;
    }
}
