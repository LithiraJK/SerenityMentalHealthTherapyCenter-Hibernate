module lk.ijse.gdse.serenitymentalhealththerapycenter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires static lombok;
    requires net.sf.jasperreports.core;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires java.management;

    opens lk.ijse.gdse.serenitymentalhealththerapycenter.config to jakarta.persistence;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter.entity to org.hibernate.orm.core;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter to javafx.fxml;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter.controller to javafx.fxml;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm to javafx.base;
    exports lk.ijse.gdse.serenitymentalhealththerapycenter;
}