package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "therapy_program")
public class TherapyProgram implements SuperEntity {
    @Id
    private String program_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Lob
    private String description;

    @OneToMany(mappedBy = "therapy_program", cascade = CascadeType.ALL)
    private List<TherapySession> therapySessions;

    @OneToMany(mappedBy = "therapy_program", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "therapy_program", cascade = CascadeType.ALL)
    private List<PatientProgram> patientPrograms;

    @OneToMany(mappedBy = "therapy_program", cascade = CascadeType.ALL)
    private List<TherapistProgram> therapistPrograms;
}
