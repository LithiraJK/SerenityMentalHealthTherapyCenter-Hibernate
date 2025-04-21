package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "therapist")
public class Therapist implements SuperEntity {
    @Id
    private String therapist_id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapySession> therapySessions;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapistAvailability> availabilities;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapistProgram> therapistPrograms;
}
