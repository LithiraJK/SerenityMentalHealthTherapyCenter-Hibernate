package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.UserDto;

import java.util.ArrayList;

public interface UserBO {

    public String validateUser(String username, String password);
    public boolean registerUser(UserDto dto);
    public boolean updateUser(UserDto dto);
    public boolean deleteUser(String userId);
    public ArrayList<UserDto> searchUser(String userId);
    public String generateNextUserId();
    public ArrayList<UserDto> getAllUsers();
    boolean isUserExists(String username, String password);
    String getUserRole(String username, String password);

}
