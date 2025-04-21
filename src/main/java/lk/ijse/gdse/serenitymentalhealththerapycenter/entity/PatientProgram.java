package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "patient_program")
public class PatientProgram implements SuperEntity{

    @EmbeddedId
    private PatientProgramId id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private TherapyProgram therapy_program;

    @Column(nullable = false)
    private LocalDate registration_date;

    @ManyToOne
    @JoinColumn(name = "payment_id") // Nullable for future developments
    private Payment payment;
}
