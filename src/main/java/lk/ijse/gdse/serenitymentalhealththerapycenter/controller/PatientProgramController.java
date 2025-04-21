package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PatientProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.PatientProgramBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.PatientProgramTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.util.SetBackgroundUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientProgramController implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<PatientProgramTM, String> patientIdCol;

    @FXML
    private TableColumn<PatientProgramTM, String> patientNameCol;

    @FXML
    private TableColumn<PatientProgramTM, String> programNameCol;

    @FXML
    private TextField patientIdTxt;

    @FXML
    private TextField patientNameTxt;

    @FXML
    private TableView<PatientProgramTM> patientProgramTable;

    @FXML
    private Button patientSearchButton;

    @FXML
    private TableColumn<PatientProgramTM, String> paymentIdCol;

    @FXML
    private TextField paymentIdTxt;

    @FXML
    private TableColumn<PatientProgramTM, String> programIdCol;

    @FXML
    private TextField programIdTxt;

    @FXML
    private TextField programNameTxt;

    @FXML
    private Button programSearchButton;

    @FXML
    private TableColumn<PatientProgramTM, LocalDate> registerDateCol;

    @FXML
    private DatePicker registerDateTxt;

    @FXML
    private ToggleButton searchToggleButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private Button updateButton;

    @FXML
    private AnchorPane bodyPane;

    private final PatientProgramBO programBO = new PatientProgramBOImpl();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetBackgroundUtil setBackground = new SetBackgroundUtil();
        setBackground.setBackgroundImage(bodyPane, 1300, 760);

        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("programId"));
        programNameCol.setCellValueFactory(new PropertyValueFactory<>("programName"));
        registerDateCol.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));

        refreshTable();
    }

    private void refreshTable() {
        ArrayList<PatientProgramDto> programList = programBO.getAllPatientPrograms();
        ObservableList<PatientProgramTM> programTMS = FXCollections.observableArrayList();

        for (PatientProgramDto dto : programList) {
            programTMS.add(new PatientProgramTM(
                    dto.getPatientId(),
                    dto.getPatientName(),
                    dto.getProgramId(),
                    dto.getProgramName(),
                    dto.getRegistrationDate(),
                    dto.getPaymentId()
            ));
        }

        patientProgramTable.setItems(programTMS);
    }

    private void clearFields() {
        patientIdTxt.clear();
        patientNameTxt.clear();
        programIdTxt.clear();
        programNameTxt.clear();
        paymentIdTxt.clear();
        registerDateTxt.setValue(null);
    }

    @FXML
    void tableClick(MouseEvent event) {
        PatientProgramTM selected = patientProgramTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            patientIdTxt.setText(selected.getPatientId());
            patientNameTxt.setText(selected.getPatientName());
            programIdTxt.setText(selected.getProgramId());
            programNameTxt.setText(selected.getProgramName());
            registerDateTxt.setValue(selected.getRegistrationDate());
            paymentIdTxt.setText(selected.getPaymentId());
        }
    }

    @FXML
    void delete(ActionEvent event) {
//        String paymentId = paymentIdTxt.getText();
        String patientName = patientNameTxt.getText();
        String programName = programNameTxt.getText();

        if (programBO.deletePatientProgram(patientName, programName)) {
            showAlert("Success", "Program deleted successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to delete program", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void save(ActionEvent event) {
        PatientProgramDto dto = new PatientProgramDto(
                patientIdTxt.getText(),
                patientNameTxt.getText(),
                programIdTxt.getText(),
                programNameTxt.getText(),
                registerDateTxt.getValue(),
                paymentIdTxt.getText()
        );

        if (programBO.savePatientProgram(dto)) {
            showAlert("Success", "Program saved successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to save program", Alert.AlertType.ERROR);
        }
    }

    public void refreshPage(){
        clearFields();
        refreshTable();
    }

    @FXML
    void searchToggle(ActionEvent event) {
        if (searchToggleButton.isSelected()) {
            searchToggleButton.setText("Search by Program");
        } else {
            searchToggleButton.setText("Search by Patient");
        }
    }

    @FXML
    void search(ActionEvent event) {
        String query = searchTxt.getText().trim();

        if (query.isEmpty()) {
            showAlert("Input Error", "Please enter a search term", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }

        ObservableList<PatientProgramTM> programTMS = FXCollections.observableArrayList();

        if (searchToggleButton.isSelected()) {
            ArrayList<PatientProgramDto> programList = programBO.search(query , false);
            for (PatientProgramDto dto : programList) {
                programTMS.add(new PatientProgramTM(
                        dto.getPatientId(),
                        dto.getPatientName(),
                        dto.getProgramId(),
                        dto.getProgramName(),
                        dto.getRegistrationDate(),
                        dto.getPaymentId()
                ));
            }
        } else {
            ArrayList<PatientProgramDto> programList = programBO.search(query, true);
            for (PatientProgramDto dto : programList) {
                programTMS.add(new PatientProgramTM(
                        dto.getPatientId(),
                        dto.getPatientName(),
                        dto.getProgramId(),
                        dto.getProgramName(),
                        dto.getRegistrationDate(),
                        dto.getPaymentId()
                ));
            }
        }

        if (programTMS.isEmpty()) {
            showAlert("Not Found", "No results found for: " + query, Alert.AlertType.INFORMATION);
        }

        patientProgramTable.setItems(programTMS);

    }

    @FXML
    void searchPatient(ActionEvent event) {
        String name = patientNameTxt.getText().trim();
        PatientDto patient = programBO.findByPatientName(name);
        if (patient == null) {
            showAlert("Not Found", "Patient not found", Alert.AlertType.WARNING);
            return;
        }
        patientIdTxt.setText(patient.getPatientId());
        patientNameTxt.setText(patient.getName());
    }

    @FXML
    void searchProgram(ActionEvent event) {
        String name = programNameTxt.getText().trim();
        TherapyProgramDto program = programBO.findByProgramName(name);
        if (program == null) {
            showAlert("Not Found", "Program not found", Alert.AlertType.WARNING);
            return;
        }
        programIdTxt.setText(program.getProgramId());
        programNameTxt.setText(program.getName());
    }

    @FXML
    void update(ActionEvent event) {
        PatientProgramDto dto = new PatientProgramDto(
                patientIdTxt.getText(),
                patientNameTxt.getText(),
                programIdTxt.getText(),
                programNameTxt.getText(),
                registerDateTxt.getValue(),
                paymentIdTxt.getText()
        );

        if (programBO.updatePatientProgram(dto)) {
            showAlert("Success", "Program updated successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to update program", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
