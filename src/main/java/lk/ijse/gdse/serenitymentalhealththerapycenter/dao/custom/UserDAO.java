package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.User;


import java.util.List;
import java.util.Optional;

public interface UserDAO{

    public boolean save(User entity);
    public boolean update(User entity);
    public boolean delete(String pk);
    public List<User> getAll();
    public Optional<User> findByName(String name);
    public User findByID(String pk);
    public Optional<String> getLastPK();
    public String validateUser(String username, String password);


}
