package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.PatientBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.PatientTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.util.ValidateUtil;
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
        String patientId = patientIdTxt.getText();
        String name = patientNameTxt.getText();
        String email = patientEmailTxt.getText();
        String phone = patientPhoneTxt.getText();
        String address = patientAddressTxt.getText();
        String medicalHistory = patientHistoryTxt.getText();

        // Reset all field styles
        ValidateUtil.setFocusColorForTextField(patientNameTxt, true);
        ValidateUtil.setFocusColorForTextField(patientEmailTxt, true);
        ValidateUtil.setFocusColorForTextField(patientPhoneTxt, true);
        ValidateUtil.setFocusColorForTextField(patientAddressTxt, true);
        ValidateUtil.setFocusColorForTextArea(patientHistoryTxt, true);

        // Validate required fields
        boolean isNameValid = ValidateUtil.isRequiredField(name);
        boolean isEmailValid = ValidateUtil.isRequiredField(email);
        boolean isPhoneValid = ValidateUtil.isRequiredField(phone);

        ValidateUtil.setFocusColorForTextField(patientNameTxt, isNameValid);
        ValidateUtil.setFocusColorForTextField(patientEmailTxt, isEmailValid);
        ValidateUtil.setFocusColorForTextField(patientPhoneTxt, isPhoneValid);

        if (!isNameValid || !isEmailValid || !isPhoneValid) {
            showAlert("Input Error", "Please fill in all required fields (Name, Email, Phone)", Alert.AlertType.ERROR);
            return;
        }

        // Validate email format
        isEmailValid = ValidateUtil.isValidEmail(email);
        ValidateUtil.setFocusColorForTextField(patientEmailTxt, isEmailValid);
        if (!isEmailValid) {
            showAlert("Input Error", "Please enter a valid email address", Alert.AlertType.ERROR);
            return;
        }

        // Validate phone number format
        isPhoneValid = ValidateUtil.isValidPhoneNumberAny(phone);
        ValidateUtil.setFocusColorForTextField(patientPhoneTxt, isPhoneValid);
        if (!isPhoneValid) {
            showAlert("Input Error", "Please enter a valid phone number", Alert.AlertType.ERROR);
            return;
        }

        PatientDto patientDto = new PatientDto(
                patientId,
                name,
                email,
                phone,
                address,
                medicalHistory
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
        String patientId = patientIdTxt.getText();
        String name = patientNameTxt.getText();
        String email = patientEmailTxt.getText();
        String phone = patientPhoneTxt.getText();
        String address = patientAddressTxt.getText();
        String medicalHistory = patientHistoryTxt.getText();

        // Reset all field styles
        ValidateUtil.setFocusColorForTextField(patientNameTxt, true);
        ValidateUtil.setFocusColorForTextField(patientEmailTxt, true);
        ValidateUtil.setFocusColorForTextField(patientPhoneTxt, true);
        ValidateUtil.setFocusColorForTextField(patientAddressTxt, true);
        ValidateUtil.setFocusColorForTextArea(patientHistoryTxt, true);

        // Validate required fields
        boolean isNameValid = ValidateUtil.isRequiredField(name);
        boolean isEmailValid = ValidateUtil.isRequiredField(email);
        boolean isPhoneValid = ValidateUtil.isRequiredField(phone);

        ValidateUtil.setFocusColorForTextField(patientNameTxt, isNameValid);
        ValidateUtil.setFocusColorForTextField(patientEmailTxt, isEmailValid);
        ValidateUtil.setFocusColorForTextField(patientPhoneTxt, isPhoneValid);

        if (!isNameValid || !isEmailValid || !isPhoneValid) {
            showAlert("Input Error", "Please fill in all required fields (Name, Email, Phone)", Alert.AlertType.ERROR);
            return;
        }

        // Validate email format
        isEmailValid = ValidateUtil.isValidEmail(email);
        ValidateUtil.setFocusColorForTextField(patientEmailTxt, isEmailValid);
        if (!isEmailValid) {
            showAlert("Input Error", "Please enter a valid email address", Alert.AlertType.ERROR);
            return;
        }

        // Validate phone number format
        isPhoneValid = ValidateUtil.isValidPhoneNumberAny(phone);
        ValidateUtil.setFocusColorForTextField(patientPhoneTxt, isPhoneValid);
        if (!isPhoneValid) {
            showAlert("Input Error", "Please enter a valid phone number", Alert.AlertType.ERROR);
            return;
        }

        PatientDto patientDto = new PatientDto(
                patientId,
                name,
                email,
                phone,
                address,
                medicalHistory
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
