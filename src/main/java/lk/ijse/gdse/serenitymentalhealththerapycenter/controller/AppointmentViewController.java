package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.AppointmentTM;

import java.sql.Date;

public class AppointmentViewController {
    @FXML
    private Label lblAppoinmentId;

    @FXML
    private DatePicker datePickerDate;

    @FXML
    private ComboBox<String> cmbTherapist;

    @FXML
    private ComboBox<String> cmbPatient;

    @FXML
    private ComboBox<String> cmbTherapyProgram;

    @FXML
    private TableView<AppointmentTM> tblAppointment;

    @FXML
    private TableColumn<AppointmentTM, String> colAppointment;

    @FXML
    private TableColumn<AppointmentTM, Date> colDate;

    @FXML
    private TableColumn<AppointmentTM, String> colTherapist;

    @FXML
    private TableColumn<AppointmentTM, String> colPatient;

    @FXML
    private TableColumn<AppointmentTM, String> colTherapyProgram;

    @FXML
    private Label lblProgramFee;

    @FXML
    private JFXButton btnPay;

    @FXML
    void btnPaymentOnAction(ActionEvent event) {

    }
}
