package lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.SuperBO;

public interface UserBO extends SuperBO {
    void initializeAdmin();

    boolean isUserExists(String username, String password);
}
