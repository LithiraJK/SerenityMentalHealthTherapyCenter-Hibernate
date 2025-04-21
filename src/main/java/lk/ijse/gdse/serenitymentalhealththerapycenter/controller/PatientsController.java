package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.PatientBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.PatientTM;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientsController implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<PatientTM, String> patientAddressCol;

    @FXML
    private TextField patientAddressTxt;

    @FXML
    private TableColumn<PatientTM, String> patientEmailCol;

    @FXML
    private TextField patientEmailTxt;

    @FXML
    private TableColumn<PatientTM, String> patientHistoryCol;

    @FXML
    private TextArea patientHistoryTxt;

    @FXML
    private TableColumn<PatientTM, String> patientIdCol;

    @FXML
    private TextField patientIdTxt;

    @FXML
    private TableColumn<PatientTM, String> patientNameCol;

    @FXML
    private TextField patientNameTxt;

    @FXML
    private TableColumn<PatientTM, String> patientPhoneCol;

    @FXML
    private TextField patientPhoneTxt;

    @FXML
    private TableView<PatientTM> patientsTable;

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

    private final PatientBOImpl patientBO = new PatientBOImpl();

    public void initialize(URL location, ResourceBundle resources){
        SetBackgroundUtil setBackground = new SetBackgroundUtil();
        setBackground.setBackgroundImage(bodyPane, 1300, 760);

        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        patientPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        patientAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        patientHistoryCol.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));
        try {
            refreshPage();
        }catch (Exception e) {
            throw e;
        }

    }

    public void refreshPage(){
        patientIdTxt.clear();
        patientNameTxt.clear();
        patientEmailTxt.clear();
        patientPhoneTxt.clear();
        patientAddressTxt.clear();
        patientHistoryTxt.clear();
        try {
            patientIdTxt.setText(patientBO.getNextPatientPK());
            refreshTable();
        } catch (Exception e) {
            throw e;
        }
    }

    public void refreshTable(){
        ArrayList<PatientDto> patientDtos = patientBO.getAllPatients();
        if (patientDtos != null && !patientDtos.isEmpty()) {
            ObservableList<PatientTM> patientTMS = FXCollections.observableArrayList();
            for(PatientDto patientDto : patientDtos){
                PatientTM patient = new PatientTM(
                        patientDto.getPatientId(),
                        patientDto.getName(),
                        patientDto.getEmail(),
                        patientDto.getPhone(),
                        patientDto.getAddress(),
                        patientDto.getMedicalHistory()
                );
                patientTMS.add(patient);
            }
            patientsTable.setItems(patientTMS);
        } else {
            patientsTable.setItems(FXCollections.emptyObservableList());
        }
    }



    @FXML
    void tableClick(MouseEvent event) {
        PatientTM selectedPatient = patientsTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            patientIdTxt.setText(selectedPatient.getPatientId());
            patientNameTxt.setText(selectedPatient.getName());
            patientEmailTxt.setText(selectedPatient.getEmail());
            patientPhoneTxt.setText(selectedPatient.getPhone());
            patientAddressTxt.setText(selectedPatient.getAddress());
            patientHistoryTxt.setText(selectedPatient.getMedicalHistory());
        }
    }

    @FXML
    void delete(ActionEvent event) {
        String patientId = patientIdTxt.getText();
        if (patientBO.deletePatient(patientId)) {
            showAlert("Success", "Patient deleted successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to delete patient", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void save(ActionEvent event) {
        PatientDto patientDto = new PatientDto(
                patientIdTxt.getText(),
                patientNameTxt.getText(),
                patientEmailTxt.getText(),
                patientPhoneTxt.getText(),
                patientAddressTxt.getText(),
                patientHistoryTxt.getText()
        );

        if (patientBO.savePatient(patientDto)) {
            showAlert("Success", "Patient saved successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to save patient", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String name = searchTxt.getText().trim();

        if (name.isEmpty()) {
            showAlert("Input Error", "Please enter a name to search", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }

        ArrayList<PatientDto> patientDtos = patientBO.findByPatientName(name);

        if (patientDtos != null && !patientDtos.isEmpty()) {
            ObservableList<PatientTM> patientTMS = FXCollections.observableArrayList();

            for (PatientDto patientDto : patientDtos) {
                PatientTM patientTM = new PatientTM(
                        patientDto.getPatientId(),
                        patientDto.getName(),
                        patientDto.getEmail(),
                        patientDto.getPhone(),
                        patientDto.getAddress(),
                        patientDto.getMedicalHistory()
                );
                patientTMS.add(patientTM);
            }

            patientsTable.setItems(patientTMS);
        } else {
            showAlert("Not Found", "No patient found with that name", Alert.AlertType.WARNING);
            patientsTable.setItems(FXCollections.emptyObservableList());
        }
    }


    @FXML
    void update(ActionEvent event) {
        PatientDto patientDto = new PatientDto(
                patientIdTxt.getText(),
                patientNameTxt.getText(),
                patientEmailTxt.getText(),
                patientPhoneTxt.getText(),
                patientAddressTxt.getText(),
                patientHistoryTxt.getText()
        );

        if (patientBO.updatePatient(patientDto)) {
            showAlert("Success", "Patient updated successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to update patient", Alert.AlertType.ERROR);
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
