package lk.ijse.gdse.serenitymentalhealththerapycenter.config;

import lk.ijse.gdse.serenitymentalhealththerapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {

        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Patient.class)
                    .addAnnotatedClass(Payment.class)
                    .addAnnotatedClass(Appointment.class)
                    .addAnnotatedClass(Therapist.class)
                    .addAnnotatedClass(TherapyProgram.class)
                    .addAnnotatedClass(TherapySession.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to build SessionFactory.", e);
        }
    }

    public static FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            synchronized (FactoryConfiguration.class) {
                if (factoryConfiguration == null) {
                    factoryConfiguration = new FactoryConfiguration();
                }
            }
        }
        return factoryConfiguration;
    }

    public Session getSession() {
        if (sessionFactory != null) {
            return sessionFactory.openSession();
        } else {
            throw new IllegalStateException("SessionFactory is not initialized.");
        }
    }
}