package lk.ijse.gdse.serenitymentalhealththerapycenter.dao;


import java.util.List;
import java.util.Optional;


public interface CrudDAO <T> extends SuperDAO{
     boolean save(T entity);
     boolean update(T entity);
     boolean delete(String pk);
     List<T> getAll();
     Optional<T> findByName(String pk);
     Optional<String> getLastPK();

}