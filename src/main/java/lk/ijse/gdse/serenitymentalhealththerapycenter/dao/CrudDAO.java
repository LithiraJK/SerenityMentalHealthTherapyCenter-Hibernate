package lk.ijse.gdse.serenitymentalhealththerapycenter.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO {
    public ArrayList<T> getAllData() throws SQLException, ClassNotFoundException;

    public boolean save(T Dto) throws SQLException, ClassNotFoundException;

    public boolean update(T Dto) throws SQLException, ClassNotFoundException;

    public boolean existId(String id) throws SQLException, ClassNotFoundException;

    public boolean delete(T id) throws SQLException, ClassNotFoundException;

    public String getNewId() throws SQLException, ClassNotFoundException;

    public ArrayList<T> search(T newValue) throws SQLException, ClassNotFoundException;

    public T findById(T entity) throws SQLException;
}
