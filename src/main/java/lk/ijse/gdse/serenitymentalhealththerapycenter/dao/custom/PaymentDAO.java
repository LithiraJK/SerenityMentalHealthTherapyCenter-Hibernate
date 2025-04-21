package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom;

import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Patient;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.Payment;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentDAO {
    public boolean save(Payment entity);
    public boolean update(Payment entity);
    public boolean delete(String pk);
    public List<Payment> getAll();
    public List<Payment> findByName(String name);
    public Optional<Payment> findById(String pk);
    public Optional<String> getLastPK();
    public List<Payment> findByDate(LocalDate date);

}
