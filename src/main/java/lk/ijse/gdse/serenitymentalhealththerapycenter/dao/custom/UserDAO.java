package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.CrudDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.SuperDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.User;

public interface UserDAO extends SuperDAO, CrudDAO<User> {
    void initializeAdmin();

    boolean validateUser(String username, String password);
}
