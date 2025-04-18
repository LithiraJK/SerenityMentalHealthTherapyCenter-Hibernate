package lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.impl;

import lk.ijse.gdse.serenitymentalhealththerapycenter.dao.custom.UserDAO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.User;
import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {
    @Override
    public void initializeAdmin() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        Query<User> query = session.createQuery("FROM User WHERE role = :role", User.class);
        query.setParameter("role", "admin");
        User adminUser = query.uniqueResult();

        if (adminUser == null) {
            User newAdmin = new User();
            newAdmin.setUsername("admin");
            newAdmin.setPassword("123");
            newAdmin.setRole("admin");
            session.persist(newAdmin);
            System.out.println("Admin user created.");
        } else {
            System.out.println("Admin user already exists.");
        }

        transaction.commit();
        session.close();
    }

    @Override
    public boolean validateUser(String username, String password) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            String hql = "FROM User WHERE username = :username AND password = :password";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            User user = query.uniqueResult();

            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<User> getAllData() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(User Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(User Dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean existId(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(User id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String getNewId() throws SQLException, ClassNotFoundException {
        return "";
    }

    @Override
    public ArrayList<User> search(User newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public User findById(User entity) throws SQLException {
        return null;
    }
}
