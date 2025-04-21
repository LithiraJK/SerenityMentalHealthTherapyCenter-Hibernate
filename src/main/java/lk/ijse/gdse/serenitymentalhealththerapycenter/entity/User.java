package lk.ijse.gdse.serenitymentalhealththerapycenter.entity;

import jakarta.persistence.*;
import lombok.*;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User implements SuperEntity {
    @Id
    private String user_id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role; // "Admin" or "Receptionist"

//    public void setPassword(String password) {
//        this.password = new BCryptPasswordEncoder().encode(password);
//    }

}
