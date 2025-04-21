package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TherapistProgramId {
    private String therapistId;
    private String programId;
}
