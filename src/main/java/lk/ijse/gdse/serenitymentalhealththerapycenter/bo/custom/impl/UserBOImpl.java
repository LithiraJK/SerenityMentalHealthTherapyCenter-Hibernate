package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.UserBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.SuperDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.UserDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl.UserDAOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.UserDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBOImpl implements UserBO {
    UserDAO userDAO = new UserDAOImpl();

    public String validateUser(String username, String password){
        return userDAO.validateUser(username, password);
    }
    public boolean registerUser(UserDto dto){
        User user = new User();
        user.setUser_id(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(dto.getPassword());

        return userDAO.save(user);
    }

    // --------------------------------------------

    @Override
    public boolean updateUser(UserDto dto) {
        User user = new User();
        user.setUser_id(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(dto.getPassword());

        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(String userId) {
        return userDAO.delete(userId);
    }

    @Override
    public ArrayList<UserDto> searchUser(String name) {
        Optional<User> optionalUser = userDAO.findByName(name);
        ArrayList<UserDto> userDtos = new ArrayList<>();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getUser_id());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }

        return userDtos;
    }


    @Override
    public String generateNextUserId() {
        Optional<String> lastPkOpt = userDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get(); // e.g., U005
            int num = Integer.parseInt(lastPk.replace("U", ""));
            return String.format("U%03d", num + 1);
        } else {
            return "U001";
        }
    }

    @Override
    public ArrayList<UserDto> getAllUsers() {
        List<User> users = userDAO.getAll();

        ArrayList<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getUser_id());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public boolean isUserExists(String username, String password) {
        // Using the DAO layer to check if user exists
        String role = userDAO.validateUser(username, password);
        return role != null;
    }

    @Override
    public String getUserRole(String username, String password) {
        // Using the DAO layer to retrieve user role
        return userDAO.validateUser(username, password);
    }


}
