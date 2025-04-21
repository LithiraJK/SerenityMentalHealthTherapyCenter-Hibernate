package lk.ijse.gdse.serenitymentalhealththerapycenter.config;


import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private static SessionFactory sessionFactory;
    private FactoryConfiguration(){
        Configuration configuration = new Configuration();
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        configuration.setProperties(properties);

        configuration.addAnnotatedClass(User.class)
                .addAnnotatedClass(Patient.class)
                .addAnnotatedClass(PatientProgram.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Therapist.class)
                .addAnnotatedClass(TherapistAvailability.class)
                .addAnnotatedClass(TherapistProgram.class)
                .addAnnotatedClass(TherapyProgram.class)
                .addAnnotatedClass(TherapySession.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        return (factoryConfiguration == null) ? new FactoryConfiguration() : factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

}