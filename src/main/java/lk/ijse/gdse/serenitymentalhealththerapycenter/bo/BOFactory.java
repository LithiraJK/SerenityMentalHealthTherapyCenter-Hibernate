package lk.ijse.gdse.serenitymentalhealththerapycenter.bo;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapySessionBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {

    }

    public static BOFactory getInstance() {
        return boFactory == null ? new BOFactory() : boFactory;
    }

    public enum BOType {
        PATIENT, THERAPIST, APPOINTMENT, THERAPYPROGRAM, USER, THERAPYSESSION, PAYMENT
    }

    public SuperBO getBO(BOType type) {
        switch (type) {
            case PATIENT:
                return new PatientsBOImpl();
            case THERAPIST:
                return new TherapistsBOImpl();
            case APPOINTMENT:
                return new AppointmentBOImpl();
            case THERAPYPROGRAM:
                return new TherapyProgramBOImpl();
            case USER:
                return new UserBOImpl();
            case THERAPYSESSION:
                return new TherapySessionBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            default:
                return null;
        }
    }
}
