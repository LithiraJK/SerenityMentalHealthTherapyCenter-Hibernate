package lk.ijse.gdse.serenitymentalhealththerapycenter.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String role;
}
