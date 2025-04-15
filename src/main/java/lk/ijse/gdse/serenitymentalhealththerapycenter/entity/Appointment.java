package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String appointmentId;
    private Date date;

    @ManyToOne
    private Therapist therapist;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private TherapyProgram therapyProgram;

    @ManyToOne
    private Payment payment;
}
