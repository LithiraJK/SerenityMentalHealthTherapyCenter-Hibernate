package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "therapist_availability")
public class TherapistAvailability implements SuperEntity {
    @Id
    private String availability_id;

    @ManyToOne
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    @Column(nullable = false)
    private LocalDate available_date;

    @Column(nullable = false)
    private LocalTime start_time;

    @Column(nullable = false)
    private LocalTime end_time;

    @Column(nullable = false)
    private boolean is_available = true;

    @OneToOne(mappedBy = "therapistAvailability", cascade = CascadeType.ALL)
    private TherapySession session;

    public TherapistAvailability(String availabilityId, Therapist therapist, LocalDate availableDate, LocalTime startTime, LocalTime endTime, boolean available) {
        this.availability_id = availabilityId;
        this.therapist = therapist;
        this.available_date = availableDate;
        this.start_time = startTime;
        this.end_time = endTime;
        this.is_available = available;

    }

    // Helper method to manage bidirectional relationship
    public void setSession(TherapySession session) {
        if (session == null) {
            if (this.session != null) {
                this.session.setTherapistAvailability(null);
            }
        } else {
            session.setTherapistAvailability(this);
        }
        this.session = session;
        this.is_available = (session == null);
    }

    // Check if this slot contains the given time
    public boolean containsTime(LocalTime time) {
        return !time.isBefore(start_time) && time.isBefore(end_time);
    }

    // Check if this slot overlaps with another slot
    public boolean overlaps(TherapistAvailability other) {
        return this.available_date.equals(other.available_date) &&
                !this.end_time.isBefore(other.start_time) &&
                !other.end_time.isBefore(this.start_time);
    }

}
