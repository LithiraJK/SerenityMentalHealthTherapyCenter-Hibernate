module lk.ijse.gdse.serenitymentalhealththerapycenter {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires static lombok;

    opens lk.ijse.gdse.serenitymentalhealththerapycenter.config to jakarta.persistence;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter.entity to org.hibernate.orm.core;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter to javafx.fxml;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter.controller to javafx.fxml;

    exports lk.ijse.gdse.serenitymentalhealththerapycenter;
}