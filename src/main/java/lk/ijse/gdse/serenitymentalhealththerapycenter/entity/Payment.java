package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String paymentId;
    private String status;
    private Double amount;
    private Date paymentDate;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointmentList;
}
