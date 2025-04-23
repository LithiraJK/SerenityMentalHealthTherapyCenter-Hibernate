module lk.ijse.gdse.serenitymentalhealththerapycenter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires javafx.base;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires java.naming;
    requires mysql.connector.j;
    requires spring.security.crypto;


    opens lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm to javafx.base;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter.config to jakarta.persistence;
    opens lk.ijse.gdse.serenitymentalhealththerapycenter.entity to org.hibernate.orm.core;

    opens lk.ijse.gdse.serenitymentalhealththerapycenter to javafx.fxml;
    exports lk.ijse.gdse.serenitymentalhealththerapycenter;
    exports lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

    opens lk.ijse.gdse.serenitymentalhealththerapycenter.controller to javafx.fxml;
}